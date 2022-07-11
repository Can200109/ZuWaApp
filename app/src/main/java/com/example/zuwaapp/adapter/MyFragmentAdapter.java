package com.example.zuwaapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;
    public MyFragmentAdapter(List<Fragment> fragments, FragmentActivity fragmentActivity){
        super(fragmentActivity);
        this.fragments = fragments;

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
