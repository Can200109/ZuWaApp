package com.example.zuwaapp.buttonActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuwaapp.R;
import com.example.zuwaapp.adapter.MyListViewAdapter;
import com.example.zuwaapp.entity.Title;
import com.example.zuwaapp.fragment.MultiplexingFragment;

import java.util.ArrayList;
import java.util.List;

public class VerticalSortActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private List<Title> mList = new ArrayList<>();  //左侧列表list
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager supportFragmentManager = getSupportFragmentManager();
    private MyListViewAdapter apader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_sort);
        Intent intent = getIntent();
        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
    }

    private void initData() {
        //左边listView集合添加数据，适配器适配
        listViewData();
        //添加fragment,复用fragment
        addFragment();
        //默认选中ListView第一条item
        replese(0);
        //ListView第一条item的Select为true
        mList.get(0).setSelect(true);
        //listView点击事件
        mListView.setOnItemClickListener(this);
    }


    // 需要添加的几种农场管理操作
    private void listViewData() {
        //标题list
        mList.add(new Title("节日礼服"));
        mList.add(new Title("电脑设备"));
        mList.add(new Title("低碳办公"));
        mList.add(new Title("唯美相机"));
        mList.add(new Title("精品手机"));
        mList.add(new Title("体感vr"));
        mList.add(new Title("聚会用品"));
        mList.add(new Title("运动健身"));
        mList.add(new Title("摄影航拍"));
        mList.add(new Title("旅游户外"));
        mList.add(new Title("耳机乐器"));
        mList.add(new Title("手表饰品"));
        mList.add(new Title("企业办公"));
        mList.add(new Title("演出会展"));


        //适配器适配
        apader = new MyListViewAdapter(mList, this);
        mListView.setAdapter(apader);
    }


    private void addFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        for (int i = 0; i < mList.size(); i++) {
            //传过去标题和内容  内容暂时为空 包括缩略图和路径
            Fragment multiplexing = multiplexingFragment.getMultiplexing(mList.get(i).getName(), "");
            mFragmentList.add(multiplexing);
        }
        //添加fragment
        for (int i = 0; i < mFragmentList.size(); i++) {
            transaction.add(R.id.mFrame, mFragmentList.get(i));
        }
        transaction.commit();
    }

    private void replese(int position) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        //所有的fragment隐藏，position对应的fragment显示，提交。
        for (int i = 0; i < mFragmentList.size(); i++) {
            Fragment fragment = mFragmentList.get(i);
            transaction.hide(fragment);
        }
        transaction.show(mFragmentList.get(position)).commit();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //切换fragment
        replese(position);
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        mList.get(position).setSelect(true);
        apader.notifyDataSetChanged();
        Toast.makeText(VerticalSortActivity.this, "" + position, Toast.LENGTH_SHORT).show();
    }
}
