package com.example.menu.ui.mainHome.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.menu.DB.URLBase;
import com.example.menu.MainActivity;
import com.example.menu.ui.mainHome.mine.activity.FollowActivity;
import com.example.menu.ui.mainHome.mine.activity.LoginActivity;
import com.example.menu.ui.mainHome.mine.activity.PersonalActivity;
import com.example.menu.R;
import com.example.menu.service.userIcon.DownloadUserIconSerice;

import java.util.Objects;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;
    private String userName=null;
    private String userIcon=null;
    private String userNickName=null;
    private String userID=null;
    private Bitmap  icon=null;//服务器下载的头像
    @SuppressLint("SdCardPath")
    private static String path = URLBase.getURL(2);//URLBase.getURL(2)获取本地头像缓存地址
    private static String url = URLBase.getURL(1);//下载头像的地址
    private  LinearLayout my_concern;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mineViewModel =
                ViewModelProviders.of(this).get(MineViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_mine, container, false);
        //找到显示用户名、头像的地方
        final TextView login_tips_userName = (TextView)root.findViewById(R.id.login_tips_userName);
        final ImageView user_Icon = (ImageView)root.findViewById(R.id.user_headPhoto);
        //将用户名、头像显示出来
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID",null);
        userName = sharedPreferences.getString("userName",null);
        userIcon = sharedPreferences.getString("userIcon",null);
        userNickName = sharedPreferences.getString(("userNickName"),null);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (info == null){
            Toast.makeText(getContext(),"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
        }else
        //判断是否已经登录,没有登录显示中铁logo，并提示登录，有登录则显示用户的头像以及昵称
            if (userName!=null){
                login_tips_userName.setText(userNickName);
                /*显示头像，判断是否为默认头像*/
                if (userIcon.equals("default")){
                    new Thread(new Runnable() {
                        public void run() {
                            icon = DownloadUserIconSerice.getImage(url + "default_userIcon.jpg");
                            DownloadUserIconSerice.saveMyBitmap(icon,userID+"userIcon");
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //显示头像
                                    Bitmap bt1 = BitmapFactory.decodeFile(path +userID+ "userIcon.jpg");// 从SD卡中找头像，转换成Bitmap
                                    @SuppressWarnings("deprecation")
                                    Drawable drawable1 = new BitmapDrawable(bt1);// 转换成drawable
                                    user_Icon.setImageDrawable(drawable1);
                                }
                            });
                        }

                    }).start();
                }
                else{
                    Bitmap bt = BitmapFactory.decodeFile(path +userID+ "userIcon.jpg");// 从SD卡中找头像，转换成Bitmap
                    if (bt != null) {
                        @SuppressWarnings("deprecation")
                        Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                        user_Icon.setImageDrawable(drawable);
                    }
                    else {//从服务器获取头像
                        new Thread(new Runnable() {
                            public void run() {
                                icon = DownloadUserIconSerice.getImage(url+userID+"userIcon.jpg");
                                DownloadUserIconSerice.saveMyBitmap(icon,userID+"userIcon");
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        //显示头像
                                        Bitmap bt1 = BitmapFactory.decodeFile(path +userID+ "userIcon.jpg");// 从SD卡中找头像，转换成Bitmap
                                        @SuppressWarnings("deprecation")
                                        Drawable drawable1 = new BitmapDrawable(bt1);// 转换成drawable
                                        user_Icon.setImageDrawable(drawable1);
                                    }
                                });
                            }

                        }).start();
                    }
                }
                //用户头像点击事件
                user_Icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(){
                            public void run(){
                                Intent m=new Intent(getActivity(), PersonalActivity.class);
                                startActivity(m);
                            }
                        }.start();
                    }
                });
            }
            else {
                login_tips_userName.setText("点击头像登录");
                user_Icon.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_default_head_img));
                user_Icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(){
                            public void run(){
                                Intent m=new Intent(getActivity(), LoginActivity.class);
                                startActivity(m);
                            }
                        }.start();
                    }
                });
            }
            //我的关注页面
            my_concern = (LinearLayout) root.findViewById(R.id.my_concern);
            my_concern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userName!=null){
                        Intent concern=new Intent(getActivity(), FollowActivity.class);
                        startActivity(concern);
                    }else {
                        Toast.makeText(getActivity(),"请先登录!",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        return root;
    }

}