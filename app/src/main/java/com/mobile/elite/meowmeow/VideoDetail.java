package com.mobile.elite.meowmeow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.MediaController;

import com.mobile.elite.meowmeow.view.CustomVideoView;
import com.mobile.elite.meowmeow.view.VideoControllerView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class VideoDetail extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,VideoControllerView.MediaPlayerControl {

    private CustomVideoView videoView;
    private ProgressDialog progressDialog;
    private MediaPlayer player;
    private SurfaceView videoSurface;
    private VideoControllerView controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadVideo();
    }

    private void initView() {
        setContentView(R.layout.video_detail);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

//        videoView = (CustomVideoView)findViewById(R.id.video_view);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setCancelable(false);
//
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
//        videoView.setDimensionVideo(height, width);
//        videoView.getHolder().setFixedSize(height, width);
    }


    private void loadVideo() {


        JSONArray jsData;
        String videoUrl= "";
        int position = 0;
        try {
            // Get data from main menu in video tab
             jsData = new JSONArray(getIntent().getStringExtra("data")); // get all for video
            position = getIntent().getIntExtra("position",0); // Get data for current video in index of the array
            videoUrl = jsData.getJSONObject(position).getString("actual_content"); // Get URL for video

            player.setDataSource(this, Uri.parse(videoUrl));
            player.setOnPreparedListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        videoView.requestFocus();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            public void onPrepared(MediaPlayer mp) {
//                progressDialog.dismiss();
//                videoView.start();
//            }
//        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
}
