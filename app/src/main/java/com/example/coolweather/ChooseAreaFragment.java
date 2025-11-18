package com.example.coolweather;

import static org.litepal.crud.LitePalSupport.*;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coolweather.android.db.City;
import com.example.coolweather.android.db.County;
import com.example.coolweather.android.db.Province;
import com.example.coolweather.android.util.HttpUtil;
import com.example.coolweather.android.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

//    选中的
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

//    在Fragment 与 Activity 完成 “绑定” 后，用于完成 Fragment 初始化和状态恢复的核心回调
    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel == LEVEL_CITY){
                    queryProvince();
                }else if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }
            }
        });
        queryProvince();
    }

    /**
     *查询所有的省，优先数据库，没有的话就去服务器查询
     */
    public void queryProvince(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        //从数据库中查询
        provinceList = LitePal.findAll(Province.class);
        Log.d("ProvinceQuery", "数据库查询结果数量：" + provinceList.size());
        if(provinceList != null && provinceList.size() > 0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            //通知适配器（Adapter）数据集发生了变化，要求适配器重新加载所有数据并刷新列表界面。
            adapter.notifyDataSetChanged();
            //将列表（ListView）的滚动位置设置到第 0 项（即列表的最顶部）。
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }

    /**
     *  查询所有的市
     */
    public void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceId = ?", // 字段名需与 City 类中的关联字段一致
                        String.valueOf(selectedProvince.getProvinceCode()))
                .find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询所有的县
     */
    public void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityId = ?",
                String.valueOf(selectedCity.getCityCode()))
                .find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address,"county");
        }
    }

    public void queryFromServer(String addr,final String type){
        showProgressDialog();
        HttpUtil.sendRequestWithOkHttp(addr, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //通过runOnUiThread回到主线程
                if(getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getProvinceCode());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getCityCode());
                }
                if(result){
                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                if("province".equals(type)){
                                    queryProvince();
                                }else if("city".equals(type)){
                                    queryCities();
                                }else if("county".equals(type)){
                                    queryCounties();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //显示进度对话框
    public void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    public void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
