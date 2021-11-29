package com.example.zuwaapp.activity;

import static com.example.zuwaapp.Constant.MODIFY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.TextView;

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

public class TransferActivity extends AppCompatActivity {
    private ImageButton photoButton;
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
        userId.setText("12345678910");
//        userId.setText(Constant.PHONENUMBER);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //完善信息
               String user_name = userName.getText().toString();
               String user_password = userPassword.getText().toString();
//               User user = new User(user_name,user_password,Constant.PHONENUMBER);
               User user = new User(user_name,user_password,"12345678910");
               Log.e("昵称",user_name);
               Log.e("密码",user_password);
               (new Method()).editUser(user,handler);
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

}