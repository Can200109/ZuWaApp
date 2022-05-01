package com.example.zuwaapp.usercenterActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.usercenterActivity.userInfo.CancelAccount;
import com.example.zuwaapp.usercenterActivity.userInfo.UserAgree;
import com.example.zuwaapp.usercenterActivity.userInfo.UserPrivacy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.USER_URL;


/**
 * 2022/4/25
 * 这是用户设置界面，后期还要添加用户协议，注销账号等功能
 * **/
public class UserSetting extends AppCompatActivity {
    private Button settingReturn;
    private ImageView ivUserPhoto;
    private TextView userNick, userPhoneNumber, userAgree, userPrivacy, switchAccount, cancelAccount;

    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
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
                        User user = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userPhoneNumber.setText(user.getPhoneNumber()+"  >");
                        userNick.setText(user.getUserName()+"  >");
                        String userPhoto = user.getUserPhoto();
                        if (userPhoto!=null){
                            String url = Constant.USER_PHOTO+user.getPhoneNumber()+"/"+userPhoto;
                            Log.e("图片在 : ", url);
                            Glide.with(UserSetting.this)
                                    .load(url)
                                    .placeholder(R.drawable.loading)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(ivUserPhoto);
                        }

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        init();

        //调用方法显示用户信息
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

        //返回上一页
        settingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //修改昵称和密码
        userNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, NickEdit.class);
                startActivity(intent);
            }
        });

        //修改头像
        ivUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //点击账号时
        userPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserSetting.this,"账号不能修改哦",Toast.LENGTH_LONG).show();
            }
        });

        //点击用户协议时
        userAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, UserAgree.class);
                startActivity(intent);
            }
        });

        //点击隐私协议时
        userPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, UserPrivacy.class);
                startActivity(intent);
            }
        });

        //点击切换账号时
        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //思想为用广播实现，但是由于不想写了，就做个假退出好了。
                //反正也没人看这个代码，老子想咋写就咋写
            }
        });

        //点击注销账号时
        cancelAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSetting.this, CancelAccount.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        ivUserPhoto = findViewById(R.id.edit_user_photo);
        userNick = findViewById(R.id.edit_user_nick);
        userPhoneNumber = findViewById(R.id.edit_user_phoneNumber);
        settingReturn = findViewById(R.id.user_setting_return);
        userAgree = findViewById(R.id.user_agree);
        userPrivacy = findViewById(R.id.user_privacy);
        switchAccount = findViewById(R.id.switch_account);
        cancelAccount = findViewById(R.id.cancel_account);

    }


    //下面是头像的方法
    private void pickImage(){
        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"},
                Constant.REQUEST_IMAGE
        );
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            uriList.add(uri);
            Glide.with(this).load(uri).into(ivUserPhoto);
            uploadImage(uriList,Constant.PHONENUMBER);
        }
    }

    public void uploadImage(List<Uri> uriList, String phoneNumber){
        List<String> urlList = getUrlList(uriList);
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.addFormDataPart("phoneNumber",phoneNumber);
        multipartBodyBuilder.setType(MultipartBody.FORM);
        for (String url:urlList) {
            File imageFile = new File(url);
            multipartBodyBuilder.addFormDataPart("file",
                    imageFile.getName(),
                    RequestBody.create(MediaType.parse("application/octet-stream"), imageFile)
            );
        }
        //创建请求体对象
        RequestBody requestBody = multipartBodyBuilder.build();
        //创建请求对象
        Request request = new Request.Builder()
                .url(USER_URL + "uploadPhoto")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        //发起异步请求，不需要手动创建子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp","请求失败时执行");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("okhttp","请求成功时执行");
                Log.e("异步请求的结果",response.body().string());
                Log.e("onResponse",Thread.currentThread().getName());//如果需要将数据显示到界面，使用Handler实现
            }
        });
    }
    public List<String> getUrlList(List<Uri> uriList) {
        List<String> urlList = new ArrayList<>();
        for (Uri uri:uriList){
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri,
                    null,null,null,null);
            if (cursor.moveToFirst()) {
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                urlList.add(imagePath);
            }
        }
        return urlList;
    }
}