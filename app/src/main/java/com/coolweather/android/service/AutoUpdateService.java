package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.coolweather.android.Define;
import com.coolweather.android.WeatherActivity;
import com.coolweather.android.gson.AQI;
import com.coolweather.android.gson.ForecastWeather;
import com.coolweather.android.gson.LifeIndex;
import com.coolweather.android.gson.NowWeather;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    private Weather weather;

    private ForecastWeather forecastWeather;

    private NowWeather nowWeather;

    private AQI aqi;

    private LifeIndex lifeIndex;

    private int count = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000; // 8 hours
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.JsonToWeather(weatherString);
            String weatherId = weather.basic.weatherId;

            String forecastUrl = "https://free-api.heweather.net/s6/weather/forecast?location=" +
                    weatherId + "&key=54d072a434454f4f8545db0112f80c89";
            String nowUrl = "https://free-api.heweather.net/s6/weather/now?location=" +
                    weatherId + "&key=54d072a434454f4f8545db0112f80c89";
            String AQIUrl = "https://free-api.heweather.net/s6/air/now?location=" +
                    weatherId + "&key=54d072a434454f4f8545db0112f80c89";
            String LifeUrl = "https://free-api.heweather.net/s6/weather/lifestyle?location=" +
                    weatherId + "&key=54d072a434454f4f8545db0112f80c89";

            requestAndUpdate(forecastUrl, Define.FORECAST);
            requestAndUpdate(nowUrl, Define.NOW);
            requestAndUpdate(AQIUrl, Define.AQI);
            requestAndUpdate(LifeUrl, Define.LIFESTYLE);
        }
    }

    private void requestAndUpdate(String url, final int type) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();

                switch (type) {
                    case Define.FORECAST:
                        forecastWeather = Utility.handleForecastResponse(responseText);
                        break;
                    case Define.NOW:
                        nowWeather = Utility.handleNowResponse(responseText);
                        break;
                    case Define.AQI:
                        aqi = Utility.handleAQIResponse(responseText);
                        break;
                    case Define.LIFESTYLE:
                        lifeIndex = Utility.handleLifeIndexResponse(responseText);
                        break;
                    default:
                }
                synchronizedStorage();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

        });
    }

    synchronized private void synchronizedStorage() {
        count++;

        if (4 == count) {
            if (forecastWeather != null && nowWeather != null &&
                    aqi != null && lifeIndex != null &&
                    "ok".equals(forecastWeather.status) && "ok".equals(nowWeather.status) &&
                    "ok".equals(lifeIndex.status)) {

                // assemble Weather bean
                weather = new Weather(forecastWeather, nowWeather, aqi, lifeIndex);

                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(AutoUpdateService.this)
                        .edit();
                editor.putString("weather", new Gson().toJson(weather).toString());
                editor.apply();
            }
        }
    }

    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }
}
