package com.example.menu.ui.mainHome.mine.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Users;
import com.example.menu.service.userIcon.DownloadUserIconSerice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFansActivity extends AppCompatActivity {
    int queryMsg = 0;
    String userID;//登录状态中获取用户ID
    ListView fansListView;
    LinearLayout notingfans,fansView;
    ArrayList<Users> fans = new ArrayList<>();//粉丝信息
    ArrayList<Bitmap> fans_Icon = new ArrayList<>();//粉丝头像
    private static String url = URLBase.getURL(1);//下载头像的地址
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);

        fansListView = findViewById(R.id.fans_listView);
        notingfans = findViewById(R.id.noting_fans_view);
        fansView = findViewById(R.id.fans_view);
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID",null);
        new Thread(){
            @Override
            public void run() {
                DBUrl dbUrl = new DBUrl();
                fans=dbUrl.getFans(userID);
                for (Users fan:fans) {
                    if (fan.getUserPhoto().equals("default")) {
                        fans_Icon.add( DownloadUserIconSerice.getImage(url + "default_userIcon.jpg"));
                       /* System.out.println("图片"+fans_Icon.get(j));*/
                    } else {
                        fans_Icon.add(DownloadUserIconSerice.getImage(url + DBUrl.getId(fan.getUserName()) + "userIcon.jpg"));
                        /*System.out.println("图片"+fans_Icon.get(j));
                        System.out.println("图片"+fan.getUserName());*/
                    }
                }
                Message msg = new Message();
                msg.what = queryMsg;
                handler.sendMessage(msg);
            }
        }.start();
        //返回按钮
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                if (fans.size()!=0) {
                    notingfans.setVisibility(View.GONE);
                    List<Map<String,Object>> list = new ArrayList<>();
                    int i=0;//设置用户头像读取起点
                    for (Users f:fans){
                        Map <String,Object> map = new HashMap<>();
                        map.put("fans_Icon",fans_Icon.get(i));
                        i++;
                        map.put("fans_Introduction",f.getUserIntroduction());
                        map.put("fans_NickName",f.getUserNikeName());
                        list.add(map);
                    }
                        SimpleAdapter adapter = new SimpleAdapter(MyFansActivity.this, list, R.layout.item_fans,
                                new String[]{"fans_Introduction","fans_Icon","fans_NickName"},
                                new int[]{R.id.fans_introduction, R.id.fans_icon,R.id.fans_nickName})
                        {
                            public View getView(final int position, View convertView, ViewGroup parent){
                                fansListView.setOnItemClickListener((parent1, view, position1, id) ->
                                        Toast.makeText(MyFansActivity.this, "预留跳转到个人界面", Toast.LENGTH_SHORT).show());
                                return super.getView(position,convertView,parent);
                            }
                        };
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {//重写Image使得适配器可以显示图片
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                iv.setImageBitmap((Bitmap) data);
                                return true;
                            }
                            return false;
                        }
                    });
                        fansListView.setAdapter(adapter);
                    }
                else {
                    fansView.setVisibility(View.GONE);
                    notingfans.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    @Override
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
