package com.example.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.android.db.City;
import com.example.coolweather.android.db.County;
import com.example.coolweather.android.db.Province;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.util.List;

public class Utility {
    /**
     * 解析和处理返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                Gson gson = new Gson();
                List<Province> provinceList = gson.fromJson(response,
                        new TypeToken<List<Province>>() {}.getType());
                // 保存到数据库
                for (Province pro : provinceList) {
                    // 确认解析字段正常
                    Log.d("解析结果", "省份名：" + pro.getProvinceName() + "，编码：" + pro.getProvinceCode());

                    if (!pro.save()) { // 检查存储结果
                        Log.e("存储失败", "省份 " + pro.getProvinceName() + " 保存失败");
                        return false; // 只要有一个失败，整体返回失败
                    }
                }
                return true;
            } catch (JsonSyntaxException e) {
                Log.e("解析错误", "JSON格式错误：" + e.getMessage());
            } catch (JsonParseException e) {
                Log.e("解析错误", "解析过程异常：" + e.getMessage());
            } catch (Exception e) {
                Log.e("解析错误", "其他错误：" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 解析和处理返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            List<City> cityList = gson.fromJson(response,
                    new TypeToken<List<City>>() {
                    }.getType());
            for (City city : cityList) {
                Log.d("解析结果", "城市名：" + city.getCityName() + "，编码：" + city.getCityCode());
                city.setProvinceId(provinceId); // 关键：关联当前省份的ID（provinceCode）
                if (!city.save()) {
                    Log.e("存储失败", "城市 " + city.getCityName() + " 保存失败，原因：");
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            List<County> countyList = gson.fromJson(response,
                    new TypeToken<List<County>>() {
                    }.getType());
            for (County county : countyList) {
                Log.d("解析结果", "县名：" + county.getCountyName() + "，编码：" + county.getWeatherId());
                county.setCityId(cityId); // 关键：关联当前省份的ID（provinceCode）
                if (!county.save()) {
                    Log.e("存储失败", "县名 " + county.getCountyName() + " 保存失败，原因：");
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}

