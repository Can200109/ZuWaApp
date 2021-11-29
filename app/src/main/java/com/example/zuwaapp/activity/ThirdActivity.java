package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zuwaapp.HomeActivity;
import com.example.zuwaapp.R;
import com.example.zuwaapp.bottomFragments.FiveFragment;

import androidx.appcompat.app.AppCompatActivity;


public class ThirdActivity extends AppCompatActivity {
    private Button btnwode3,btndenglu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode3);
        //获取控件对象
        btnwode3 = findViewById(R.id.btn_wode3);
        btndenglu = findViewById(R.id.btn_denglu);
        //注册点击事件监听器
        btnwode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到文件操作Activity
                finish();
            }
        });
        btndenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到文件操作Activity
                Intent intent = new Intent(
                        ThirdActivity.this,
                        HomeActivity.class
                );
                finish();
                startActivity(intent);

            }
        });

    }
}

