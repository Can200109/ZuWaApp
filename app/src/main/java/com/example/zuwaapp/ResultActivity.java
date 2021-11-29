package com.example.zuwaapp;


import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zuwaapp.bottomFragments.FirstFragment;
import com.example.zuwaapp.bottomFragments.FiveFragment;
import com.example.zuwaapp.bottomFragments.FourthFragment;
import com.example.zuwaapp.bottomFragments.SecondFragment;
import com.example.zuwaapp.bottomFragments.ThirdFragment;

public class ResultActivity extends AppCompatActivity {
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        radioGroup = findViewById(R.id.rg_main);
        //底部菜单
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<radioGroup.getChildCount();i++){
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if(rb.isChecked()){
                        setIndexSelectedTwo(i);
                        break;
                    }

                }
            }
        });
        changeFragment(new FirstFragment().getShouYeFragment());
    }
    private void changeFragment(Fragment fragment){
        //开启事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();

    }

    private void setIndexSelectedTwo(int index) {
        switch (index){
            case 0:
                changeFragment(new FirstFragment().getShouYeFragment());
                break;
            case 1:
                changeFragment(new SecondFragment().getShouCangFragment());
                break;
            case 2:
                changeFragment(new ThirdFragment().getPlusFragment());
                break;
            case 3:
                changeFragment(new FourthFragment().getMseeageFragemnt());
                break;
            case 4:
                changeFragment(new FiveFragment().getWoDeFragment());
                break;
        }
    }
}