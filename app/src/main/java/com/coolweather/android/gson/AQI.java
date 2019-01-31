package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class AQI {

    @SerializedName("air_now_station")
    public AQICity city;

    public String status;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

    public AQICity getCity() {
        return city;
    }

    public void setCity(AQICity city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
