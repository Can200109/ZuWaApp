package com.example.zuwaapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (int) view.getTag());
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product product = productList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
        Log.e("onBindViewHolder: ", photos.get(0));
        Glide.with(viewHolder.itemView).load(Constant.PRODUCT_PHOTO+product.getPhoneNumber()+"/"+product.getProductId()+"/" +photos.get(0)).into(viewHolder.productImage);
        viewHolder.productTitle.setText(product.getProductName());
        viewHolder.productPrice.setText("￥"+product.getProductPrice()+"");
        viewHolder.productCount.setText("已租次数"+product.getProductCount());

        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productPrice;
        TextView productCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productPic);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCount = itemView.findViewById(R.id.productCount);


        }
    }
}
