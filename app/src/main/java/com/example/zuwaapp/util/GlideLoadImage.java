package com.example.zuwaapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yds.library.IMultiImageLoader;

public class GlideLoadImage implements IMultiImageLoader {

    @Override
    public void load(Context context, Object url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}