package com.example.menu.service.userIcon;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.menu.DB.URLBase;
import com.loopj.android.http.HttpGet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DownloadUserIconSerice {
    //从服务器下载用户头像
    public static Bitmap getImage(String path){
        HttpGet get = new HttpGet(path);
        HttpClient client = new DefaultHttpClient();
        Bitmap pic = null;
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            pic = BitmapFactory.decodeStream(is);  // 关键是这句代
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pic;
    }
    public static void saveMyBitmap(Bitmap mBitmap, String bitName){
        @SuppressLint("SdCardPath")
        File f = new File( URLBase.getURL(2) +bitName + ".jpg");//URLBase.getURL(2)获取本地头像缓存地址
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
