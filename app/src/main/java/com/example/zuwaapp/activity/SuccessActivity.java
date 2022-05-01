package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.ResultActivity;
import com.example.zuwaapp.bottomFragments.FirstFragment;
import com.example.zuwaapp.entity.OrderType;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import static com.example.zuwaapp.Constant.ADD_ORDERTYPE;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

/**
 * Created by WuLiang.
 * <p>
 * Date: 2021/11/29
 *
 * update x_heng
 * 2022/4/29
 */
public class SuccessActivity extends AppCompatActivity {
    private String productId, productNumber;
    private Button success;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ADD_ORDERTYPE:
                    Result<OrderType> addOrder = gson.fromJson(msg.obj.toString(),new TypeToken<Result<OrderType>>(){}.getType());
                    if (addOrder.getCode()==200){
                        Toast.makeText(getApplicationContext(),"增加成功", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_success);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(!bundle.isEmpty()){
            productId = bundle.getString("productId");
            productNumber = bundle.getString("productNumber");
            Log.e("成功页的productId",productId+"");
        }else {
            Log.e("bundle是空的","瞅啥瞅，空的，还不赶快debug去，还在这瞅来瞅去的");
        }
        //接收到productId后，调用add方法，将商品增加到待发货里面
        OrderType orderType = new OrderType(Constant.PHONENUMBER, "待发货",productNumber,productId);
        new Method().addOrder(orderType,handler);

        //添加完后，有一个返回的按钮
        success = findViewById(R.id.success);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

}
