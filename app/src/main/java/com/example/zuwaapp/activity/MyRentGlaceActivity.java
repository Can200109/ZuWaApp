package com.example.zuwaapp.activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Rent;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
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

import static com.example.zuwaapp.Constant.ADD_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.DELETE_RENT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;


/**
 * 2022/4/29
 * x_heng
 *
 * 我不想写代码，我真的不想写代码
 * 这tm的重复操作，真的烦，复制粘贴我都懒的粘贴
 *
 *
 * 算了，，，
 *
 * 上面闹着玩的，我爱学习！！！
 * **/
public class MyRentGlaceActivity extends AppCompatActivity {
    private Button returnButton, btnSearch, btnShare, btnStep, shouCang, myBack;  //返回按钮,搜索框,分享按钮,踩一脚,收藏,我要还
    private ImageView headPhoto; //用户头像

    private Product product; //产品列表
    private TextView user, name, describe, price, RVprice,count;  //一些要显示的textView

    private ArrayList<ThumbViewInfo> mThumbViewInfoList; //图片预览实体类
    private MultiImageView multiImageView;  //图片展示，点击放大


    //下面就是线程，调用后台代码，不难，但是贼难受
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_USER_BY_PHONENUMBER:  //这里要找到发布者的信息，放到详情页里
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        User user0 = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        user.setText(findUserByPhoneNumberResult.getData().getUserName());
                        String userPhoto = user0.getUserPhoto();
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user0.getPhoneNumber()+"/"+userPhoto;
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
                        mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
                        ThumbViewInfo item;
                        mThumbViewInfoList.clear();
                        for (int i = 0;i < photoUrl.size(); i++) {
                            Rect bounds = new Rect();
                            //new ThumbViewInfo(图片地址);
                            item=new ThumbViewInfo(photoUrl.get(i));
                            item.setBounds(bounds);
                            mThumbViewInfoList.add(item);
                        }
                        multiImageView.setImagesData(photoUrl);
                        multiImageView.setMultiImageLoader(new GlideLoadImage());
                        multiImageView.setOnItemImageClickListener(new MultiImageView.OnItemImageClickListener() {
                            @Override
                            public void onItemImageClick(Context context, ImageView imageView, int index, List list) {
                                Log.e("onItemImageClick: ","点击了第"+index+"个图片" );
                                /**
                                 * 图片查看控件
                                 * 点击图片放大，且可以左右滑动浏览
                                 * */
                                GPreviewBuilder.from((Activity) context)
                                        //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
                                        .to(ImageLookActivity.class)
                                        .setData(mThumbViewInfoList)
                                        .setCurrentIndex(index)
                                        .setSingleFling(true)
                                        .setType(GPreviewBuilder.IndicatorType.Number)
                                        // 小圆点
                                        .setType(GPreviewBuilder.IndicatorType.Dot)
                                        .start();

                            }
                        });
                        name.setText(product.getProductName());
                        describe.setText(product.getProductDescribe());
                        price.setText("金额:"+product.getProductPrice()+"/天");
                        RVprice.setText("押金"+product.getProductDeposit());
                        count.setText("次数:"+product.getProductCount());

                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rent_glace);

        init(); //初始化

        //别的activity跳过来
        Intent intent = getIntent();
        //传过来的有 productId，ownerPhone
        Bundle bundle = intent.getBundleExtra("bundle");

        //初始化数据
        (new Method()).findUserByPhoneNumber(bundle.getString("ownerPhone"),handler);  //用户
        (new Method()).findProductById(bundle.getString("productId"),handler);  //商品

        //下面就是一系列点击事件

        //左上角返回
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索框点击事件
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRentGlaceActivity.this,SearchPage.class);
                startActivity(intent);
            }
        });
        //分享按钮的点击事件
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待实现
            }
        });

        //用户头像，用户昵称，踩一脚
        headPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用户详情页要的bundle是phone
                Intent intent1 = new Intent(MyRentGlaceActivity.this,OwnUserHome.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("phone",bundle.getString("ownerPhone"));
                intent1.putExtra("bundle",bundle1);
                startActivity(intent1);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyRentGlaceActivity.this,OwnUserHome.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("phone",bundle.getString("ownerPhone"));
                intent1.putExtra("bundle",bundle1);
                startActivity(intent1);
            }
        });
        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyRentGlaceActivity.this,OwnUserHome.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("phone",bundle.getString("ownerPhone"));
                intent1.putExtra("bundle",bundle1);
                startActivity(intent1);
            }
        });

        //点击我要还
        myBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行方法
                Rent rent = new Rent(bundle.getString("rentId"));
                Log.e("rentId+++",bundle.getString("rentId"));
                new Method().deleteRent(rent,handler);

                Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyRentGlaceActivity.this, Success2Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void init(){
        returnButton = findViewById(R.id.my_rent_glace_back);
        headPhoto = findViewById(R.id.my_rent_glace_iv_headPhoto);
        user = findViewById(R.id.my_rent_glace_tv_user);
        multiImageView = findViewById(R.id.my_rent_glace_detail_multi);
        name = findViewById(R.id.my_rent_glace_tv_name);
        describe = findViewById(R.id.my_rent_glace_tv_describe);
        price = findViewById(R.id.my_rent_glace_tv_price);
        RVprice = findViewById(R.id.my_rent_glace_tv_RVprice);
        count = findViewById(R.id.my_rent_glace_tv_count);  //次数不写了，不重要,设成0
        btnSearch = findViewById(R.id.btn_my_rent_glace_search);
        btnShare = findViewById(R.id.btn_my_rent_glace_share);
        btnStep = findViewById(R.id.my_rent_glace_user_home);
        shouCang = findViewById(R.id.my_rent_glace_ib_shoucang);
        myBack = findViewById(R.id.my_rent_glace_btn_back);
    }
}