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
    private List<Product> RentProduce = new ArrayList<>();
    private RentAdapter rentAdapter;
    private ImageButton button;
    private Product product;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
//    private Handler handler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            switch (msg.what) {
//                case FIND_COLLECT_BY_PHONENUMBER:
//                    Result<List<Collect>> findCollect = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Collect>>>(){}.getType());
//                    List<Collect> data = findCollect.getData();
//                    Log.e("数据",""+data);
//                    for(Collect collect:data){
//                        (new Method()).findProductById(collect.getProductId(),handler);
//                        Log.e("prouctID",collect.getCollectId());
//                    }
//                    break;
//                case FIND_PRODUCT_BY_ID:
//                    Result<Product> findProductById = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
//                    if(findProductById.getCode() == 200) {
//                       product = findProductById.getData();
//                       RentProduce.add(product);
//                       rentAdapter.notifyDataSetChanged();
//                    }
//                    break;
//            }
//        }
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.shoucang,
               container,
               false);
//       button = view.findViewById(R.id.btn_search_shoucang);
//       button.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               String context = "";
//               Intent intent = new Intent(getActivity(), SearchActivity.class);
//               Bundle bundle = new Bundle();
//               bundle.putString("context",context);
//               Log.e("内容",context);
//               intent.putExtra("bundle",bundle);
//               startActivity(intent);
//           }
//       });
//
//       RentProduce.clear();
////       (new Method()).findAllProduct(handler);
////        (new Method()).findCollectByPhoneNumber("12345678910",handler);
//        (new Method()).findCollectByPhoneNumber(Constant.PHONENUMBER,handler);
//        //调用方法查找数据（传入phone）
//        //按电话号码查找所有productName，然后通过product返回商品list
//
//       ListView RentProductList = view.findViewById(R.id.RentProductList);
//       rentAdapter = new RentAdapter(RentProduce,R.layout.rent_product_layout,getContext());
//       RentProductList.setAdapter(rentAdapter);
//       RentProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           @Override
//           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//               Intent intent = new Intent();
//               intent.setClass(getContext(), GlaceActivity.class);
//               Bundle bundle = new Bundle();
//               bundle.putString("phone",RentProduce.get(position).getPhoneNumber());
//               bundle.putString("id",RentProduce.get(position).getProductId());
//               intent.putExtra("bundle",bundle);
//               startActivity(intent);
//           }
//       });


        //一个长导航栏
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

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
                    return "猜你喜欢";
                case 1:
                    return "电脑";
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
