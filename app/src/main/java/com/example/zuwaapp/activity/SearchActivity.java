package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.FIND_ALL;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RentAdapter SearchAdapter;
    private String context="";
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_ALL:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
//                        Toast.makeText(SearchActivity.this,"Search查找成功"+context,Toast.LENGTH_LONG).show();
                        for(Product product:data){
                            if(product.getProductName().contains(context)){
                                productList.add(product);
                            }
                        }
                        SearchAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SearchActivity.this,"Search查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        //点击搜索先跳过来
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        context = bundle.getString("context");

            //根据输入查询数据库，并显示数据
            //获取商品名字并判断
            //productList.clear();
            Log.e("search","有内容哦 "+bundle.getString("context"));
            (new Method()).findAllProduct(handler);
            Log.e("search","发现之后");
            ListView listView = findViewById(R.id.search_result);
            SearchAdapter = new RentAdapter(productList,R.layout.glace_result_layout,SearchActivity.this);
            listView.setAdapter(SearchAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, GlaceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone",productList.get(position).getPhoneNumber());
                    bundle.putString("id",productList.get(position).getProductId());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
            });

    }
}