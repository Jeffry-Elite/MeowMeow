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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.kidzie.jeff.restclientmanager.Logging;
import com.mobile.elite.meowmeow.view.CustomVideoView;
import com.mobile.elite.meowmeow.view.VideoControllerView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class VideoDetail extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener,VideoControllerView.MediaPlayerControl, MediaPlayer.OnCompletionListener {

    private CustomVideoView videoView;
    private ProgressDialog progressDialog;
    private MediaPlayer player;
    private SurfaceView videoSurface;
    private VideoControllerView controller;
    private JSONArray jsData;

    private int mPosition;
    private int NEXT_TAG = 1;
    private int BACK_TAG = 2;

    private String url_thumb_1;
    private String url_thumb_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPosition  = getIntent().getIntExtra("position",0);
        loadVideo(mPosition);
    }

    private void initView() {
        setContentView(R.layout.video_detail);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        try {
            jsData = new JSONArray(getIntent().getStringExtra("data")); // get all for video
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void loadVideo(int position) {
        player.setOnCompletionListener(this);

        String videoUrl= "";
        try {
            // Get data from main menu in video tab

            // Get data for current video in index of the array
            videoUrl = jsData.getJSONObject(position).getString("actual_content"); // Get URL for video

            player.setDataSource(this, Uri.parse(videoUrl));
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);

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
        int position_thumb_1;
        int position_thumb_2;
        position_thumb_1 = mPosition == jsData.length()-1 ? 0 : mPosition + 1;
        position_thumb_2 = position_thumb_1 == jsData.length() - 1 ? 0 : position_thumb_1 + 1;
        controller.setThumbnail_1(getUrlThumb(position_thumb_1));
        controller.setThumbnail_2(getUrlThumb(position_thumb_2));
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

    @Override
    public void next() {
        if(mPosition == jsData.length() - 1)
            mPosition = 0;
        else
            mPosition++;

        player.reset();

    }

    @Override
    public void back() {
        if(mPosition == 0)
            mPosition = jsData.length() - 1;
        else
            mPosition--;

        player.reset();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        loadVideo(mPosition);

    }

    private String getUrlThumb(int position){
        String url_thumb = "";
        try {
            url_thumb = jsData.getJSONObject(position).getString("thumb");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url_thumb;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Logging.setLog(Logging.LOGTYPE.debug, "buffer", "buffer=" + percent, null);
    }
}
