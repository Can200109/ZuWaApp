package com.example.zuwaapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import com.example.zuwaapp.bottomFragments.FirstFragment;
import com.example.zuwaapp.bottomFragments.FiveFragment;
import com.example.zuwaapp.bottomFragments.FourthFragment;
import com.example.zuwaapp.bottomFragments.SecondFragment;
import com.example.zuwaapp.bottomFragments.ThirdFragment;

import java.util.HashMap;
import java.util.Map;


public class ResultActivity extends AppCompatActivity {
    private Map<String, ImageView> iconMap=new HashMap<>();
    private Map<String, TextView> titleMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        FragmentTabHost fragmentTabHost=findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),
                android.R.id.tabcontent);


        TabHost.TabSpec tabSpec1=fragmentTabHost.newTabSpec("tab1").setIndicator(getTabSpecView("tab1","首页",R.mipmap.home));
        TabHost.TabSpec tabSpec2=fragmentTabHost.newTabSpec("tab2").setIndicator(getTabSpecView("tab2","分类",R.mipmap.sort));
        TabHost.TabSpec tabSpec3=fragmentTabHost.newTabSpec("tab3").setIndicator(getTabSpecView("tab3","添加",R.mipmap.add));
        TabHost.TabSpec tabSpec4=fragmentTabHost.newTabSpec("tab4").setIndicator(getTabSpecView("tab4","客服",R.mipmap.service));
        TabHost.TabSpec tabSpec5=fragmentTabHost.newTabSpec("tab5").setIndicator(getTabSpecView("tab5","我的",R.mipmap.me));

        fragmentTabHost.addTab(tabSpec1, FirstFragment.class,null);
        fragmentTabHost.addTab(tabSpec2, SecondFragment.class,null);
        fragmentTabHost.addTab(tabSpec3, ThirdFragment.class,null);
        fragmentTabHost.addTab(tabSpec4, FourthFragment.class,null);
        fragmentTabHost.addTab(tabSpec5, FiveFragment.class,null);


        //设置默认选中的标签
        fragmentTabHost.setCurrentTab(0);
        iconMap.get("tab1").setBackgroundResource(R.mipmap.home_out);
        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.purple));

        //标签切换事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            //切换标签时执行
            public void onTabChanged(String tabId) {
                //将选中的标签修改为绿色，其他的标签修改为黑色
                switch (tabId){
                    case "tab1"://选中消息标签
                        iconMap.get("tab1").setBackgroundResource(R.mipmap.home_out);
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.purple));
                        iconMap.get("tab2").setBackgroundResource(R.mipmap.sort);
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab3").setBackgroundResource(R.mipmap.add);
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab4").setBackgroundResource(R.mipmap.service);
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab5").setBackgroundResource(R.mipmap.me);
                        titleMap.get("tab5").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab2"://选中联系人标签
                        iconMap.get("tab1").setBackgroundResource(R.mipmap.home);
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab2").setBackgroundResource(R.mipmap.sort_out);
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.purple));
                        iconMap.get("tab3").setBackgroundResource(R.mipmap.add);
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab4").setBackgroundResource(R.mipmap.service);
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab5").setBackgroundResource(R.mipmap.me);
                        titleMap.get("tab5").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab3"://选中我的标签
                        iconMap.get("tab1").setBackgroundResource(R.mipmap.home);
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab2").setBackgroundResource(R.mipmap.sort);
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab3").setBackgroundResource(R.mipmap.add_out);
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.purple));
                        iconMap.get("tab4").setBackgroundResource(R.mipmap.service);
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab5").setBackgroundResource(R.mipmap.me);
                        titleMap.get("tab5").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab4"://选中我的标签
                        iconMap.get("tab1").setBackgroundResource(R.mipmap.home);
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab2").setBackgroundResource(R.mipmap.sort);
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab3").setBackgroundResource(R.mipmap.add);
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab4").setBackgroundResource(R.mipmap.service_out);
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.purple));
                        iconMap.get("tab5").setBackgroundResource(R.mipmap.me);
                        titleMap.get("tab5").setTextColor(getResources().getColor(R.color.black));
                        break;
                    case "tab5"://选中我的标签
                        iconMap.get("tab1").setBackgroundResource(R.mipmap.home);
                        titleMap.get("tab1").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab2").setBackgroundResource(R.mipmap.sort);
                        titleMap.get("tab2").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab3").setBackgroundResource(R.mipmap.add);
                        titleMap.get("tab3").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab4").setBackgroundResource(R.mipmap.service);
                        titleMap.get("tab4").setTextColor(getResources().getColor(R.color.black));
                        iconMap.get("tab5").setBackgroundResource(R.mipmap.me_out);
                        titleMap.get("tab5").setTextColor(getResources().getColor(R.color.purple));
                        break;
                }
            }
        });



    }

    public View getTabSpecView(String tag, String title, int drawable){
//        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.tab_spec_layout,null);
        //获取到标签布局中ImageView和TextView的引用
        ImageView icon = view.findViewById(R.id.iv_icon);
        TextView tvTitle = view.findViewById(R.id.tv_title);

        iconMap.put(tag,icon);
        titleMap.put(tag,tvTitle);

        //设置控件显示的内容
        tvTitle.setText(title);
        icon.setBackgroundResource(drawable);//相当于XML布局文件中的src属性

        return view;
    }

}