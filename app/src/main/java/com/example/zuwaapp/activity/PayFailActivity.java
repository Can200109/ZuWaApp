package com.example.zuwaapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.OrderType;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static com.example.zuwaapp.Constant.ADD_ORDERTYPE;

public class PayFailActivity extends AppCompatActivity {
    private Button back;
    private String productId, productNumber;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ADD_ORDERTYPE:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fail);

        back = findViewById(R.id.fail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(!bundle.isEmpty()){
            productId = bundle.getString("productId");
            productNumber = bundle.getString("productNumber");
            Log.e("失败页的productId",productId+"");
        }else {
            Log.e("bundle是空的","瞅啥瞅，空的，还不赶快debug去，还在这瞅来瞅去的");
        }
        //接收到productId后，调用add方法，将商品增加到待发货里面
        OrderType orderType = new OrderType(Constant.PHONENUMBER, "待付款",productNumber,productId);
        new Method().addOrder(orderType,handler);
    }
}