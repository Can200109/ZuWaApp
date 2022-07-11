package com.example.zuwaapp.adapter;

import android.content.Context;
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
import com.example.zuwaapp.entity.Rent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * 这个adapter类用于  我的租借   页面的展示所用，因 我的租借 后台实现方式为...
 * 算了，太烂了，也懒得改了(可以去看那个rent实体类，就知道实现方式有多烂)
 * 反正没用微服务，所以无法调用Product类的那个list
 * 所以就复制了上次写的adapter，略改，略该
 * **/
public class NewRentAdapter extends BaseAdapter {
    private List<Rent> rentList = new ArrayList<>();
    private int layoutResId;
    private Context context;

    public NewRentAdapter(List<Rent> rentList, int layoutResId, Context context) {
        this.rentList = rentList;
        this.layoutResId = layoutResId;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(rentList != null){
            return rentList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(rentList != null){
            return rentList.get(position);
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
        ImageView RentPhoto = view.findViewById(R.id.rent_picture);
        TextView RentPrice = view.findViewById(R.id.rent_price);
        TextView RentTitle = view.findViewById(R.id.rent_name);

        Rent rent = rentList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(rent.getRentPhoto(),new TypeToken<List<String>>(){}.getType());
        Glide.with(view)
                .load(Constant.PRODUCT_PHOTO +rent.getOwnerPhoneNumber()+"/"+rent.getProductId()+"/"+photos.get(0))
                .centerCrop()
                .into(RentPhoto);

        RentTitle.setText(rentList.get(position).getRentName());
        RentPrice.setText("￥"+rentList.get(position).getRentPrice());

        return view;
    }
}
