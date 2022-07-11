package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.alipay.PayActivity;
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

public class RentGlaceActivity extends AppCompatActivity {

    private ImageView ownerPhoto;
    private RentAdapter RentGlaceAdapter;
    private TextView userName, userPhone, userLocation, ownerName, dayPrice, mortPrice, totalPrice;
    private ListView productList1;
    private List<Product> productList = new ArrayList<>();
    private Button rentPayment, back;
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
                        User user = findUserByPhoneNumberResult.getData();
                       if(Constant.PHONENUMBER.equals(user.getPhoneNumber())){
                           userPhone.setText(user.getPhoneNumber());
                           userName.setText(user.getUserName());
                       }else {
                           ownerName.setText(user.getUserName());
                           String userPhoto = user.getUserPhoto();
                           if (userPhoto!=null){
                               String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto;
                               Log.e("图片在 : ", url);
                               Glide.with(RentGlaceActivity.this)
                                       .load(url)
                                       .placeholder(R.drawable.loading)
                                       .centerCrop()
                                       .dontAnimate()
                                       .into(ownerPhoto);
                           }
                       }
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        dayPrice.setText(product.getProductPrice()+"");
                        mortPrice.setText(product.getProductDeposit()+"");
                        totalPrice.setText(""+(product.getProductPrice()*30+product.getProductDeposit()));

                        new Method().findUserByPhoneNumber(product.getPhoneNumber(),handler);

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

        back = findViewById(R.id.detail_glace_back);
        userName = findViewById(R.id.buyer_name);
        userPhone = findViewById(R.id.buyer_phone);
        userLocation = findViewById(R.id.buyer_address);
        dayPrice = findViewById(R.id.day_price);
        mortPrice = findViewById(R.id.mort_price);
        totalPrice = findViewById(R.id.total_price);
        rentPayment = findViewById(R.id.rent_payment);
        ownerPhoto = findViewById(R.id.owner_photo);
        ownerName = findViewById(R.id.owner_name);


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


//        (new Method()).findUserByPhoneNumber("12345678910",handler);


        //获取到商品列表(这里的电话号是商品发布者的电话号)
        Log.e("商品id",bundle.getString("id"));
        (new Method()).findProductById(bundle.getString("id"),handler);

        //获取到当前用户的userName，phone，地点的话先传默认的
        //这里先默认一个用户
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

        productList1 = findViewById(R.id.ownRentProduct);
        RentGlaceAdapter = new RentAdapter(productList,R.layout.rent_product_layout,RentGlaceActivity.this);
        productList1.setAdapter(RentGlaceAdapter);
        //点击列表可以进入详情
        productList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                finish();

                //这个地方不需要
//                Intent intent = new Intent();
//                intent.setClass(RentGlaceActivity.this, GlaceActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("phone",productList.get(position).getPhoneNumber());
//                bundle.putString("id",productList.get(position).getProductId());
//                intent.putExtra("bundle",bundle);
//                startActivity(intent);


            }
        });

        //价格获取
        //在handler里

        //点击按钮付款
        rentPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //携带价格去支付页
                Intent intent1 = new Intent(RentGlaceActivity.this, PayActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("price",totalPrice.getText().toString());
                bundle1.putString("productId",bundle.getString("id"));
                Log.e("price为++",totalPrice.getText().toString());
                intent1.putExtra("bundle1",bundle1);
                startActivity(intent1);
                finish();

            }
        });
    }



}