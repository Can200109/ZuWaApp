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

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.OrderType;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuwaapp.Constant.FIND_ORDER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;

public class MyNoGoods extends AppCompatActivity {
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
                        //拿到数据
                        for(OrderType order:findOrderList.getData()){
                            //这里，emmm，自己体会，自己体会，不解释连招a接w接外圈刮接a接a接咳血接诺克萨斯断头台！
                            new Method().findProductById(order.getProductId(),handler);
                        }
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_no_goods);

        back = findViewById(R.id.no_goods_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //查数据库初始化数据
        OrderType orderTypeData = new OrderType(Constant.PHONENUMBER,"待发货");
        new Method().findOrder(orderTypeData,handler);


            /**
             * 绑定adapter展示,多的就不说了，这里能少些就少写，反正挂不了，没必要仿淘宝那个(商店+商品+按钮)的布局
             * 所以咱直接复用写过的那个RentAdapter，难不难看的，你就说能不能用吧 //手动狗头
             **/
            ListView noPaymentOrderList = findViewById(R.id.my_no_goods_page_list);
            Log.e("数据",productList.size()+"");
            Log.e("数据",productList.toString()+"");
            noPaymentAdapter = new RentAdapter(productList,R.layout.my_nogoods_list_pro,MyNoGoods.this);
            noPaymentOrderList.setAdapter(noPaymentAdapter);
            noPaymentOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyNoGoods.this, GlaceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",productList.get(position).getProductId());
                    bundle.putString("phone",productList.get(position).getPhoneNumber());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
            });


    }

}