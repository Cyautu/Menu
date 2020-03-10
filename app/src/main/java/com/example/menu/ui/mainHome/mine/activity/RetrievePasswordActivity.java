package com.example.menu.ui.mainHome.mine.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.menu.R;

public class RetrievePasswordActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        /* 添加返回按钮*/
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
    }
    //当前页面的开闭情况方法
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
