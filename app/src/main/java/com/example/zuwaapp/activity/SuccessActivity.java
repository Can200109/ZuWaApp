package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zuwaapp.R;

/**
 * Created by WuLiang.
 * <p>
 * Date: 2021/11/29
 */
public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_success);
        Intent intent = getIntent();
        success();
    }

    public void success(){
        Intent intent = new Intent(SuccessActivity.this,FirstActivity.class);
        startActivity(intent);
    }
}
