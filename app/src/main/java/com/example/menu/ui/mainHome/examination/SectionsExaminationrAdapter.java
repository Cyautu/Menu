package com.example.menu.ui.mainHome.examination;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class SectionsExaminationrAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    SectionsExaminationrAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return  list.get(position);
    }

    @Override
    public int getCount() {
        return  list.size();
    }
}