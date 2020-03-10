package com.example.menu.service.userIcon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.menu.DB.DBUrl;
import com.example.menu.DB.URLBase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UpUserIconService {
    private ProgressDialog prgDialog;
    private int RESULT_LOAD_IMG = 1;
    private RequestParams params = new RequestParams();
    private String encodedString;
    private Bitmap bitmap;

    //开始上传图片
    public int uploadImage(String imgPath,String imgName,String userID) {
        int count = 0;//0出错
        if (imgPath != null && !imgPath.isEmpty()) {
            encodeImagetoString(imgPath,imgName);
            setUserIconNameSQL(imgName,userID);
            count = 1;
        }

        return count;
    }
    //加密
    @SuppressLint("StaticFieldLeak")
    public void encodeImagetoString(String imgPath,String imgName) {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
            }
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                System.out.println(imgPath);
                // 压缩图片
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                // Base64图片转码为String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
               /* prgDialog.setMessage("Calling Upload");*/
                // 将转换后的图片添加到上传的参数中
                params.put("image", encodedString);
                params.put("filename", imgName);
                // 上传图片
                imageUpload();
            }
        }.execute(null, null, null);
    }
    //上传开始
    private void imageUpload() {
        /*prgDialog.setMessage("开始上传...");*/
        String url = URLBase.getURL(0);//从URLBase.getURL(0)获取上传地址
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if (statusCode == 404) {
                   /* Toast.makeText(PersonalEditActivity.this,
                            "服务未找到", Toast.LENGTH_LONG).show();*/
                }
                // 当 Http 响应码'500'
                else if (statusCode == 500) {
                   /* Toast.makeText(getApplicationContext(),
                            "错误！", Toast.LENGTH_LONG).show();*/
                }
                // 当 Http 响应码 404, 500
                else {
                    /*Toast.makeText(
                            getApplicationContext(), "服务器连接失败 " +
                                    "，请检查网络"
                                    + statusCode, Toast.LENGTH_LONG).show();*/
                }
            }
        });
    }
    /*protected void onDestroy() {
        super.onDestroy();
        if (prgDialog != null) {
            prgDialog .dismiss();
        }
    }*/
    //将头像名称写入数据库
    private void setUserIconNameSQL(String fileName,String userID){
        String sql = "update Users set user_Icon=? where user_ID=?";
        PreparedStatement ptst = null;
        Connection con = null;
        con = DBUrl.getSQLConnection();
        try {
            ptst = con.prepareStatement(sql);
            ptst.setString(1,fileName);
            ptst.setString(2,userID);
            ptst.executeUpdate();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
