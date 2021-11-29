package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.DELETE_COLLECT;
import static com.example.zuwaapp.Constant.FIND_COLLECT;
import static com.example.zuwaapp.Constant.FIND_COLLECT_COLOR;
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
import com.example.zuwaapp.entity.Collect;
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

public class GlaceActivity extends AppCompatActivity {
    private ImageView headPhoto;
    private ArrayList<ThumbViewInfo> mThumbViewInfoList;
    private ImageButton shouCan,glaceBack;
    private MultiImageView multiImageView;
    private Button shop;
    private TextView user, name, describe, price, RVprice,count;
    private String ID;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_COLLECT:
                    Result<List<Collect>> findCollect = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Collect>>>(){}.getType());
                    if(findCollect.getCode() == 200){
                        //根据手机号查找出来了collect
                        List<Collect> data = findCollect.getData();
                        if(data.isEmpty()){
                            shouCan.setImageResource(R.drawable.shoucang3);
                            (new Method()).addCollect("12345678910",ID,handler);
                        }else {
                            for(Collect collect:data){
                                //看看手机号对应的productId是否有该页面的productID

                                //如果存在
                                if(ID.equals(collect.getProductId())){
                                    Log.e("ID",ID);
                                    Log.e("collect.getId",collect.getCollectId());
                                    //删除这条记录，并且将图标设为透明
                                    (new Method()).deleteCollect("12345678910",ID,handler);
                                    shouCan.setImageResource(R.drawable.shoucang);

                                }else {
                                    //增加进去这条记录，并且将图标设为黑色
                                    (new Method()).addCollect("12345678910",ID,handler);
                                    shouCan.setImageResource(R.drawable.shoucang3);
                                }
                            }
                        }
                    }
                    break;
                case FIND_COLLECT_COLOR:
                    Result<List<Collect>> findCollectColor = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Collect>>>(){}.getType());
                    if(findCollectColor.getCode() == 200){
                        //根据手机号查找出来了collect
                        List<Collect> data = findCollectColor.getData();
                        for(Collect collect:data){
                            //看看手机号对应的productId是否有该页面的productID
                            //如果存在
                            if(ID.equals(collect.getProductId())){
                                //图标为黑色
                                shouCan.setImageResource(R.drawable.shoucang3);
                            }else {
                                //图标为透明
                                shouCan.setImageResource(R.drawable.shoucang);
                            }
                        }
                    }
                    break;


                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        user.setText(findUserByPhoneNumberResult.getData().getUserName());

                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200){
                        Product product = findProductById.getData();
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
                        if(product.getProductRent()==1){
                            shop.setText("预约");
                        }else {
                            shop.setText("我想要");
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
        user = findViewById(R.id.tv_user);
        multiImageView = findViewById(R.id.detail_multi);
        name = findViewById(R.id.tv_name);
        describe = findViewById(R.id.tv_describe);
        price = findViewById(R.id.tv_price);
        RVprice = findViewById(R.id.tv_RVprice);
        count = findViewById(R.id.tv_count);

        

        (new Method()).findUserByPhoneNumber(bundle.getString("phone"),handler);
        Log.e("id",bundle.getString("id"));
        (new Method()).findProductById(bundle.getString("id"),handler);

        ID = bundle.getString("id");

        //弹进这个页面，先判断收藏的颜色
        (new Method()).findCollectColorByPhoneNumber("12345678910",handler);

        shouCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //把数据存进数据库
                //判断，如果数据没在数据库里面，图标设置成黑色，并且将数据存入
                //如果在里面，图标变为白色，然后将数据清除
                //如何判断有没有在收藏表里面

                (new Method()).findCollectByPhoneNumber("12345678910",handler);
//                if (){
//                    shouCan.setImageResource(R.drawable.shoucang);
//                    isIconChange=false;
//
//                }else{
//                    //图标变为黑色
//                    shouCan.setImageResource(R.drawable.shoucang3);
//                    //将数据存入
//
//                    //调用方法
//
//
//                }

            }
        });



        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(Constant.PHONENUMBER.equals(bundle.getString("phone"))){
                if("12345678911".equals(bundle.getString("phone"))){
                    Toast.makeText(GlaceActivity.this, "不能租自己发布的东西哦", Toast.LENGTH_SHORT).show();
                }else {
                    //弹出支付页面
                    Log.e("支付","待实现");
                    //首先谈到付款页，然后点击付款将collect变为1，再写一个租借人的号码，然后根据租界人的号码和1查找
                    //携带物品信息，我的信息，跳转
                    Intent intent1 = new Intent(GlaceActivity.this,RentGlaceActivity.class);
                    //此phone是物品信息
                    bundle.putString("phone",bundle.getString("phone"));
                    bundle.putString("title",name.toString());
                    //我的信息根据电话号写
                    intent1.putExtra("bundle",bundle);
                    startActivity(intent1);

                }

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹到用户信息页面
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



    }
    private void init(){
        shouCan = findViewById(R.id.ib_shoucang);
        shop = findViewById(R.id.btn_shop);
        glaceBack = findViewById(R.id.glace_back);
        headPhoto = findViewById(R.id.iv_headPhoto);
        ZoomMediaLoader.getInstance().init(new ImageLoader());
    }
}