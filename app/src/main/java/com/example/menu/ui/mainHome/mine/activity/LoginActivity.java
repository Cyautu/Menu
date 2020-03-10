package com.example.menu.ui.mainHome.mine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menu.DB.DBUrl;
import com.example.menu.MainActivity;
import com.example.menu.R;
import com.example.menu.method.mine.MD5Utils;

public class LoginActivity extends AppCompatActivity {
    EditText userName,password;
    Button login,register;
    TextView forget;
    private String user_Name,user_Password,md5User_Password;//定义接收用户输入的值以及加密后的字符串
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.userNameInput);
        password = findViewById(R.id.userPasswordInput);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        forget = findViewById(R.id.forget_password);
        //登录验证
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        user_Name = userName.getText().toString().trim();
                        user_Password = password.getText().toString().trim();
                        md5User_Password = MD5Utils.md5(user_Password);
                        Looper.prepare();
                        if (TextUtils.isEmpty(user_Name) ) {
                            Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                        }else if(TextUtils.isEmpty(user_Password)){
                            Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                        }else if(DBUrl.getLoginInfo(user_Name).equals("")){
                            Toast.makeText(LoginActivity.this, "此用户不存在！", Toast.LENGTH_SHORT).show();
                            password.setText("");
                        }else if(!md5User_Password.equals(DBUrl.getLoginInfo(user_Name).trim())) {
                            Toast.makeText(LoginActivity.this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                            password.setText("");
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            saveLoginInfo();
                            goToMineFragment();
                        }
                        Looper.loop();
                    }
                });thread.start();
            }
        });
        //忘记密码
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RetrievePasswordActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
        //注册按钮
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,RegisterActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                    startActivity(intent);
            }
        });
        //返回按钮
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
    }

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
    //登录成功跳转首页
    public void goToMineFragment(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        LoginActivity.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//禁止返回到这个页面
        startActivity(intent);
    }
   /* //读取sp
    private void readsp(){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        userName.setText(sp.getString("userName",null));
        password.setText(sp.getString("password",null));
    }*/
    //存放登录信息
    private void saveLoginInfo(){
        DBUrl db = new DBUrl();
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String[] result = new String[3];
        result=db.getLoginInformation(user_Name);
        editor.putString("userName", user_Name);
        editor.putString("password", user_Password);
        editor.putString("userID",result[0]);
        editor.putString("userIcon",result[1]);
        editor.putString("userNickName",result[2]);
        editor.apply();
    }
    //安卓重写返回键事件,指定只能返回首页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            /*LoginActivity.this.finish();//关闭当前即可返回前页*/
            Intent home=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(home);
        }
        return true;
    }
}
