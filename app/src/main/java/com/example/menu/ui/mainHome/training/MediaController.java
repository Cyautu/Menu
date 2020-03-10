package com.example.menu.ui.mainHome.training;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.menu.R;
import com.example.menu.ui.mainHome.training.utils.DensityUtil;
import com.example.menu.ui.mainHome.training.utils.MTimeUtils;



public class MediaController {

    private final Activity activity;

    private VideoView player;
    private View contentView;
    private View controllerView;


    private ImageView stop$play;
    private ImageView requestOrien;
    private TextView timeProcess;

    private RelativeLayout playerParent;
    private SeekBar seekBar;

    MediaController(Activity activity) {
        this.activity = activity;
        initView();
    }

    //初始化控件
    private void initView() {
        controllerView = LayoutInflater.from(activity).inflate(R.layout.include_play_control, null, false);
        requestOrien = controllerView.findViewById(R.id.request_orien);
        stop$play = controllerView.findViewById(R.id.stopAndPlay);
        timeProcess = controllerView.findViewById(R.id.timeProcess);
        seekBar = controllerView.findViewById(R.id.timeline);
    }

    MediaController setPlayer(VideoView player) {
        this.player = player;
        return this;
    }

    public MediaController setContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    MediaController setPlayerParent(RelativeLayout playerParent) {
        this.playerParent = playerParent;
        return this;
    }

    public MediaController build() {
        initListener();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, player.getId());
        controllerView.setLayoutParams(layoutParams);
        playerParent.addView(controllerView);
        controllerView.setVisibility(View.GONE);
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        //暂停和播放：
        stop$play.setOnClickListener(view -> {
            boolean tag = stop$play.getTag() != null && (boolean) view.getTag();
            if (player.isPlaying()) {
                player.pause();
                stop$play.setImageResource(R.drawable.ic_play_arrow_24dp);
            } else {
                player.start();
                stop$play.setImageResource(R.drawable.ic_pause_24dp);
            }
            stop$play.setTag(!tag);
        });

        //横竖屏切换：
        requestOrien.setOnClickListener(view -> {
            boolean tag = requestOrien.getTag() != null && (boolean) requestOrien.getTag();
            activity.setRequestedOrientation(!tag ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            requestOrien.setTag(!tag);
        });
        //拖动进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean isTouch = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int precent, boolean b) {
                if (isTouch) {
                    //计算拖动后的时间长度：
                    int positionLong = player.getDuration() * seekBar.getProgress() / 100;
                    player.seekTo(positionLong);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(0);
                isTouch = true;
                player.pause();
                stop$play.setImageResource(R.drawable.ic_pause_24dp);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (controllerView.getVisibility() == View.VISIBLE) {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 4000);
                    }
                }
                isTouch = false;
                player.start();
                stop$play.setImageResource(R.drawable.ic_pause_24dp);
            }
        });
        //播放进度
        player.setOnPlayingListener(() -> {
            int current = player.getCurrentPosition();
            int duration = player.getDuration();
            timeProcess.setText(Html.fromHtml(MTimeUtils.formatTime(current) + "/" + MTimeUtils.formatTime(duration)));
            seekBar.setProgress(current * 100 / duration);
        });

        player.setOnCompletionListener(mp -> stop$play.setImageResource(R.drawable.ic_play_arrow_24dp));

        //添加控制器的显示和隐藏:
        player.setOnTouchListener((view, motionEvent) -> {
            mGesde.onTouchEvent(motionEvent);
            return true;
        });
    }
    //屏幕方向的切换

    void switchOrientation(boolean tag) {
        int width =  DensityUtil.getWh(activity)[0];
        int height = tag ? DensityUtil.dip2px(activity, 230) : DensityUtil.getWh(activity)[1];
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(width, height);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, height);
        player.setLayoutParams(params1);
        playerParent.setLayoutParams(params2);
        contentView.setVisibility(tag ? View.VISIBLE : View.GONE);
        requestOrien.setImageResource(tag ? R.drawable.ic_fullscreen_24dp : R.drawable.ic_fullscreen_exit_24dp);
    }

    private GestureDetector mGesde = new GestureDetector(new MSimpleGestureDectListener());

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            controllerView.setVisibility(View.GONE);
        }
    };

    //手势处理
    private class MSimpleGestureDectListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //判断
            if (controllerView.getVisibility() == View.VISIBLE) {
                controllerView.setVisibility(View.GONE);
            } else {
                controllerView.setVisibility(View.VISIBLE);
            }

            //定时   关闭控制器
            if (controllerView.getVisibility() == View.VISIBLE) {
                if (mHandler != null) {
                    mHandler.removeMessages(0);
                    mHandler.sendEmptyMessageDelayed(0, 4000);
                }
            }
            return true;
        }

    }
}
