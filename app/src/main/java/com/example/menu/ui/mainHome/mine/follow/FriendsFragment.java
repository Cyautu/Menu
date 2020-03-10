package com.example.menu.ui.mainHome.mine.follow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.example.menu.ui.loadingFlash.LoadProgressDialog;
import com.example.menu.ui.mainHome.mine.activity.FollowActivity;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Users;
import com.example.menu.service.userIcon.DownloadUserIconSerice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FriendsFragment extends Fragment {
    int queryMsg = 0;
    private PageViewModel pageViewModel;
    private ListView listView;
    private LinearLayout nothingFollowF,followFriendV;
    String userID;//登录状态中获取用户ID
    ArrayList<Bitmap> user_Icon = new ArrayList<>();//存放用户头像
    ArrayList<Users> follows = new ArrayList<>();//被关注人信息
    ArrayList<String> userName = new ArrayList<>();//存储用户名
    private static String url = URLBase.getURL(1);//下载头像的地址
    LoadProgressDialog loadProgressDialog;
    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_follow_friends, container, false);
        listView = root.findViewById(R.id.list_friend);
        nothingFollowF = root.findViewById(R.id.noting_follow_friend);//没有关注图片log区域
        followFriendV = root.findViewById(R.id.follow_friend_view);//关注显示区域
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID",null);
        loadProgressDialog = new LoadProgressDialog(getActivity(),"正在加载列表…");
        loadProgressDialog.show();
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (info == null){
            Toast.makeText(getActivity(),"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
        }
        //获取关注的人的信息
        new Thread() {
            @Override
            public void run() {
                DBUrl dbUrl = new DBUrl();
                follows = dbUrl.getFollows(userID);
                int j = 0;
                for (Users follow : follows) {
                    if (follow.getUserPhoto().equals("default")) {
                        user_Icon.add(DownloadUserIconSerice.getImage(url + "default_userIcon.jpg"));
                        /*System.out.println("我关注的人的头像" + user_Icon.get(j));*/
                        j++;
                    } else {
                        user_Icon.add(DownloadUserIconSerice.getImage(url +
                                DBUrl.getId(follow.getUserName()) + "userIcon.jpg"));
                        /*System.out.println("图片" + user_Icon.get(j));
                        System.out.println("图片" + follow.getUserName());*/
                        j++;
                    }
                    userName.add(follow.getUserName());
                }
                Message msg = new Message();
                msg.what = queryMsg;
                handler.sendMessage(msg);
            }
        }.start();

        return root;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                if (follows.size()!=0) {
                    nothingFollowF.setVisibility(View.GONE);//隐藏空空如也图片
                    //在list中显示出来
                    int i = 0;//设置用户头像读取起点
                    List<Map<String, Object>> list = new ArrayList<>();
                    for (Users f : follows) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("user_Icon", user_Icon.get(i));
                        i++;
                        map.put("user_Introduction", f.getUserIntroduction());
                        map.put("user_NickName", f.getUserNikeName());
                        list.add(map);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.item_follow,
                            new String[]{"user_Introduction", "user_Icon", "user_NickName"},
                            new int[]{R.id.user_introduction, R.id.user_icon, R.id.user_nickName}) {
                        @SuppressLint("ViewHolder")
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            ViewHolder viewHolder;
                            if (convertView == null) {
                                viewHolder = new ViewHolder();
                                convertView = View.inflate(getActivity(), R.layout.item_follow, null);
                                viewHolder.button = convertView.findViewById(R.id.follow);
                                viewHolder.user_name = convertView.findViewById(R.id.user_name);
                                convertView.setTag(viewHolder);
                            } else {
                                viewHolder = (ViewHolder) convertView.getTag();
                            }

                            //list长按事件
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                                    final AlertDialog favDialog = builder.create();
                                    View dialogView = View.inflate(getActivity(), R.layout.dialog_follow, null);
                                    TextView sendMsg = dialogView.findViewById(R.id.option1);
                                    TextView addRemark = dialogView.findViewById(R.id.option2);
                                    TextView unfollow = dialogView.findViewById(R.id.option3);
                                    sendMsg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v1) {
                                            Toast.makeText(getActivity(), "预留跳转到发送信息界面", Toast.LENGTH_SHORT).show();
                                            favDialog.dismiss();
                                        }
                                    });
                                    addRemark.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v12) {
                                            Toast.makeText(getActivity(), "预留添加备注", Toast.LENGTH_SHORT).show();
                                            favDialog.dismiss();
                                        }
                                    });
                                    unfollow.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog bd = new AlertDialog.Builder(getActivity())
                                                    .setMessage("确定不再关注此用户")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Looper.prepare();
                                                                    /*未做*/
                                                                    System.out.println(userName.get(position));
                                                                    DBUrl.unFollowUser(userID, DBUrl.getId(userName.get(position)));
                                                                    Toast.makeText(getActivity(), "已取消关注", Toast.LENGTH_SHORT).show();
                                                                    getActivity().finish();
                                                                    startActivity(new Intent(getActivity(), FollowActivity.class));
                                                                    Looper.loop();
                                                                }
                                                            }).start();
                                                        }
                                                    })
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    })
                                                    .create();
                                            bd.show();
                                            favDialog.dismiss();
                                        }
                                    });
                                    favDialog.setView(dialogView);
                                    favDialog.show();
                                    return true;
                                }
                            });
                            return super.getView(position, convertView, parent);
                        }

                        class ViewHolder {
                            TextView user_name;
                            Button button;
                        }
                    };
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {//重写Image使得适配器可以显示图片
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView iv = (ImageView) view;
                                Drawable drawable = new BitmapDrawable((Bitmap) data);
                                iv.setImageDrawable(drawable);
                                return true;
                            }
                            return false;
                        }
                    });
                    listView.setAdapter(adapter);
                    //list点击事件
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Toast.makeText(getActivity(), "预留跳转到个人界面", Toast.LENGTH_SHORT).show();
                    });
                }else {
                    followFriendV.setVisibility(View.GONE);//隐藏listView
                    nothingFollowF.setVisibility(View.VISIBLE);
                }
                loadProgressDialog.dismiss();
            }
        }
    };
}