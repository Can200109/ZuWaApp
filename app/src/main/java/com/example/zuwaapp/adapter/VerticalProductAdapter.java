package com.example.zuwaapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zuwaapp.Constant;
import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Product;
import com.example.zuwaapp.method.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class VerticalProductAdapter extends RecyclerView.Adapter<VerticalProductAdapter.ViewHolder1> {
    private List<Product> productList;
    public VerticalProductAdapter(List<Product> productList) {
        this.productList = productList;
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_products_item,parent,false);
        ViewHolder1 viewHolder = new ViewHolder1(view);
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
    public void onBindViewHolder(@NonNull ViewHolder1 viewHolder1, int position) {
        Product product = productList.get(position);
        Gson gson = new GsonBuilder().create();
        List<String> photos = gson.fromJson(product.getProductPhoto(),new TypeToken<List<String>>(){}.getType());
        Glide.with(viewHolder1.itemView)
                .load(Constant.PRODUCT_PHOTO+product.getPhoneNumber()+"/"+product.getProductId()+"/" +photos.get(0))
                .dontAnimate()
                .into(viewHolder1.productImage);
        viewHolder1.productTitle.setText(product.getProductName());
        viewHolder1.productPrice.setText("￥"+product.getProductPrice()+"");
        viewHolder1.productCount.setText("30天起租");

        viewHolder1.itemView.setTag(position);
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }




    static class ViewHolder1 extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productPrice;
        TextView productCount;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productPic1);
            productTitle = itemView.findViewById(R.id.productTitle1);
            productPrice = itemView.findViewById(R.id.productPrice1);
            productCount = itemView.findViewById(R.id.productCount1);

        }
    }
}