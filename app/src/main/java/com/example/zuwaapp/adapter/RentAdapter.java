package com.example.zuwaapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RentAdapter extends BaseAdapter {
    private List<Product> productList = new ArrayList<>();
    private int layoutResId;
    private Context context;

    public RentAdapter(List<Product> productList, int layoutResId, Context context) {
        this.productList = productList;
        this.layoutResId = layoutResId;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(productList != null){
            return productList.get(position);
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(layoutResId,null,false);
        }
        ImageView RentPhoto = view.findViewById(R.id.iv_RentPhoto);
        TextView RentPrice = view.findViewById(R.id.tv_Rent_price);
        TextView RentTitle = view.findViewById(R.id.tv_Rent_title);

        Product product = productList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
        Log.e("onBindViewHolder: ", photos.get(0));
        Glide.with(view)
                .load(Constant.PRODUCT_PHOTO +product.getPhoneNumber()+"/"+product.getProductId()+"/"+photos.get(0))
                .centerCrop()
                .into(RentPhoto);

        RentTitle.setText(productList.get(position).getProductName());
        RentPrice.setText("￥"+productList.get(position).getProductPrice());

        return view;
    }

}

