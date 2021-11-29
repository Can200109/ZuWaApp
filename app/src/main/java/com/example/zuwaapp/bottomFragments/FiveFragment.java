package com.example.zuwaapp.bottomFragments;

import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.MODIFY;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.FirstActivity;
import com.example.zuwaapp.activity.FiveActivity;
import com.example.zuwaapp.activity.SecondActivity;
import com.example.zuwaapp.activity.ThirdActivity;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.entity.User;
import com.example.zuwaapp.method.Method;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FiveFragment extends Fragment {
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
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userId.setText(findUserByPhoneNumberResult.getData().getPhoneNumber());
                        userName.setText(findUserByPhoneNumberResult.getData().getUserName());
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
        //“我的发布”点击进入一个页面
        Button btnbutton1 = view.findViewById(R.id.btn_button1);


        //点开时显示自己的信息
//        userId.setText(Constant.PHONENUMBER);
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

//        (new Method()).findUserByPhoneNumber("12345678910",handler);

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
}
