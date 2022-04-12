package com.example.zuwaapp;


import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.zuwaapp.adapter.MyFragmentAdapter;
import com.example.zuwaapp.bottomFragments.FirstFragment;
import com.example.zuwaapp.bottomFragments.FiveFragment;
import com.example.zuwaapp.bottomFragments.FourthFragment;
import com.example.zuwaapp.bottomFragments.SecondFragment;
import com.example.zuwaapp.bottomFragments.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
//    private RadioGroup radioGroup;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        bottomNavigationView = findViewById(R.id.nav);
        bottomNavigationView.setOnNavigationItemSelectedListener( item ->{
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()){
                case R.id.nav_menu0:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView,FirstFragment.class,null)
                            .setReorderingAllowed(true)
                            .commit();
//                Log.d(TAG,"");
                    return true;
                case R.id.nav_menu1:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView,SecondFragment.class,null)
                            .setReorderingAllowed(true)
                            .commit();
//                Log.d(TAG,"");
                    return true;
                case R.id.nav_menu2:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView,ThirdFragment.class,null)
                            .setReorderingAllowed(true)
                            .commit();
//                Log.d(TAG,"");
                    return true;
                case R.id.nav_menu3:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView,FourthFragment.class,null)
                            .setReorderingAllowed(true)
                            .commit();
//                Log.d(TAG,"");
                    return true;
                case R.id.nav_menu4:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView,FiveFragment.class,null)
                            .setReorderingAllowed(true)
                            .commit();
//                Log.d(TAG,"");
                    return true;
                default:
                    return false;
            }
        } );

    }

}