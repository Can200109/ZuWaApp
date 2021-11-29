package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.zuwaapp.R;
import com.example.zuwaapp.bottomFragments.FiveFragment;

import androidx.appcompat.app.AppCompatActivity;


public class FiveActivity extends AppCompatActivity {
    private Button btnwode4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode4);
        //获取控件对象
        btnwode4 = findViewById(R.id.btn_wode4);
        //注册点击事件监听器
        btnwode4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
