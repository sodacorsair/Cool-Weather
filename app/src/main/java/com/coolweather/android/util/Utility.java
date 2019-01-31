package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.Define;
import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.AQI;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.ForecastWeather;
import com.coolweather.android.gson.LifeIndex;
import com.coolweather.android.gson.Now;
import com.coolweather.android.gson.NowWeather;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    // parse and handle province level data returned from server
    public static boolean handleProvincesResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // parse and handle city data from server
    public static boolean handleCitiesResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // parse and handle county data from server
    public static boolean handleCountiesResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather JsonToWeather(String content) {
        return new Gson().fromJson(content, Weather.class);
    }

    public static ForecastWeather handleForecastResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, ForecastWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NowWeather handleNowResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, NowWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AQI handleAQIResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            AQI aqi = new Gson().fromJson(content, AQI.class);
            return aqi;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LifeIndex handleLifeIndexResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, LifeIndex.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleJsonResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
        String content = jsonArray.getJSONObject(0).toString();

        return content;
    }

}
