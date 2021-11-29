package com.example.zuwaapp.bottomFragments;

import static android.app.Activity.RESULT_OK;
import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.MODIFY;
import static com.example.zuwaapp.Constant.PRODUCT_URL;
import static com.example.zuwaapp.Constant.USER_URL;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.FirstActivity;
import com.example.zuwaapp.activity.FiveActivity;
import com.example.zuwaapp.activity.SecondActivity;
import com.example.zuwaapp.activity.ThirdActivity;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.util.GlideLoadImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FiveFragment extends Fragment {
    private List<Uri> uriList = new ArrayList<>();
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ImageView ivHead;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private TextView userName,userId;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_USER_BY_PHONENUMBER:
                    Result<User> findUserByPhoneNumberResult = gson.fromJson(msg.obj.toString(),new TypeToken<Result<User>>(){}.getType());
                    if (findUserByPhoneNumberResult.getCode()==200){
                        User user = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userId.setText(user.getPhoneNumber());
                        userName.setText(user.getUserName());
                        Glide.with(getContext()).load(Constant.USER_PHOTO+user.getPhoneNumber()+"/"+user.getUserPhoto());
                        Log.e("id",findUserByPhoneNumberResult.getData().getPhoneNumber());
                        Log.e("昵称",findUserByPhoneNumberResult.getData().getUserName());
                    }
                    break;
            }
        }
    };

    private static FiveFragment wdf;
    public static  FiveFragment getWoDeFragment(){
        if(wdf == null){
            wdf = new FiveFragment();
        }
        return wdf;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wode,
                container,
                false);
        userName = view.findViewById(R.id.userName);
        userId = view.findViewById(R.id.userId);
        ivHead = view.findViewById(R.id.iv_head);
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                uploadImage(uriList,"12345678910");
            }
        });
        //“我的发布”点击进入一个页面
        Button btnbutton1 = view.findViewById(R.id.btn_button1);

        (new Method()).findUserByPhoneNumber("12345678910",handler);

        btnbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(),
                        FirstActivity.class
                );
                startActivity(intent);
            }
        });
        //“我的租借”点击进入一个页面
        Button btnbutton2 = view.findViewById(R.id.btn_button2);
        btnbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(),
                        SecondActivity.class
                );
                startActivity(intent);
            }
        });
        //“设置”点击进入一个页面
        Button btnbutton3 = view.findViewById(R.id.btn_button3);
        btnbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(),
                        ThirdActivity.class
                );
                startActivity(intent);
            }
        });
        //“帮助与反馈”点击进入一个页面
        Button btnbutton4 = view.findViewById(R.id.btn_button4);
        btnbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到文件操作Activity
                Intent intent = new Intent(
                        getContext(),
                        FiveActivity.class
                );
                startActivity(intent);
            }
        });
        return view;
    }
    private void pickImage(){
        ActivityCompat.requestPermissions(getActivity(),
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
            Glide.with(getActivity()).load(uri).into(ivHead);

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
            ContentResolver contentResolver = getActivity().getContentResolver();
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
