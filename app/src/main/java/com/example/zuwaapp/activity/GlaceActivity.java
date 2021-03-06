package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.DELETE_COLLECT;
import static com.example.zuwaapp.Constant.FIND_COLLECT;
import static com.example.zuwaapp.Constant.FIND_COLLECT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_PRODUCTTYPE;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;
import static com.example.zuwaapp.Constant.SET_COLOR;
import static com.example.zuwaapp.Constant.USER_URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
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
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.alipay.PayActivity;
import com.example.zuwaapp.entity.Collect;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.method.OnItemClickListener;
import com.example.zuwaapp.ngss.MultiImageView;
import com.example.zuwaapp.util.GlideLoadImage;
import com.example.zuwaapp.util.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.enitity.ThumbViewInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlaceActivity extends AppCompatActivity {
    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ImageView headPhoto,shouCan;
    private ArrayList<ThumbViewInfo> mThumbViewInfoList;
    private Button glaceBack;
    private MultiImageView multiImageView;
    private Button shop, btnStep,search;
    private TextView tvUser, name, describe, price, RVprice,count;
    private String ID, productType;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_PRODUCT_BY_PRODUCTTYPE:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
                        List<Product> ramData = new Constant().getRandomThreeInfoList(data,2);
//                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_LONG).show();
                        for(Product product:ramData){
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GlaceActivity.this,"????????????",Toast.LENGTH_LONG).show();
                    }
                    break;
                case SET_COLOR:
                    Result<Collect> data0 = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Collect>>(){}.getType());
                    Collect collect0 = data0.getData();
                    if(collect0==null){
                        Log.e( "handleMessage: ", "????????????");
                        shouCan.setImageResource(R.drawable.shoucang);

                    }else {
                        Log.e("collect.getId",collect0.getCollectId());
                        shouCan.setImageResource(R.drawable.shoucang3);
                    }
                    break;

                case FIND_COLLECT:
                    Result<Collect> data = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Collect>>(){}.getType());
                    Collect collect = data.getData();
                    if(collect==null){
                        Log.e( "handleMessage: ", "????????????");
                        //??????????????????????????????????????????????????????
//                        (new Method()).addCollect("12345678910",ID,handler);
                        (new Method()).addCollect(Constant.PHONENUMBER,ID,handler);
                        shouCan.setImageResource(R.drawable.shoucang3);
                    }else {
                        Log.e("collect.getId",collect.getCollectId());
                        //????????????????????????????????????????????????
                        (new Method()).deleteCollect(Constant.PHONENUMBER,ID,handler);
                        shouCan.setImageResource(R.drawable.shoucang);
                    }
                    break;
                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());

                    if (findUserByPhoneNumberResult.getCode()==200){
                        User user = findUserByPhoneNumberResult.getData();
                        tvUser.setText(findUserByPhoneNumberResult.getData().getUserName());
                        String userPhoto = user.getUserPhoto();
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto;
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
                        Product product = findProductById.getData();
                        productType = product.getProductType();
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
                                /**
                                 * ??????????????????
                                 * ????????????????????????????????????????????????
                                 * */
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
                        new Method().findProductByProductType(productType,handler);
                        if(product.getProductRent()==1){
                            shop.setText("??????");
                        }else {
                            shop.setText("?????????");
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glace);
        init();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        tvUser = findViewById(R.id.tv_user);
        multiImageView = findViewById(R.id.detail_multi);
        name = findViewById(R.id.tv_name);
        describe = findViewById(R.id.tv_describe);
        price = findViewById(R.id.tv_price);
        RVprice = findViewById(R.id.tv_RVprice);
        count = findViewById(R.id.tv_count);
        ID = bundle.getString("id");

        (new Method()).findUserByPhoneNumber(bundle.getString("phone"),handler);
        Log.e("id",bundle.getString("id"));
        (new Method()).findProductById(bundle.getString("id"),handler);

        headPhoto = findViewById(R.id.iv_headPhoto);

        //?????????????????????????????????????????????

        new Method().findCollectToSetColor(Constant.PHONENUMBER,ID,handler);
        shouCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????????????????
                //??????????????????????????????????????????????????????????????????????????????????????????
                //????????????????????????????????????????????????????????????
                //???????????????????????????????????????
                new Method().findCollectByProductIdAndPhoneNumber(Constant.PHONENUMBER,ID,handler);
            }
        });


        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.PHONENUMBER.equals(bundle.getString("phone"))){
//                if("12345678911".equals(bundle.getString("phone"))){
                    Toast.makeText(GlaceActivity.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                }else {
                    //??????????????????????????????????????????
                    Intent intent1 = new Intent(GlaceActivity.this, PayActivity.class);
                    //???phone???????????????
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("phone",bundle.getString("phone"));
                    bundle1.putString("id",bundle.getString("id"));
                    //??????????????????????????????
                    intent1.putExtra("bundle",bundle1);
                    startActivity(intent1);

                }

            }
        });

        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????????????????
                Intent intent = new Intent(GlaceActivity.this,OwnUserHome.class);
                bundle.putString("phone",bundle.getString("phone"));
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        glaceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GlaceActivity.this,OwnUserHome.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("phone",bundle.getString("phone"));
                intent1.putExtra("bundle",bundle1);
                startActivity(intent1);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GlaceActivity.this,SearchPage.class);
                startActivity(intent1);
            }
        });


        RecyclerView Rview = findViewById(R.id.glace_listRecommend);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        Rview.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(productList);
        Rview.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();

                //???????????????
                //?????????????????????????????????  ??????   ??????   ??????    ??????    ??????   ??????
                intent.setClass(GlaceActivity.this, GlaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone",productList.get(position).getPhoneNumber());
                bundle.putString("id",productList.get(position).getProductId());
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });



    }
    private void init(){
        shouCan = findViewById(R.id.ib_shoucang);
        shop = findViewById(R.id.btn_shop);
        glaceBack = findViewById(R.id.glace_back);
        headPhoto = findViewById(R.id.iv_headPhoto);
        btnStep = findViewById(R.id.glace_user_home);
        search = findViewById(R.id.btn_glace_search);
        ZoomMediaLoader.getInstance().init(new ImageLoader());
    }
}