package com.example.menu.ui.mainHome.mine.retrievePassword.inputSecurity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.example.menu.DB.DBUrl;
import com.example.menu.fieldEncapsulation.Users;
import com.example.menu.R;
import com.example.menu.ui.mainHome.mine.retrievePassword.inputPassword.InputPasswordFragment;

import java.util.ArrayList;

public class InputSecurityFragment extends Fragment {
    private InputSecurityViewModel inputSecurity;
    private String userName;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inputSecurity = ViewModelProviders.of(this).get(InputSecurityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_retrieve_security, container, false);

        final TextView questionOneTitle=(TextView)root.findViewById(R.id.questionTitle1);
        final TextView questionTwoTitle=(TextView)root.findViewById(R.id.questionTitle2);
        final TextView questionThreeTitle=(TextView)root.findViewById(R.id.questionTitle3);
        final EditText questionAnswer1=(EditText)root.findViewById(R.id.questionAnswer1);
        final EditText questionAnswer2=(EditText)root.findViewById(R.id.questionAnswer2);
        final EditText questionAnswer3=(EditText)root.findViewById(R.id.questionAnswer3);
        //获取从上一个fragment传递过来的用户名,通过查询密保问题并显示到界面上
        new Thread(){
            public void run(){
                userName= getArguments().getString("key");
                ArrayList<Users> qs=new DBUrl().getSafeQuestion(userName);
                for(Users q:qs){
                    questionOneTitle.setText(q.getSecurity1());
                    questionTwoTitle.setText(q.getSecurity2());
                    questionThreeTitle.setText(q.getSecurity3());
                }
            }
        }.start();

        //验证完用户后点击事件
        final Button bt=(Button) root.findViewById(R.id.next_step_two);
        bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        FragmentManager fm = getFragmentManager();
                        String answer1 = questionAnswer1.getText().toString();
                        String answer2 = questionAnswer2.getText().toString();
                        String answer3 = questionAnswer3.getText().toString();
                        TextView result_safe_text=(TextView)root.findViewById(R.id.safe_verification_result);
                        DBUrl db=new DBUrl();
                        if ((answer1.length()==0)||(answer2.length()==0)||answer3.length()==0){
                            result_safe_text.setText("请回答完问题");
                            result_safe_text.setTextColor(0xFFC81522);
                        }
                        else{
                            if (db.getSafeVerification(userName, answer1, answer2, answer3)){
                                //将用户名传递给密码重置界面
                                InputPasswordFragment passwordFragment = new InputPasswordFragment();
                                Bundle bundle_userName=new Bundle();
                                bundle_userName.putString("user",userName);
                                passwordFragment.setArguments(bundle_userName);
                                fm.beginTransaction()
                                        //将当前页面替换为密码重设界面
                                        .replace(R.id.nav_retrieve_fragment,passwordFragment)
                                        .commit();
                            }
                            else {
                                result_safe_text.setText("密保错误");
                                result_safe_text.setTextColor(0xFFC81522);
                            }
                        }
                    }
                }.start();
            }
        });
        return root;
    }
}