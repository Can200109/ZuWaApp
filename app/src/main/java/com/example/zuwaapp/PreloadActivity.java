package com.example.zuwaapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.zuwaapp.entity.UserState;
import com.example.zuwaapp.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class PreloadActivity extends Activity implements View.OnTouchListener{
    private SQLiteDatabase db;
    private MyDatabaseHelper dbHelper;
    private List<UserState>  users = new ArrayList<>();

    private Handler handler;
    private static final long DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化本地数据库
        initDatabaseByDBHelper();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.openpage);
        setContentView(imageView);

        imageView.setOnTouchListener(this);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queryData();

                if(users.isEmpty()){
                    startActivity(new Intent(PreloadActivity.this,VerifyActivity.class));
                    finish();
                }else {
                    UserState user = users.get(0);

                    Log.e("user的状态",user.getUserMode()+"");
                    Log.e("user的电话号",user.getUserPhoneById()+"");
                    Intent intent = new Intent(PreloadActivity.this,ResultActivity.class);
                    Constant.setPHONENUMBER(user.getUserPhoneById());
                    startActivity(intent);
                    finish();
                }

            }
        }, DELAY);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void initDatabaseByDBHelper() {
        //创建数据库管理类的对象
        dbHelper = new MyDatabaseHelper(
                this,
                "user_state_table",
                null,
                1
        );
        //获取数据库对象
        db = dbHelper.getWritableDatabase();
    }

    private void queryData() {
        //获取数据库对象（已有)
        //执行查询
        Cursor cursor = db.query("user_state_table", null, null, null, null, null, null);
        //遍历查询到的数据
        while(cursor.moveToNext()){
            //获取当前记录的数据
            String userPhone = cursor.getString(cursor.getColumnIndex("userPhone"));
            String userState = cursor.getString(cursor.getColumnIndex("userState"));
            UserState user = new UserState(userState,userPhone);
            users.add(user);

        }
    }

}
