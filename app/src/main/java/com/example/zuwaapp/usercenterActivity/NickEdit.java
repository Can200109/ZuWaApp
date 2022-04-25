package com.example.zuwaapp.usercenterActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.zuwaapp.Constant.FIND_USER_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.MODIFY;
import static com.example.zuwaapp.Constant.USER_URL;
/**
 * 2022/4/24
 * 这是昵称修改界面
 * **/
public class NickEdit extends AppCompatActivity {
    private User user;
    private EditText userNick,userPassword;
    private Button button,nickReturn;

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
                        user = findUserByPhoneNumberResult.getData();
                        //Toast.makeText(getApplicationContext(),"查找成功",Toast.LENGTH_LONG).show();
                        userPassword.setText(user.getUserPassword());
                        userNick.setText(user.getUserName());
                    }
                    break;
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
        setContentView(R.layout.activity_nick_edit);

        userNick = findViewById(R.id.zhen_edit_user_name);
        userPassword = findViewById(R.id.zhen_edit_user_password);
        button = findViewById(R.id.update_edit_nick);
        nickReturn = findViewById(R.id.user_nickSetting_return);
        //先传入修改前的数据
        (new Method()).findUserByPhoneNumber(Constant.PHONENUMBER,handler);

        //点击按钮修改昵称和密码
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = userNick.getText().toString();
                String password = userPassword.getText().toString();

                User user1 = new User(nick, password, Constant.PHONENUMBER,user.getUserPhoto());
                (new Method()).editUser(user1,handler);
                Toast.makeText(NickEdit.this,"修改成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NickEdit.this, UserSetting.class);
                startActivity(intent);
            }
        });

        nickReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}