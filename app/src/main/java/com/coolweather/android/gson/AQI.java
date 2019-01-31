package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class AQI {

    @SerializedName("air_now_city")
    public AQICity city;

    public String status;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

    public AQI() {
        city = new AQICity();
    }

}
