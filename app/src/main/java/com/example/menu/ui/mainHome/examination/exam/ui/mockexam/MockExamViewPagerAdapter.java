package com.example.menu.ui.mainHome.examination.exam.ui.mockexam;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class MockExamViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> list;

    public MockExamViewPagerAdapter(ArrayList<View> list) {
        this.list = list;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) 	{
        container.removeView(list.get(position));
    }


    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0==arg1;
    }
}
