package com.example.menu.ui.mainHome.mine.retrievePassword.inputUser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.menu.DB.DBUrl;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.example.menu.R;
import com.example.menu.ui.mainHome.mine.retrievePassword.inputSecurity.InputSecurityFragment;

public class InputUserFragment extends Fragment {
    private InputUserViewModel inputUser;
    private InputSecurityFragment securityFragment;
    private EditText userNameInput;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {

        inputUser =
                ViewModelProviders.of(this).get(InputUserViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_retrieve_user, container, false);

        userNameInput=(EditText)root.findViewById(R.id.checkingUserNameInput);
        final TextView result_text=(TextView)root.findViewById(R.id.result_user_text);

        //验证完用户后下一步点击事件
        Button bt=(Button) root.findViewById(R.id.next_step_one);
        bt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        String userName=userNameInput.getText().toString();
                        //将用户名传递给验证密保界面
                        InputSecurityFragment securityFragment=new InputSecurityFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("key",userName);
                        securityFragment.setArguments(bundle);
                        if (userName.length()==0){
                            result_text.setText("请输入用户名");
                            result_text.setTextColor(0xFFC81522);
                        }
                        else{
                            if (!TextUtils.isEmpty(DBUrl.getLoginInfo(userName))){
                                FragmentManager fm = getFragmentManager();
                                fm.beginTransaction()
                                        //将当前页面替换为密保验证界面
                                        .replace(R.id.nav_retrieve_fragment,securityFragment)
                                        .commit();
                            }
                            else{
                                result_text.setText("用户不存在");
                                result_text.setTextColor(0xFFC81522);
                            }
                        }
                    }
                }.start();
            }
        });
        return root;
    }

}