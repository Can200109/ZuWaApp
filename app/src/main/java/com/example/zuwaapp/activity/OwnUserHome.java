package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class OwnUserHome extends AppCompatActivity {
    private ImageView ownHead;
    private ImageButton ownBack;
    private TextView ownUser;
    private RentAdapter MePushAdapter;
    private List<Product> productList = new ArrayList<>();
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
                        ownUser.setText(findUserByPhoneNumberResult.getData().getUserName());
                        List<String> userPhoto = gson.fromJson(user.getUserPhoto(),new TypeToken<List<String>>(){}.getType());
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto.get(0);
                            Log.e("tupianjiazai : ", url);
                            Glide.with(getApplicationContext())
                                    .load(url)
                                    .placeholder(R.drawable.loading)
                                    .circleCrop()
                                    .dontAnimate()
                                    .into(ownHead);
                        }
                    }
                    break;
                case FIND_PRODUCT_BY_PHONENUMBER:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
//                        Toast.makeText(OwnUserHome.this,"查找成功",Toast.LENGTH_LONG).show();
                        for(Product product:data){
                            productList.add(product);
                            MePushAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(OwnUserHome.this,"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_user_home);
        ownBack = findViewById(R.id.own_back);
        ownUser = findViewById(R.id.owntv_user);
        ownHead = findViewById(R.id.owniv_headPhoto);

        Intent intent = getIntent();
        //这里获取用户名(根据电话号)
        Bundle bundle = intent.getBundleExtra("bundle");
        (new Method()).findUserByPhoneNumber(bundle.getString("phone"),handler);

        //获取该人出租列表
        //根据电话号找并显示出来
        productList.clear();
        //(new Method()).findProductByPhoneNumber(Constant.PHONENUMBER,handler);
        (new Method()).findProductByPhoneNumber(bundle.getString("phone"),handler);
        ListView listView = findViewById(R.id.own_products_list);
        MePushAdapter = new RentAdapter(productList,R.layout.glace_result_layout,OwnUserHome.this);
        listView.setAdapter(MePushAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.setClass(OwnUserHome.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

    }
}