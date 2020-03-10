package com.example.menu.DB;

import android.annotation.SuppressLint;

import java.io.File;

public class URLBase {
    //用到的地址以及路径，返回所需地址的方法
    public static String getURL(int flag){
        @SuppressLint("SdCardPath")
                //url[0]是上传头像的地址，url[1]是下载头像的地址,url[2]是本地存储头像的缓存地址,url[3]数据库链接地址
        String [] url = {"http://172.16.3.158:8080/MenuServer/jsp/uploadImg.jsp",
                         "http://172.16.3.158:8080/userIcon/",
                         "/sdcard/menu/userIcon/",
                         "jdbc:jtds:sqlserver://172.16.3.158:1433;databaseName=Menu",
                            };
        String path = null;
        switch (flag){
            //flag=0表示获取的头像上传地址
            case 0:path = url[0];break;
            //flag=1表示获取的头像下载地址
            case 1:path = url[1];break;
            //flag=2表示获取的本地存储头像的缓存地址
            case 2:path = url[2];break;
            //flag=3表示获取的数据库链接地址
            case 3:path = url[3];break;
        }
        return path;
    }
    //创建本地缓存目录
    public static void createFolder(){
        //创建app缓存目录
        @SuppressLint("SdCardPath")
        //app根目录
                File file = new File("/sdcard/menu/");
        @SuppressLint("SdCardPath")
        //用户头像目录
                File userIcon = new File("/sdcard/menu/userIcon");
        //附件
        @SuppressLint("SdCardPath")
                File accessory = new File("/sdcard/menu/accessory");

        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs();
            if (!userIcon.exists()) {
                userIcon.mkdirs();
            }
            }
        if (!accessory.exists()) {
            accessory.mkdirs();
        }
    }
    //题库代号判断
    public static int getQuestionCode(String TBName){//TBName表名称
        int key = 100;
        if (TBName.equals("QB000")){
            key=0;//精细化
        }
        else if (TBName.equals("QB010")){
            key=1;//一建模拟
        }
        else if(TBName.equals("QB011")){
            key=2;//一建真题
        }
        else if (TBName.equals("QB020")){
            key=3;//安全工程师模拟
        }
        else if (TBName.equals("QB021")){
            key=4;//安全工程师真题
        }
        return key;
    }
}
