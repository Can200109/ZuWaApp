package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.FIND_ALL;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_RENT;
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
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.bottomFragments.FiveFragment;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {
    private Button btnwode2;
    private RentAdapter MePushAdapter;
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_RENT:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
                        for (Product product:data){
                            productList.add(product);
                        }
                        MePushAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SecondActivity.this,"无数据",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode2);
        //获取控件对象
        btnwode2 = findViewById(R.id.btn_wode2);
        //注册点击事件监听器
        btnwode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到文件操作Activity
               finish();
            }
        });



        //先查出全部列表，然后根据条件添加
        productList.clear();
//        new Method().findRents("12345678910",1,handler);
        new Method().findRents(Constant.PHONENUMBER,1,handler);

        ListView listView = findViewById(R.id.ZuJieList);
        MePushAdapter = new RentAdapter(productList,R.layout.me_push_layout,SecondActivity.this);
        listView.setAdapter(MePushAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.e("FirstA","点了事件"+position);
                Intent intent = new Intent();
                intent.setClass(SecondActivity.this, Glace2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

    }
}

