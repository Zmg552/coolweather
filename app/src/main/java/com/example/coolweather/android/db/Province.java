package com.example.coolweather.android.db;

public class Province {
    int id;
    String provinceName;
    int ProvinceCode;

    public int getId(){
        return id;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public int getProvinceCode(){
        return ProvinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        ProvinceCode = provinceCode;
    }
}
