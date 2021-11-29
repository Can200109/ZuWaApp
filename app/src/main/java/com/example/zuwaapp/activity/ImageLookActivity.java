package com.example.zuwaapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.zuwaapp.R;
import com.previewlibrary.GPreviewActivity;

public class ImageLookActivity extends GPreviewActivity {
    /***
     * 重写该方法
     * 使用你的自定义布局
     **/
    @Override
    public int setContentLayout() {
        return R.layout.activity_image_look;
    }
}