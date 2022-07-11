package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.MODIFY;
import static com.example.zuwaapp.Constant.USER_URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.ResultActivity;
import com.example.zuwaapp.VerifyActivity;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ImageView photoButton;
    private TextView userId;
    private EditText userName, userPassword;
    private Button button;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MODIFY:
                    Result<User> modifyResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (modifyResult.getCode()==200){
                        /**此方法更新数据会返回更新后的数据如需取用请参考下边例子
                         * Result<>类型都是此格式取用数据
                         *
                         * */

                        modifyResult.getData().getPhoneNumber();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        init();

        //数据
//        userId.setText("12345678910");
        userId.setText(Constant.PHONENUMBER);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //完善信息
               String user_name = userName.getText().toString();
               String user_password = userPassword.getText().toString();
               User user = new User(user_name,user_password,Constant.PHONENUMBER);
//               User user = new User(user_name,user_password,"12345678910");
               Log.e("昵称",user_name);
               Log.e("密码",user_password);
               (new Method()).editUser(user,handler);
//               uploadImage(uriList,"12345678910");
               uploadImage(uriList,Constant.PHONENUMBER);
               //跳转
               Intent intent = new Intent(TransferActivity.this,
                       ResultActivity.class);
               startActivity(intent);
           }
       });

    }

    public void init() {
        photoButton = findViewById(R.id.user_headerPhoto);
        userId = findViewById(R.id.user_id);
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        button = findViewById(R.id.user_go);
    }
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
            Glide.with(this).load(uri).into(photoButton);
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