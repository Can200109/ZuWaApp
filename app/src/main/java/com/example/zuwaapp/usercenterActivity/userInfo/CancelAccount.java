package com.example.zuwaapp.usercenterActivity.userInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CancelAccount.this,"铁子，不是什么都可以点点点的，既然注册了，就给我好好用，别一天天的想着注销账号",Toast.LENGTH_LONG).show();
            }
        });

    }
}