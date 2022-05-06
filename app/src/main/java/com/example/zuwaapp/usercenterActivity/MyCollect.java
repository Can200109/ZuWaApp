package com.example.zuwaapp.usercenterActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.Gaussian.CardAdapter;
import com.example.zuwaapp.Gaussian.CardScaleHelper;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.ImageLookActivity;
import com.example.zuwaapp.entity.Collect;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.ngss.MultiImageView;
import com.example.zuwaapp.util.GlideLoadImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuwaapp.Constant.FIND_COLLECT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;

public class MyCollect extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private List<Product> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private CardAdapter cardAdapter = new CardAdapter(mList);

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_COLLECT_BY_PHONENUMBER:
                    Result<List<Collect>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Collect>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Collect> data = findResult.getData();
                        if(data == null ||"".equals(data)){
                            Log.e("收藏","无数据");
                        }else {
                            for(Collect collect:data){
                                new Method().findProductById(collect.getProductId(),handler);
                            }
                        }
                    } else {
                        Toast.makeText(MyCollect.this,"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        Product product = findProductById.getData();
                        mList.add(product);
                        cardAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_my_collect);

        Collect collect = new Collect(Constant.PHONENUMBER);
        (new Method()).findCollectByPhoneNumber(collect,handler);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_collect_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(cardAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);

    }
}