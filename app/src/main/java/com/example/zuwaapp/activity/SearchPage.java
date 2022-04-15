package com.example.zuwaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zuwaapp.R;
import com.example.zuwaapp.ngss.SearchView;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.bCallBack;


public class SearchPage extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 2. 绑定视图
        setContentView(R.layout.activity_searchpage);

        Intent intent = getIntent();


        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view0);

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                System.out.println("我收到了" + string);
                Log.e("内容",string);
                Intent intent = new Intent(SearchPage.this,SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("context",string);
                Log.e("内容",string);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });



    }
}
