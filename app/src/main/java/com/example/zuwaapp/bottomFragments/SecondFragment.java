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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.ImageLookActivity;
import com.example.zuwaapp.activity.SearchActivity;
import com.example.zuwaapp.adapter.RentAdapter;
import com.example.zuwaapp.entity.Collect;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.util.GlideLoadImage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.yds.library.MultiImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment {
    private List<Product> RentProduce = new ArrayList<>();
    private RentAdapter rentAdapter;
    private Product product;
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_COLLECT_BY_PHONENUMBER:
                    Result<List<Collect>> findCollect = gson.fromJson(msg.obj.toString(),new TypeToken<Result<List<Collect>>>(){}.getType());
                    List<Collect> data = findCollect.getData();
                    Log.e("数据",""+data);
                    for(Collect collect:data){
                        (new Method()).findProductById(collect.getProductId(),handler);
                        Log.e("prouctID",collect.getCollectId());
                    }
                    break;
                case FIND_PRODUCT_BY_ID:
                    Result<Product> findProductById = gson.fromJson(msg.obj.toString(),new TypeToken<Result<Product>>(){}.getType());
                    if(findProductById.getCode() == 200) {
                       product = findProductById.getData();
                       RentProduce.add(product);
                       rentAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    private static SecondFragment scf;
    public static SecondFragment getShouCangFragment(){
        if(scf == null){
            scf = new SecondFragment();
        }
        return scf;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.shoucang,
               container,
               false);

       RentProduce.clear();
//       (new Method()).findAllProduct(handler);
        (new Method()).findCollectByPhoneNumber("12345678910",handler);
        //调用方法查找数据（传入phone）
        //按电话号码查找所有productName，然后通过product返回商品list

       ListView RentProductList = view.findViewById(R.id.RentProductList);
       rentAdapter = new RentAdapter(RentProduce,R.layout.rent_product_layout,getContext());
       RentProductList.setAdapter(rentAdapter);
       RentProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               Intent intent = new Intent();
               intent.setClass(getContext(), GlaceActivity.class);
               Bundle bundle = new Bundle();
               bundle.putString("phone",RentProduce.get(position).getPhoneNumber());
               bundle.putString("id",RentProduce.get(position).getProductId());
               intent.putExtra("bundle",bundle);
               startActivity(intent);
           }
       });
       return view;
    }

}
