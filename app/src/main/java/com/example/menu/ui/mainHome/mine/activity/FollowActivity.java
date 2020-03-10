package com.example.menu.ui.mainHome.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.menu.R;
import com.example.menu.SearchActivity;
import com.example.menu.ui.mainHome.mine.follow.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        findViewById(R.id.back).setOnClickListener(v -> FollowActivity.this.finish());
        findViewById(R.id.search).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(FollowActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}