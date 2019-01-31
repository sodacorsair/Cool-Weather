package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LifeIndex {

    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    public String status;

}
