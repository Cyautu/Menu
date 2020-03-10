package com.example.menu.ui.mainHome.training;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menu.DB.DBUrl;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Training;
import com.example.menu.service.downloadFile.DownloadUtil;

import java.io.File;
import java.util.ArrayList;

public class TrainingDetailActivity extends AppCompatActivity {

    String accessoryPath, videoPath;
    MediaController mmediaController;
    int queryMsg = 0;
    ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        TextView trainingTitle, ofCompany, ofProjectDep, trainingSpeaker, trainingTime, trainingContent;
        ArrayList<Training> list;
        list = (ArrayList<Training>) getIntent().getSerializableExtra("list");
        int position = getIntent().getIntExtra("position", 0);
        trainingTitle = findViewById(R.id.trainingTitle);
        ofCompany = findViewById(R.id.ofCompany);
        ofProjectDep = findViewById(R.id.ofProjectDep);
        trainingSpeaker = findViewById(R.id.trainingSpeaker);
        trainingTime = findViewById(R.id.trainingTime);
        trainingContent = findViewById(R.id.trainingContent);
        progressBar = findViewById(R.id.progressBar);
        if (list != null) {
            trainingTitle.setText(list.get(position).getTrainingTitle());
            ofCompany.setText(list.get(position).getOfCompany());
            ofProjectDep.setText(list.get(position).getOfProjectDep());
            trainingSpeaker.setText(list.get(position).getTrainingSpeaker() + " 主讲");
            trainingTime.setText(list.get(position).getTrainingTime());
            trainingContent.setText(list.get(position).getTrainingContent());
        }
        findViewById(R.id.back).setOnClickListener(v -> TrainingDetailActivity.this.finish());
        findViewById(R.id.accessory).setOnClickListener(v -> {
            if (new File(Environment.getExternalStorageDirectory().getPath() + "/menu/accessory/" + accessoryPath.substring(43)).exists())
            {
                try {
                    Intent intent = getAccessoryFileIntent();
                    startActivity(intent);
                } catch (Exception e) {
                    //没有安装第三方的软件会提示
                    Toast.makeText(getApplicationContext(), "没有找到wps", Toast.LENGTH_SHORT).show();
                }
            }else if (accessoryPath != null) {
                progressBar.setVisibility(View.VISIBLE);
                downAccessory();
            } else {
                Toast.makeText(getApplicationContext(), "附件为空", Toast.LENGTH_SHORT).show();
            }
        });
        new Thread(() -> {
            if (list != null) {
                accessoryPath = DBUrl.getTrainingPath("Training_AccessoryPath", list.get(position).getTrainingID());
                videoPath = DBUrl.getTrainingPath("Training_VideoPath", list.get(position).getTrainingID());
            }
            Message msg = new Message();
            msg.what = queryMsg;
            handler.sendMessage(msg);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                if (videoPath != null) {
                    VideoView videoView;
                    videoView = findViewById(R.id.videoView);
                    RelativeLayout videoContainer = findViewById(R.id.videoContainer);
                    View contentView = findViewById(R.id.ScrollView);
                    videoView.setVideoPath(videoPath);
                    videoView.requestFocus();
                    videoView.setMediaController(new android.widget.MediaController(TrainingDetailActivity.this));
                    videoView.start();
                    mmediaController = new MediaController(TrainingDetailActivity.this)
                            .setPlayerParent(videoContainer)
                            .setPlayer(videoView)
                            .setContentView(contentView)
                            .build();
                } else {
                    Toast.makeText(getApplicationContext(), "视频不见了...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean tag = getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        findViewById(R.id.toolbar).setVisibility(!tag ? View.GONE : View.VISIBLE);
        mmediaController.switchOrientation(tag);
        fullScreen(!tag);
    }

    public void downAccessory() {
        new Thread(() -> DownloadUtil.get().download(accessoryPath, Environment.getExternalStorageDirectory().getPath() + "/menu/accessory/", accessoryPath.substring(43),
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        /*Log.e("---------", "onDownloadSuccess");*/
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            Intent intent = getAccessoryFileIntent();
                            startActivity(intent);
                        } catch (Exception e) {
                            //没有安装第三方的软件会提示
                            Toast.makeText(getApplicationContext(), "没有找到wps", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDownloading(int progress) {
                        /*Log.e("---------", "onDownloading");*/
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        /*Log.e("---------", "onDownloadFailed" + e.getMessage());*/
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "下载出错了···", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Looper.loop();
                    }
                })
        ).start();
    }

    public Intent getAccessoryFileIntent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "ReadOnly");// 只读模式
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng",
                "cn.wps.moffice.documentmanager.PreStartActivity2");
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/menu/accessory/" + accessoryPath.substring(43)));
        intent.setData(uri);
        intent.putExtras(bundle);
        return intent;
    }

    //全屏切换
    public void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
