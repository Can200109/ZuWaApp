package com.example.zuwaapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.Gaussian.CardAdapterHelper;
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.activity.MyRentGlaceActivity;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.entity.Rent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ToastUtils;

/**
 * 这是一段重复代码，只是改了个list的实体类。
 * **/
public class RentCardAdapter extends RecyclerView.Adapter<RentCardAdapter.ViewHolder> {
    private List<Rent> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public RentCardAdapter(List<Rent> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());

        //获取图片
        Rent rent = mList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(rent.getRentPhoto(),new TypeToken<List<String>>(){}.getType());
        Glide.with(holder.itemView)
                .load(Constant.PRODUCT_PHOTO +rent.getOwnerPhoneNumber()+"/"+rent.getProductId()+"/"+photos.get(0))
                .centerCrop()
                .into(holder.mImageView);

        //获取描述
        holder.words.setText(mList.get(position).getRentName());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MyRentGlaceActivity.class);
                Bundle bundle = new Bundle();
                //产品发布人，产品。
                bundle.putString("ownerPhone",mList.get(position).getOwnerPhoneNumber());
                bundle.putString("productId",mList.get(position).getProductId());
                bundle.putString("rentId",mList.get(position).getRentId());
                intent.putExtra("bundle",bundle);
                view.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public TextView words;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            words = (TextView) itemView.findViewById(R.id.words);
        }

    }

}
