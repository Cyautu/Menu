package com.example.menu.ui.mainHome.mine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menu.DB.DBUrl;
import com.example.menu.R;
import com.example.menu.method.mine.MD5Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity{
    private EditText userName_input;//定义接收输入框账号的值
    private EditText userPassword_input;//定义接收输入框密码的值
    private EditText userPassword_input_again;//定义再次输入的密码
    private EditText questionOne_input,questionTwo_input,questionThree_input;//定义三个安全问题输入的值
    private String questionTitle1,questionTitle2,questionTitle3;
    private Spinner spinner1,spinner2,spinner3;//问题一、二、三控件
    private ArrayAdapter<CharSequence> adapter1;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);

        Button register=(Button)findViewById(R.id.register_button);
        register.setOnClickListener(this.RegisterButtonListener);
        //获取前端输入的账号和密码以及再次输入的密码
        userName_input=(EditText)findViewById(R.id.register_userNameInput);
        userPassword_input=(EditText)findViewById(R.id.register_userPasswordInput);
        userPassword_input_again=(EditText)findViewById(R.id.register_userPasswordInput_again);
        questionOne_input=(EditText)findViewById(R.id.set_security_input1);
        questionTwo_input=(EditText)findViewById(R.id.set_security_input2);
        questionThree_input=(EditText)findViewById(R.id.set_security_input3);

        //声明并返回按钮并调用判断当前页面的开闭情况方法，并进一步作出返回事件的反应
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
        //注册安全问题下拉菜单监听
        spinner1=(Spinner)findViewById(R.id.choose_question1);
        spinner2=(Spinner)findViewById(R.id.choose_question2);
        spinner3=(Spinner)findViewById(R.id.choose_question3);
        //安全问题1下拉菜单监听
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   questionTitle1=spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //安全问题2下拉菜单监听
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionTitle2=spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //安全问题3下拉菜单监听
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionTitle3=spinner1.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //将安全问题列表导入
        adapter1=ArrayAdapter.createFromResource(this,R.array.question1_list,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);//绑定适配器类
        spinner2.setAdapter(adapter1);
        spinner3.setAdapter(adapter1);
    }
    //注册按钮点击事件
    View.OnClickListener RegisterButtonListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new Thread(){//新建一个进程来执行注册方法
                @SuppressLint("WrongConstant")
                public void run(){
                    //获取前端传来的值
                    String userName=userName_input.getText().toString();
                    String userPassword=userPassword_input.getText().toString();
                    String userPasswordAgain=userPassword_input_again.getText().toString();
                    String answerOne = questionOne_input.getText().toString();
                    String answerTwo = questionTwo_input.getText().toString();
                    String answerThree =questionThree_input.getText().toString();
                    String md5UserPassword = MD5Utils.md5(userPassword);
                    //注册输入判断
                    Looper.prepare();
                    if (userName.length()==0){
                        Toast.makeText(RegisterActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (userPassword.length()==0){
                            Toast.makeText(RegisterActivity.this,"请设置密码", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (userPasswordAgain.length()==0){
                                Toast.makeText(RegisterActivity.this,"未确认密码", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (!userPassword.equals(userPasswordAgain)){
                                            Toast.makeText(RegisterActivity.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
                                        }
                                else{
                                    if ((answerOne.length()==0)|(answerTwo.length()==0)|(answerThree.length()==0)|
                                            (questionTitle1.equals("请选择问题"))|(questionTitle2.equals("请选择问题"))|(questionTitle1.equals("请选择问题"))){
                                                Toast.makeText(RegisterActivity.this,"安全问题设置有误", Toast.LENGTH_SHORT).show();
                                            }
                                    else {
                                        if (userPassword.length()<7) {
                                            Toast.makeText(RegisterActivity.this,"密码至少8位", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            if (!TextUtils.isEmpty(DBUrl.getLoginInfo(userName))) {
                                                Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                                            } else {
                                                try {
                                                    registerUser(userName, md5UserPassword, questionTitle1, answerOne, questionTitle2, answerTwo, questionTitle3, answerThree);
                                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent();
                                                    intent.setClass(RegisterActivity.this, LoginActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                                                    startActivity(intent);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Looper.loop();
                }
                 /*if(View){
                    //你要运行的方法
                    isoncl=false; //点击一次后就改成false，这样就实现只点击一次了
                }*/
            }.start();
        }
    };
    //返回
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //将新用户存入数据库
    public void registerUser(String user_Name,String user_Password,String question1,String answer1,String question2,String answer2,String question3
                             ,String answer3){//注册方法
        String sql="INSERT INTO Users(user_Name,user_Sex,user_Password,user_Icon,user_NickName,user_RegisterDate,user_Introduction,Security1,Answer1" +
                ",Security2,Answer2,Security3,Answer3) VALUES(?,'保密',?,'default','默认的昵称',getdate(),'暂未设置个人简介',?,?,?,?,?,?)";
        PreparedStatement preparedStatement;
        Connection connection=null;
               connection =DBUrl.getSQLConnection();
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,user_Name);
            preparedStatement.setString(2,user_Password);
            preparedStatement.setString(3,question1);
            preparedStatement.setString(4,answer1);
            preparedStatement.setString(5,question2);
            preparedStatement.setString(6,answer2);
            preparedStatement.setString(7,question3);
            preparedStatement.setString(8,answer3);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
