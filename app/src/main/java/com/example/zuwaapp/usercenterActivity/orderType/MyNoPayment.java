package com.example.zuwaapp.usercenterActivity.orderType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.RentGlaceActivity;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.alipay.PayActivity;
import com.example.zuwaapp.entity.OrderType;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuwaapp.Constant.FIND_ORDER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

public class MyNoPayment extends AppCompatActivity {
    private Button back;
    private List<Product> productList = new ArrayList<>();
    private RentAdapter noPaymentAdapter;

    private Product product;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById =
                            gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        productList.add(product);
                        noPaymentAdapter.notifyDataSetChanged();
                    }
                    break;
                case FIND_ORDER:
                    Result<List<OrderType>> findOrderList = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<OrderType>>>(){}.getType());
                    if(findOrderList.getCode() == 200){
                        //????????????
                        for(OrderType order:findOrderList.getData()){
                            //?????????emmm????????????????????????????????????????????????a???w???????????????a???a????????????????????????????????????
                            new Method().findProductById(order.getProductId(),handler);
                        }
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_no_payment);

        back = findViewById(R.id.no_payment_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //???????????????????????????
        OrderType orderTypeData = new OrderType(Constant.PHONENUMBER,"?????????");
        new Method().findOrder(orderTypeData,handler);


            /**
             * ??????adapter??????,??????????????????????????????????????????????????????????????????????????????????????????(??????+??????+??????)?????????
             * ????????????????????????????????????RentAdapter????????????????????????????????????????????? //????????????
             **/
            ListView noPaymentOrderList = findViewById(R.id.my_no_payment_page_list);
            Log.e("??????",productList.size()+"");
            Log.e("??????",productList.toString()+"");
            noPaymentAdapter = new RentAdapter(productList,R.layout.my_nopayment_list_pro,MyNoPayment.this);
            noPaymentOrderList.setAdapter(noPaymentAdapter);
            noPaymentOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyNoPayment.this, PayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",productList.get(position).getProductId());
                    bundle.putString("phone",productList.get(position).getPhoneNumber());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
            });

    }
}