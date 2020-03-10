package com.example.menu.ui.mainHome.examination.activity;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;


class SequentialAdapter extends androidx.viewpager.widget.PagerAdapter {
    private ArrayList<View> viewpagelist;
    SequentialAdapter(ArrayList<View> list){
        this.viewpagelist = list;
    }
    @Override
    public int getCount() {
        return viewpagelist.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewpagelist.get(position));
    }
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewpagelist.get(position));
        return viewpagelist.get(position);
    }
}
