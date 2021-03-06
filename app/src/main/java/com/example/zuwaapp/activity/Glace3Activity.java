package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.ADD_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.DELETE_PRODUCT;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.util.GlideLoadImage;
import com.example.zuwaapp.util.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.yds.library.MultiImageView;

import java.util.ArrayList;
import java.util.List;

public class Glace3Activity extends AppCompatActivity {
    private ImageView headPhoto;
    private ArrayList<ThumbViewInfo> mThumbViewInfoList;
    private ImageButton shouCan,glaceBack;
    private MultiImageView multiImageView;
    private Button deleteButton;
    private Product product;
    private TextView user, name, describe, price, RVprice,count;
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
                        User user0 = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_LONG).show();
                        user.setText(findUserByPhoneNumberResult.getData().getUserName());
                        List<String> userPhoto = gson.fromJson(user0.getUserPhoto(),new TypeToken<List<String>>(){}.getType());
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user0.getPhoneNumber()+"/"+userPhoto.get(0);
                            Log.e("tupianjiazai : ", url);
                            Glide.with(getApplicationContext())
                                    .load(url)
                                    .placeholder(R.drawable.loading)
                                    .circleCrop()
                                    .dontAnimate()
                                    .into(headPhoto);
                        }

                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        product = findProductById.getData();
                        List<String> photoName = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
                        List<String> photoUrl = new ArrayList<>();
                        for (String photo:photoName){
                            photoUrl.add(PRODUCT_PHOTO+product.getPhoneNumber()+"/"+product.getProductId()+"/"+photo);
                        }
                        mThumbViewInfoList = new ArrayList<>(); // ?????????????????????????????????
                        ThumbViewInfo item;
                        mThumbViewInfoList.clear();
                        for (int i = 0;i < photoUrl.size(); i++) {
                            Rect bounds = new Rect();
                            //new ThumbViewInfo(????????????);
                            item=new ThumbViewInfo(photoUrl.get(i));
                            item.setBounds(bounds);
                            mThumbViewInfoList.add(item);
                        }
                        multiImageView.setImagesData(photoUrl);
                        multiImageView.setMultiImageLoader(new GlideLoadImage());
                        multiImageView.setOnItemImageClickListener(new MultiImageView.OnItemImageClickListener() {
                            @Override
                            public void onItemImageClick(Context context, ImageView imageView, int index, List list) {
                                Log.e("onItemImageClick: ","????????????"+index+"?????????" );

                                GPreviewBuilder.from((Activity) context)
                                        //??????????????????????????????????????????8.0??????????????????????????????????????????
                                        .to(ImageLookActivity.class)
                                        .setData(mThumbViewInfoList)
                                        .setCurrentIndex(index)
                                        .setSingleFling(true)
                                        .setType(GPreviewBuilder.IndicatorType.Number)
                                        // ?????????
                                        .setType(GPreviewBuilder.IndicatorType.Dot)
                                        .start();

                            }
                        });
                        name.setText(product.getProductName());
                        describe.setText(product.getProductDescribe());
                        price.setText("??????:"+product.getProductPrice()+"/???");
                        RVprice.setText("??????"+product.getProductDeposit());
                        count.setText("??????:"+product.getProductCount());

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glace3);
        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        user = findViewById(R.id.tv_user);
        multiImageView = findViewById(R.id.detail_multi);
        name = findViewById(R.id.tv_name);
        describe = findViewById(R.id.tv_describe);
        price = findViewById(R.id.tv_price);
        RVprice = findViewById(R.id.tv_RVprice);
        count = findViewById(R.id.tv_count);
        deleteButton = findViewById(R.id.delete_product);



        (new Method()).findUserByPhoneNumber(bundle.getString("phone"),handler);
        Log.e("id",bundle.getString("id"));
        (new Method()).findProductById(bundle.getString("id"),handler);



        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????????????????
                Intent intent = new Intent(Glace3Activity.this,OwnUserHome.class);
                bundle.putString("phone",bundle.getString("phone"));
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????????????????????
                (new Method()).deleteProduct(product,handler);

                //????????????????????????
                Intent intent1 = new Intent(Glace3Activity.this,Success3Activity.class);
                startActivity(intent1);
            }
        });

        glaceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    private void init(){
        shouCan = findViewById(R.id.ib_shoucang);
        glaceBack = findViewById(R.id.glace_back);
        headPhoto = findViewById(R.id.iv_headPhoto);
        ZoomMediaLoader.getInstance().init(new ImageLoader());
    }
}