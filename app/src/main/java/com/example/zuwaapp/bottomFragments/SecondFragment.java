package com.example.zuwaapp.bottomFragments;

import static com.example.zuwaapp.Constant.FIND_ALL;
import static com.example.zuwaapp.Constant.FIND_COLLECT;
import static com.example.zuwaapp.Constant.FIND_COLLECT_BY_PHONENUMBER;
import static com.example.zuwaapp.Constant.FIND_PRODUCT_BY_ID;
import static com.example.zuwaapp.Constant.PRODUCT_PHOTO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.ImageLookActivity;
import com.example.zuwaapp.activity.SearchActivity;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.buttonActivity.VerticalSortActivity;
import com.example.zuwaapp.entity.Collect;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.fragment.FragmentFive;
import com.example.zuwaapp.fragment.FragmentFour;
import com.example.zuwaapp.fragment.FragmentOne;
import com.example.zuwaapp.fragment.FragmentSix;
import com.example.zuwaapp.fragment.FragmentThree;
import com.example.zuwaapp.fragment.FragmentTwo;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.util.GlideLoadImage;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.yds.library.MultiImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {
    private Button toVerticalSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort,
                container,
                false);

        toVerticalSort = view.findViewById(R.id.btn_vertical_sort);
        //一个长导航栏
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        toVerticalSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VerticalSortActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentOne();
                case 1:
                    return new FragmentTwo();
                case 2:
                    return new FragmentThree();
                case 3:
                    return new FragmentFour();
                case 4:
                    return new FragmentFive();
                case 5:
                default:
                    return new FragmentSix();
            }
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "节日礼服";
                case 1:
                    return "电脑设备";
                case 2:
                    return "旅游户外";
                case 3:
                    return "低碳办公";
                case 4:
                    return "摄影航拍";
                case 5:
                default:
                    return "穿戴饰品";
            }
        }
    }


}

