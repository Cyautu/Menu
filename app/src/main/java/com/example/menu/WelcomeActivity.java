package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private int reclen = 5;
    private TextView skipText;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   getSupportActionBar().hide();
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        initView();
        timer.schedule(task,1000,1000);
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                //从闪屏界面跳转到首页
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    reclen--;
                    skipText.setText("跳过 " + reclen);
                    skipText.setTextColor(0xFF4169E1);
                    if (reclen < 0) {
                        timer.cancel();
                        skipText.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }
    };
    private void initView() {
        skipText = findViewById(R.id.skip_text);//跳过
        skipText.setOnClickListener(this);//跳过监听
    }
    /**
     * 点击跳过
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_text:
                //从闪屏界面跳转到首界面
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }
}
