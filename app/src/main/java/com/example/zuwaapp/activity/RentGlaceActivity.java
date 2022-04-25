package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.ADD_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.alipay.AuthResult;
import com.example.zuwaapp.alipay.OrderInfoUtil2_0;
import com.example.zuwaapp.alipay.PayResult;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Rent;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RentGlaceActivity extends AppCompatActivity {

    private ImageButton back;
    private RentAdapter RentGlaceAdapter;
    private TextView userName, userPhone, userLocation, price;
    private ListView productList1;
    private List<Product> productList = new ArrayList<>();
    private Button rent;
    private Product product;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userPhone.setText(findUserByPhoneNumberResult.getData().getPhoneNumber());
                        userName.setText(findUserByPhoneNumberResult.getData().getUserName());
                        Log.e("id",findUserByPhoneNumberResult.getData().getPhoneNumber());
                        Log.e("昵称",findUserByPhoneNumberResult.getData().getUserName());
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        price.setText("合计："+(product.getProductPrice()*30+product.getProductDeposit()));
                        productList.add(product);
                        RentGlaceAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_glace);

        back = findViewById(R.id.rentown_back);
        userName = findViewById(R.id.ownRenttv_user);
        userPhone = findViewById(R.id.ownRentv_phone);
        userLocation = findViewById(R.id.ownRenttv_location);
        price = findViewById(R.id.price);
        rent = findViewById(R.id.rent);

        //携带数据跳过来
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        //点击返回键返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取到当前用户的userName，phone，地点的话先传默认的
        //这里先默认一个用户
          (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);
//        (new Method()).findUserByPhoneNumber("12345678910",handler);


        //获取到商品列表(这里的电话号是商品发布者的电话号)
        Log.e("商品id",bundle.getString("id"));
        (new Method()).findProductById(bundle.getString("id"),handler);

        productList1 = findViewById(R.id.ownRentProduct);
        RentGlaceAdapter = new RentAdapter(productList,R.layout.rent_product_layout,RentGlaceActivity.this);
        productList1.setAdapter(RentGlaceAdapter);
        //点击列表可以进入详情
        productList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(RentGlaceActivity.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        //价格获取
        //在handler里

        //点击按钮添加
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rent rent = new Rent(product.getProductName(),product.getProductDescribe(),product.getProductPrice(),product.getProductPhoto(),product.getProductType(),Constant.PHONENUMBER,bundle.getString("phone"),product.getProductId());
                (new Method()).addRent(rent,handler);
                //弹出支付页
                //payV2(view);
                Intent intent1 = new Intent(RentGlaceActivity.this,SuccessActivity.class);
                startActivity(intent1);

            }
        });
    }



}