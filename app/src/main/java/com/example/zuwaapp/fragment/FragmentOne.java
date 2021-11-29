package com.example.zuwaapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.adapter.ProductAdapter;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Result;
import com.example.zuwaapp.method.Method;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.example.zuwaapp.Constant.FIND_ALL;

public class FragmentOne extends Fragment {
    private ImageView imageView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    private Handler handler  = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FIND_ALL:
                    Result<List<Product>> findResult = gson.fromJson(msg.obj.toString(), new TypeToken<Result<List<Product>>>(){}.getType());
                    if (findResult.getCode() == 200) {
                        List<Product> data = findResult.getData();
//                        Toast.makeText(getContext(),"查找成功",Toast.LENGTH_LONG).show();
                        for(Product product:data){
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(),"查找失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            productList.clear();
            View view = inflater.inflate(R.layout.fragment_one, container, false);
            View view1 = inflater.inflate(R.layout.products_item,container,false);
            imageView = view1.findViewById(R.id.productPic);
            new Method().findAllProduct(handler);
            RecyclerView Rview = view.findViewById(R.id.list1);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            Rview.setLayoutManager(layoutManager);
            productAdapter = new ProductAdapter(productList);
            Rview.setAdapter(productAdapter);
            productAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent();

                    //传什么过去
                    //发布者（’用户‘电话）  图片   标题   描述    价格    押金   次数
                    intent.setClass(getActivity(), GlaceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone",productList.get(position).getPhoneNumber());
                    bundle.putString("id",productList.get(position).getProductId());
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
            });
            return view;
        }
}