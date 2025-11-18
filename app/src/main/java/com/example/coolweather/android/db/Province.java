package com.example.coolweather.android.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;
import org.litepal.annotation.Column;

public class Province extends LitePalSupport {
    private int localId;
    @SerializedName("name") // 与JSON字段匹配
    String provinceName;

    @SerializedName("id")
    int provinceCode;

    public int getLocalId(){ return localId; }
    public void setLocalId(int localId){ this.localId = localId; }
    public int getProvinceCode() { return provinceCode; }
    public void setProvinceCode(int provinceCode) { this.provinceCode = provinceCode; }
    public String getProvinceName() { return provinceName; }
    public void setProvinceName(String provinceName) { this.provinceName = provinceName; }
}
