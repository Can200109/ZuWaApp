package com.example.zuwaapp.bottomFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.ResultActivity;
import com.example.zuwaapp.activity.SearchActivity;
import com.example.zuwaapp.activity.SearchPage;
import com.example.zuwaapp.fragment.FragmentFive;
import com.example.zuwaapp.fragment.FragmentFour;
import com.example.zuwaapp.fragment.FragmentOne;
import com.example.zuwaapp.fragment.FragmentSix;
import com.example.zuwaapp.fragment.FragmentThree;
import com.example.zuwaapp.fragment.FragmentTwo;
import com.google.android.material.tabs.TabLayout;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class FirstFragment extends Fragment {
    private final int REQUEST_CODE=1;
    private TextView btnSearch,btnCode;
    private EditText edtSearch;


    private ViewPager mViewPager;
    private List<ImageView> images;
    //下方指示点
    private int currentItem;
    //记录上一次位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{R.drawable.lunbo1,R.drawable.lunbo2,R.drawable.lunbo3};
    private ViewPagerAdapter adapter;
    //定时调度机制
    private ScheduledExecutorService scheduledExecutorService;



    private static FirstFragment syf;

    public static FirstFragment getShouYeFragment(){
        if(syf == null){
            syf = new FirstFragment();
        }
        return syf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
        }
        View view = inflater.inflate(R.layout.shouye,container,false);

        initviews(view);
        ZXingLibrary.initDisplayOpinion(this.getContext());
        //搜索框的点击事件
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchPage.class);
                startActivity(intent);

            }
        });
        //二维码的点击事件
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("信息","我是这个按钮");
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);


            }
        });
        //轮播图图片获取
        getImage();
        //设置轮播图
        setView(view);

        return view;
    }




    //轮播图图片获取
    private void getImage() {

    }



    //UI界面的更新
    private void setView(View view) {
        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0;i<imageIds.length;i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点

        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
    //定义的适配器
    private class ViewPagerAdapter extends PagerAdapter {
        //返回当前有效视图的个数
        @Override
        public int getCount() {
            return images.size();
        }
        //判断instantiateItem函数所返回的key与一个页面视图是否代表同一个
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(images.get(position));
        }
        //创建指定位置的页面视图，也就是将一张图片放到容器中的指定位置

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

    }
    //利用线程池定时执行动画轮播
    public void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(),2,2, TimeUnit.SECONDS );
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem+1) % imageIds.length;
            mHandler.sendEmptyMessage(0);//发送轮播消息


        }
    }
    //接受子线程传递来的数据
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    mViewPager.setCurrentItem(currentItem);
                    break;
            }
        }
    };
    //当切换到其他界面时，关闭后台轮博
    public void onStop() {
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE: {
                //处理扫描结果
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    Intent intent = new Intent();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        intent.setClass(getActivity(), ResultActivity.class);
                        intent.putExtra("result",result);
                        startActivity(intent);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        intent.setClass(getActivity(),ResultActivity.class);
                        intent.putExtra("result","二维码解析失败");
                        startActivity(intent);
                    }
                }
            }
            break;


        }
    }




    //设置按钮监听器
    private void initviews(View view) {
        mViewPager = view.findViewById(R.id.viewpager);
        btnSearch = view.findViewById(R.id.btn_search);
        btnCode = view.findViewById(R.id.btn_code);
//        edtSearch = view.findViewById(R.id.edt_search);
    }
}