package com.example.zuwaapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zuwaapp.R;
import com.example.zuwaapp.entity.Title;

import java.util.List;

/**
 * 重写了listview的adapter类
 * 网上找的，用着还可以
 *
 * **/
public class MyListViewAdapter extends BaseAdapter {

    private List<Title> mlist;
    private Context context;

    public MyListViewAdapter(List<Title> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            holder.mListViewText = convertView.findViewById(R.id.mListViewText);
            holder.mYueDouXianShi = convertView.findViewById(R.id.mYueDouXianShi);
            holder.mYueDouLin = convertView.findViewById(R.id.mYueDouLin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = mlist.get(position).getName();
        holder.mListViewText.setText(name);


        //true和false用来解决复用问题的，如果当前标记为选中状态那么item字体颜色变红，红色竖条显示，item背景色变白
        if (mlist.get(position).isSelect()) {
            holder.mListViewText.setTextColor(Color.RED);     //字体
            holder.mYueDouXianShi.setVisibility(View.GONE);    //红色竖条没必要显示
            holder.mYueDouLin.setBackgroundColor(Color.WHITE);  //背景
        }
        //如果不是选中状态，item字体颜色变黑，红色竖条隐藏，item背景还变灰
        else {
            holder.mListViewText.setTextColor(Color.BLACK);
            holder.mYueDouXianShi.setVisibility(View.GONE);
            holder.mYueDouLin.setBackgroundColor(Color.parseColor("#EBEBEB"));    //parseColor("#EBEBEB")
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView mListViewText;
        public TextView mYueDouXianShi;
        public LinearLayout mYueDouLin;
    }
}
