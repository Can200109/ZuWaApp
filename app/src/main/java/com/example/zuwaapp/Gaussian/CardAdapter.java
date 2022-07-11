package com.example.zuwaapp.Gaussian;

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
import com.example.zuwaapp.R;
import com.example.zuwaapp.activity.GlaceActivity;
import com.example.zuwaapp.entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ToastUtils;

/**
 * Created by jameson on 8/30/16.
 * update by X_heng on 4/24/22
 * 英语太难了，写汉语吧。。。
 * 就是说，更改adapter的点击事件和注入的实体类，达到想要的效果
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Product> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public CardAdapter(List<Product> mList) {
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
        Product product = mList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
        Log.e("onBindViewHolder: ", photos.get(0));
        Glide.with(holder.itemView)
                .load(Constant.PRODUCT_PHOTO +product.getPhoneNumber()+"/"+product.getProductId()+"/"+photos.get(0))
                .centerCrop()
                .into(holder.mImageView);

        //获取描述
        holder.words.setText(mList.get(position).getProductName());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtils.show(holder.mImageView.getContext(), "" + position);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), GlaceActivity.class);
                Bundle bundle = new Bundle();
                //
                bundle.putString("phone",mList.get(position).getPhoneNumber());
                bundle.putString("id",mList.get(position).getProductId());
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
