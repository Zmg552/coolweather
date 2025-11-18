package com.example.coolweather.android.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    int localId;

    @SerializedName("name") // 与JSON字段匹配
    String countyName;

    // 3. 映射 JSON 中的 "id"（县编码）
    @SerializedName("id")
    private int countyCode;

    // 4. 映射 JSON 中的 "weather_id"（天气ID，注意下划线）
    @SerializedName("weather_id") // 必须与 JSON 字段完全一致（含下划线）
    private String weatherId;

    int cityId;

    public int getLocalId() { return localId; }

    public void setLocalId(int id) { this.localId = id; }
    public String getCountyName() { return countyName; }
    public void setCountyName(String countyName) { this.countyName = countyName; }

    public int getCountyCode() { return countyCode; }
    public void setCountyCode(int countyCode) { this.countyCode = countyCode; }

    public String getWeatherId() { return weatherId; }
    public void setWeatherId(String weatherId) { this.weatherId = weatherId; }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
}

