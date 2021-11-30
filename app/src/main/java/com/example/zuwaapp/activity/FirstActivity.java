package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;

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
import android.widget.Toast;


import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.bottomFragments.FiveFragment;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class FirstActivity extends AppCompatActivity {
    private Button MePushBack;
    private RentAdapter MePushAdapter;
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_PRODUCT_BY_PHONENUMBER:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
                        for(Product product:data){
                            productList.add(product);
                            List<String> photoName = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
                            List<String> photoUrl = new ArrayList<>();
                            for (String photo:photoName){
                                photoUrl.add(PRODUCT_PHOTO+product.getPhoneNumber()+"/"+product.getProductId()+"/"+photo);
                            }
                            MePushAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(FirstActivity.this,"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode1);
        //获取控件对象
        MePushBack = findViewById(R.id.MePush_back);

        //根据电话号找到我的发布并显示出来
        productList.clear();
//        (new Method()).findProductByPhoneNumber(Constant.PHONENUMBER,handler);
        (new Method()).findProductByPhoneNumber("12345678910",handler);
        ListView listView = findViewById(R.id.Me_push_list);
        MePushAdapter = new RentAdapter(productList,R.layout.me_push_layout,FirstActivity.this);
        listView.setAdapter(MePushAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("FirstA","点了事件"+position);
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this, Glace3Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


        //注册点击事件监听器
        MePushBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
