package com.example.zuwaapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by x_heng.
 * <p>
 * Date: 2022/5/3
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //当数据库第一次被创建的时候自动回调onCreate()方法
    //通常在该方法中写建表操作或数据库相关的初始化操作的逻辑代码
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表
        String createTable = "create table user_state_table(id Integer Primary key autoincrement, userState varchar(4), userPhone varchar(32))";
        db.execSQL(createTable);
        Log.e("lww", "数据库表创建成功");
    }

    //当数据库版本更新的时候被自动回调
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
