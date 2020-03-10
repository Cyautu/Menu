package com.example.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.bumptech.glide.Glide;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.example.menu.service.userIcon.DownloadUserIconSerice;
import com.example.menu.ui.loadingFlash.LoadProgressDialog;
import com.example.menu.ui.mainHome.mine.activity.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    Toolbar toolbar;
    int queryMsg = 0;
    String Fans_ID,Followed_ID,userName,Query;
    String [] user_NameArr,user_IntroductionArr,user_PhotoArr,user_NickArr;
    ArrayList<Bitmap> user_Icon = new ArrayList<>();//存放用户头像
    ArrayList<String> btStates = new ArrayList<>();//用于判断是否已经关注过了
    private static String url = URLBase.getURL(1);//下载头像的地址
    LoadProgressDialog loadProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        final SearchView searchView  = (SearchView) MenuItemCompat.getActionView(item);
        searchView.onActionViewExpanded();//展开搜索框
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("输入关键字搜索");
        //设置SearchView的 EditTxt
        SearchView.SearchAutoComplete st = searchView.findViewById(R.id.search_src_text);
        st.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        st.setTextColor(getResources().getColor(android.R.color.background_dark));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) { //搜索提交
                Query = query;
                searchView.clearFocus();
                //加载动画开始
                loadProgressDialog = new LoadProgressDialog(SearchActivity.this,"搜索中…");
                loadProgressDialog.show();
                ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
                if (info == null){
                    Toast.makeText(getApplicationContext(),"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
                    loadProgressDialog.dismiss();
                }else {
                    new QueryInfo().start();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {//文本改变
                user_Icon.clear();
                //预留
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(v -> {
            SearchActivity.this.finish();
        });
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                if (userName.length() != 0) {
                    List<Map<String,Object>> list = new ArrayList<>();
                    for (int i = 0; i < user_NameArr.length; i++){
                        Map <String,Object> map = new HashMap<>();
                        map.put("user_Name",user_NameArr[i]);
                        map.put("user_Introduction",user_IntroductionArr[i]);
                        map.put("user_Photo",user_Icon.get(i));//将bitmap形式的图片给到list里面
                        map.put("user_NickName",user_NickArr[i]);
                        list.add(map);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(SearchActivity.this, list, R.layout.item_search,
                            new String[]{"user_Name","user_Introduction","user_Photo","user_NickName"},
                            new int[]{R.id.user_name, R.id.personal_introduction, R.id.user_icon,R.id.user_nickName})
                    {
                        public View getView(final int position, View convertView, ViewGroup parent){
                            ViewHolder viewHolder;
                            if (convertView == null){
                                viewHolder= new ViewHolder();
                                convertView = View.inflate(SearchActivity.this, R.layout.item_search, null);
                                viewHolder.follow = convertView.findViewById(R.id.follow);
                                viewHolder.user_name = convertView.findViewById(R.id.user_name);
                                convertView.setTag(viewHolder);
                            }else {
                                viewHolder = (ViewHolder) convertView.getTag();
                            }
                            viewHolder.follow.setOnClickListener(v -> {
                                viewHolder.follow.setText("已关注");
                                viewHolder.follow.setClickable(false);
                                viewHolder.follow.setTextColor(Color.GRAY);
                                Thread thread = new Thread(() -> {
                                    Looper.prepare();
                                    SharedPreferences sharedPreferences = SearchActivity.this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                    Fans_ID = sharedPreferences.getString("userID", null);
                                    String un = viewHolder.user_name.getText().toString();
                                    Followed_ID = DBUrl.getId(un);
                                    if (Fans_ID != null) {
                                        if (!Fans_ID.equals(Followed_ID)) {
                                            if (!DBUrl.getFollowState(Fans_ID,Followed_ID)){
                                                Toast.makeText(SearchActivity.this, "已添加关注！", Toast.LENGTH_SHORT).show();
                                                DBUrl.followUsers(Fans_ID, Followed_ID);
                                            }
                                            else {
                                                Toast.makeText(SearchActivity.this, "不能重复关注！", Toast.LENGTH_SHORT).show();
                                                /*viewHolder.follow.setVisibility(View.GONE);*/
                                            }
                                        } else {
                                            Toast.makeText(SearchActivity.this, "你不能关注自己！", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Intent intent = new Intent();
                                        intent.setClass(SearchActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    Looper.loop();
                                });
                                thread.start();
                            });
                            switch (btStates.get(position)){
                                case "0":
                                    viewHolder.follow.setVisibility(View.GONE);
                                    break;
                                case "1":
                                    viewHolder.follow.setText("已关注");
                                    viewHolder.follow.setClickable(false);
                                    viewHolder.follow.setTextColor(Color.GRAY);
                                    break;
                                case "2":
                                    break;
                            }
                            listView.setOnItemClickListener((parent1, view, position1, id) ->
                                    Toast.makeText(SearchActivity.this, "预留跳转到个人界面", Toast.LENGTH_SHORT).show());
                            return super.getView(position,convertView,parent);
                        }
                        class ViewHolder{
                            Button follow;
                            TextView user_name;
                        }
                    };
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {//重写Image使得适配器可以显示图片
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                Drawable drawable = new BitmapDrawable((Bitmap)data);
                                iv.setImageDrawable(drawable);
                                return true;
                            }
                            return false;
                        }
                    });
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(SearchActivity.this, "没有找到此用户！", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(null);
                }
                //加载动画结束
                loadProgressDialog.dismiss();
            }
        }
    };
    public class QueryInfo extends Thread{
        @Override
        public void run() {
            String unId,un;
            userName = DBUrl.search(Query)[0];
            user_NameArr = userName.split("丨");
            user_PhotoArr = DBUrl.search(Query)[1].split("丨");
            user_IntroductionArr = DBUrl.search(Query)[2].split("丨");
            user_NickArr = DBUrl.search(Query)[3].split("丨");
            SharedPreferences sharedPreferences = SearchActivity.this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            unId = sharedPreferences.getString("userID", null);
            un = sharedPreferences.getString("userName", null);
            for (int i = 0; i < user_NameArr.length; i++) {
                boolean states = DBUrl.getFollowState(unId,DBUrl.getId(user_NameArr[i]));
                if (user_NameArr[i].equals(un)){ //自己
                    btStates.add(i,"0");
                }else if(states){    //已关注
                    btStates.add(i,"1");
                }else {    //未关注
                    btStates.add(i,"2");
                }
                if (user_PhotoArr[i].equals("default")) {
                    user_Icon.add( DownloadUserIconSerice.getImage(url + "default_userIcon.jpg"));
                    /*map.put("user_Photo",user_Icon[i]);*/
                } else {
                    user_Icon.add(DownloadUserIconSerice.getImage(url+ DBUrl.getId(user_NameArr[i]) + "userIcon.jpg"));
                    /*map.put("user_Photo",user_Icon[i]);*/
                }
            }
            Message msg = new Message();
            msg.what = queryMsg;
            handler.sendMessage(msg);
        }
    }
    /*@Override
    //安卓重写返回键事件,返回到前页不能再返回到原来的页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            SearchActivity.this.finish();//关闭当前即可返回前页
        }
        return true;
    }*/
}

