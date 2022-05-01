package com.example.zuwaapp.usercenterActivity.userInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.zuwaapp.R;

//注销用户页面
public class CancelAccount extends AppCompatActivity {
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_account);

        textView = findViewById(R.id.user_cancel_text);
        button = findViewById(R.id.user_cancel_button);

    }
}