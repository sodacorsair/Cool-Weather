package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Lifestyle {

    public String type;

    @SerializedName("txt")
    public String info;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
