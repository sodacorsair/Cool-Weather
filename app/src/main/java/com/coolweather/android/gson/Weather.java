package com.coolweather.android.gson;

import java.util.ArrayList;
import java.util.List;

public class Weather {

    public Basic basic;

    public Now now;

    public AQI aqi;

    public Update update;

    public Comfort comfort;

    public CarWash carWash;

    public Sport sport;

    public String status;

    public List<Forecast> forecastList;

    public class Comfort {

        public String info;

    }

    public class CarWash {

        public String info;

    }

    public class Sport {

        public String info;

    }

    public Weather() {

    }

    public Weather(ForecastWeather forecastWeather, NowWeather nowWeather,
                   AQI aqi, LifeIndex lifeIndex) {
        basic = new Basic();
        now = new Now();
        update = new Update();
        comfort = new Comfort();
        carWash = new CarWash();
        sport = new Sport();
        forecastList = new ArrayList<>();

        this.basic.cityName = nowWeather.basic.cityName;
        this.basic.countyName = nowWeather.basic.countyName;
        this.basic.weatherId = nowWeather.basic.weatherId;

        this.now.temperature = nowWeather.now.temperature;
        this.now.info = nowWeather.now.info;

        if ("ok".equals(aqi.status)) {
            this.aqi = new AQI();
            this.aqi.city.aqi = aqi.city.aqi;
            this.aqi.city.pm25 = aqi.city.pm25;
        }

        this.update.updateTime = nowWeather.update.updateTime;

        this.comfort.info = lifeIndex.lifestyleList.get(0).info;
        this.carWash.info = lifeIndex.lifestyleList.get(6).info;
        this.sport.info = lifeIndex.lifestyleList.get(3).info;

        for (Forecast forecast : forecastWeather.forecastList) {
            this.forecastList.add(forecast);
        }
    }
}
