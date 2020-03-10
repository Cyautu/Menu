package com.example.menu.ui.mainHome.mine.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Users;
import java.util.ArrayList;

public class PersonalActivity extends AppCompatActivity {
    @SuppressLint("SdCardPath")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private String path = URLBase.getURL(2);//URLBase.getURL(2)获取本地头像缓存地址
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ImageView user_Icon = (ImageView)findViewById(R.id.user_Photo);
        final TextView user_NickName = (TextView) findViewById(R.id.nick_name);
        final TextView user_Introduction = (TextView) findViewById(R.id.personal_introduction);
        @SuppressLint("CutPasteId")
        final TextView fans_number = findViewById(R.id.fans_label);
        final TextView follows_number= findViewById(R.id.follow_label);
        //从SharedPreferences获取userID,userNickName等信息
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        final String userID = sharedPreferences.getString("userID",null);
        String userIcon = sharedPreferences.getString("userIcon",null);
        /*显示头像，判断是否为默认头像*/
            Bitmap bt = BitmapFactory.decodeFile(path +userID+ "userIcon.jpg");// 从SD卡中找头像，转换成Bitmap
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                user_Icon.setImageDrawable(drawable);
            }

        //获取性别，简介，昵称并显示出来
        new Thread(new Runnable() {
            String[] u = new String[3];//0存储性别,1存储简介
            @SuppressLint("SetTextI18n")
            public void run() {
                ArrayList<Users> is=new DBUrl().getOrdinaryInformation(userID);
                ArrayList<Users> fans = new DBUrl().getFans(userID);
                ArrayList<Users> follows = new DBUrl().getFollows(userID);
                /*System.out.println("我的粉丝数量为"+fans.size());
                  System.out.println("我的关注数量为"+follows.size());*/
                for(Users i:is){
                    u[2]=i.getUserNikeName();
                    u[1]=i.getUserIntroduction();
                    u[0]=i.getUserSex();
                }
            runOnUiThread(new Runnable() {
                public void run() {
                    user_NickName.setText(u[2]);
                    user_Introduction.setText(u[1]);
                    fans_number.setText(fans.size()+" 粉丝");//显示粉丝数量
                    follows_number.setText(follows.size()+" 关注");
                       Sex(u[0]);//调用Sex方法判断用户性别，并显示对应的icon
            }
            });
        }
        }).start();
        //编辑资料按钮事件
        Button edit_information = (Button)findViewById(R.id.edit_information_button);
        edit_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonalActivity.this, PersonalEditActivity.class);
                /*PersonalActivity.this.finish();销毁之后则不能返回*/
                startActivity(intent);
            }
        });
        //注销登录
        Button loginOut = (Button)findViewById(R.id.login_out_button);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清楚登录缓存退出到登录界面
                SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();//清除本地用户登录信息
                editor.apply();
                Intent out=new Intent(PersonalActivity.this, LoginActivity.class);
                startActivity(out);
                Toast.makeText(PersonalActivity.this, "注销成功！", Toast.LENGTH_SHORT).show();
                PersonalActivity.this.finish();
            }
        });

        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
        //粉丝
        fans_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f=new Intent(PersonalActivity.this, MyFansActivity.class);
                startActivity(f);
            }
        });
        //关注,
        follows_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f=new Intent(PersonalActivity.this, FollowActivity.class);
                startActivity(f);
            }
        });
    }
    //判断性别显示相应的icon
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Sex(String sex){
        final ImageView sex_icon = (ImageView)findViewById(R.id.sex_icon);
        if (sex.equals("男")){
            sex_icon.setBackground(this.getResources().getDrawable(R.drawable.sex_man));
        }
        else if (sex.equals("女")){
            sex_icon.setBackground(this.getResources().getDrawable(R.drawable.sex_girl));
        }
        else {
            sex_icon.setBackground(this.getResources().getDrawable(R.drawable.sex_noshow));
        }
    }
    @Override
    //返回页面方法，返回上一页
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
