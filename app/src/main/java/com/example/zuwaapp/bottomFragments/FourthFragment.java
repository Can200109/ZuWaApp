package com.example.zuwaapp.bottomFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuwaapp.R;
import com.example.zuwaapp.Service.ServiceActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FourthFragment extends Fragment {
    private TextView mCall,btn1,btn2,btn3,btn4,btn5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.xiaoxi,
                container,
                false);

        mCall =(TextView) view.findViewById(R.id.btn_call);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"18233770378"));
                startActivity(intent);
            }
        });
        btn1 =(TextView) view.findViewById(R.id.btn_pro1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btn2 =(TextView) view.findViewById(R.id.btn_pro2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btn3 =(TextView) view.findViewById(R.id.btn_pro3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btn4 =(TextView) view.findViewById(R.id.btn_pro4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });
        btn5 =(TextView) view.findViewById(R.id.btn_pro5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
