package com.example.menu.ui.mainHome.mine.retrievePassword.inputPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.menu.DB.DBUrl;
import com.example.menu.ui.mainHome.mine.activity.LoginActivity;
import com.example.menu.method.mine.MD5Utils;
import com.example.menu.R;


public class InputPasswordFragment extends Fragment {

    private InputPasswordViewModel passwordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        passwordViewModel =
                ViewModelProviders.of(this).get(InputPasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_retrieve_password, container, false);
        final EditText password = (EditText)root.findViewById(R.id.set_new_password_input1);
        final EditText password_again = (EditText)root.findViewById(R.id.set_new_password_input2);
        final TextView set_result=(TextView)root.findViewById(R.id.set_password_result);
        //设置好密码提交按钮事件
        Button bt=(Button) root.findViewById(R.id.next_step_three);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = getArguments().getString("user");
                final String pass = password.getText().toString();
                final String pass_again = password_again.getText().toString();
                final String md5UserPassword = MD5Utils.md5(pass);
                new Thread(){
                    public void run(){
                        DBUrl db=new DBUrl();
                        if (pass.length()==0){
                            set_result.setText("请输入密码");
                            set_result.setTextColor(0xFFC81522);
                        }
                        else {
                            if (pass_again.length()==0){
                                set_result.setText("请确认密码");
                                set_result.setTextColor(0xFFC81522);
                            }
                            else{
                                if (!pass.equals(pass_again)){
                                    set_result.setText("两次密码不正确");
                                    set_result.setTextColor(0xFFC81522);
                                }
                                else{
                                    db.setNewPassword(userName,md5UserPassword);
                                    set_result.setText("找回成功，跳转登录");
                                    set_result.setTextColor(0xFF157BF8);
                                    Intent i=new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
                                }
                            }
                        }
                    }
                }.start();
            }
        });
        return root;
    }
}