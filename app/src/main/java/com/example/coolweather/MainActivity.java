package com.example.coolweather;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this); // 必须先初始化
        // 主动触发数据库连接，强制创建表
        LitePal.getDatabase();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        // 检查是否首次创建，避免重复添加
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main, new ChooseAreaFragment()) // 假设布局中有id为main的容器
//                    .commit();
//        }

    }
}