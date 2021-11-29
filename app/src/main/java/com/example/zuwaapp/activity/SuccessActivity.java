package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zuwaapp.R;
import com.example.zuwaapp.ResultActivity;
import com.example.zuwaapp.bottomFragments.FirstFragment;

/**
 * Created by WuLiang.
 * <p>
 * Date: 2021/11/29
 */
public class SuccessActivity extends AppCompatActivity {
    private Button success;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_success);

        getIntent();
        success = findViewById(R.id.success);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

}
