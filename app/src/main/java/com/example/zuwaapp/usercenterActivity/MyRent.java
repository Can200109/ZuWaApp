package com.example.zuwaapp.usercenterActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.Gaussian.CardAdapter;
import com.example.zuwaapp.Gaussian.CardScaleHelper;
import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.RentCardAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Rent;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuwaapp.Constant.FIND_RENT_BY_PHONENUMBER;

public class MyRent extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private List<Rent> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private RentCardAdapter cardAdapter = new RentCardAdapter(mList);

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_RENT_BY_PHONENUMBER:
                    Result<List<Rent>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Rent>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Rent> data = findResult.getData();
                        for (Rent rent:data){
                            mList.add(rent);
                        }
                        cardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MyRent.this,"无数据",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rent);

        new Method().findRentByPhoneNumber(Constant.PHONENUMBER,handler);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_rent_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(cardAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
    }
}