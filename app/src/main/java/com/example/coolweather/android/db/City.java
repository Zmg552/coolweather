package com.example.coolweather.android.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    int localId;

    @SerializedName("name") // 与JSON字段匹配
    String cityName;

    @SerializedName("id") // 与JSON字段匹配
    int cityCode;

    int provinceId;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int id) {
        this.localId = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
