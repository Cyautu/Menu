package com.example.menu.ui.mainHome.mine.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Users;
import com.example.menu.service.userIcon.UpUserIconService;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PersonalEditActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private ArrayAdapter<CharSequence> adapter;
    private String sex,nickName,introduction;//定义性别、昵称、简介变量，用于存储前台输入的字符
    private ImageView userPhoto;//头像view
    Uri uritempFile;//裁剪好的头像存储路径
    Button edit_photo;
    @SuppressLint("SdCardPath")
    private static String path = URLBase.getURL(2);//URLBase.getURL(2)获取本地头像缓存地址
    String userID;//用户ID
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_edit);
        //判断用户手机的android版本，解决高版本相机调用报错bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build() );
            builder.detectFileUriExposure();
        }
        //找到对应的控件id
        userPhoto = (ImageView)findViewById(R.id.user_Photo);
        final TextView user_Name = (TextView)findViewById(R.id.username_text);
        final EditText user_NickName = (EditText) findViewById(R.id.nickname_input);
        final Spinner choose_sex = (Spinner)findViewById(R.id.choose_sex);
        final Button save_edit = (Button) findViewById(R.id.edit_save_button);
        /*设置简介编辑框自动换行*/
        final EditText user_Introduction = (EditText) findViewById(R.id.introduction_input);
        //设置EditText的显示方式为多行文本输入
        user_Introduction.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        user_Introduction.setGravity(Gravity.TOP);
        //改变默认的单行模式
        user_Introduction.setSingleLine(false);
        //水平滚动设置为False
        user_Introduction.setHorizontallyScrolling(false);

        //从SharedPreferences获取userID以及userName
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("userID",null);
        final String userName = sharedPreferences.getString("userName",null);
        String userHeadPhoto = sharedPreferences.getString("userIcon",null);
        final String userNickName = sharedPreferences.getString(("userNickName"),null);

        //显示当前设置的昵称、简介
        user_Name.setText(userName);
        user_NickName.setText(userNickName);
        new Thread(new Runnable() {
            String u[]=new String[2];//0存储性别,1存储简介
            public void run() {
                ArrayList<Users> is=new DBUrl().getOrdinaryInformation(userID);
                for(Users i:is){
                    u[1]=i.getUserIntroduction();
                    u[0]=i.getUserSex();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        user_Introduction.setText(u[1]);
                    }
                });
            }
        }).start();
        //性别选择框监听
        choose_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex=choose_sex.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //将性别列表导入
        adapter= ArrayAdapter.createFromResource(this,R.array.sex_list,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_sex.setAdapter(adapter);//绑定适配器类
       /* //声明并返回按钮并调用判断当前页面的开闭情况方法，并进一步作出返回事件的反应
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }*/

        //保存按钮监听事件
        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        //获取前端输入的昵称、简介
                        nickName = user_NickName.getText().toString().trim();
                        introduction = user_Introduction.getText().toString().trim();
                        DBUrl db = new DBUrl();
                        Looper.prepare();
                        if (nickName.length()!=0){//判断昵称是否为空
                            boolean  result = false;
                            if (introduction.length()!=0){
                                result = db.setOrdinaryInformation(userID,nickName,sex,introduction);
                            }
                            else {
                                result = db.setOrdinaryInformation(userID,nickName,sex,"暂未设置个人简介");
                            }
                            if (result){
                                //将新的昵称存入SharedPreferences
                                SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("userNickName",nickName);
                                editor.apply();
                                Toast.makeText(PersonalEditActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent();
                                intent.setClass(PersonalEditActivity.this, PersonalActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                                */
                                PersonalEditActivity.this.finish();
                               /* startActivity(intent);*/
                            }
                            else {
                                Toast.makeText(PersonalEditActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(PersonalEditActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    }
                }.start();
            }
        });
        //修改头像
        edit_photo = (Button)findViewById(R.id.edit_photo_button);
        /*显示头像，判断是否为默认头像*/
            Bitmap bt = BitmapFactory.decodeFile(path +userID+ "userIcon.jpg");// 从SD卡中找头像，转换成Bitmap
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                userPhoto.setImageDrawable(drawable);
            }
        //点击修改头像，调用 showTypeDialog()
        edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });
        //声明并返回按钮并调用判断当前页面的开闭情况方法，并进一步作出返回事件的反应
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
    }
    //弹出头像图片选择方式
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_change_icon, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        //调试有问题
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "userIcon.jpg")));
                intent2.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/userIcon.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bitmap head = null;
                    try {
                        head = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //调用上传服务器代码,因为涉及到上传头像名称到数据库，所以必须新建线程
                    new Thread(){
                        @Override
                        public void run() {
                            int flag;
                            UpUserIconService up = new UpUserIconService();
                            flag=up.uploadImage(path +userID+ "userIcon.jpg",userID+"userIcon",userID);
                            Looper.prepare();
                            if (flag==1){
                                Toast.makeText(PersonalEditActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(PersonalEditActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                            } Looper.loop();
                        }
                    }.start();
                    //将新的头像名称存入SharedPreferences
                    SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userIcon",userID+"userIcon.jpg");
                    editor.apply();
                    userPhoto.setImageBitmap(head);// 用ImageView显示出来
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //图片裁剪
    @SuppressLint("SdCardPath")
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        uritempFile = Uri.parse("file://" + "/" + "/sdcard/menu/userIcon/" + "/" + userID+"userIcon.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    }

    //返回
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
