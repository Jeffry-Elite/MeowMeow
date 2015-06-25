package com.mobile.elite.meowmeow.screen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.kidzie.jeff.restclientmanager.Logging;
import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.parser.VideoDataParser;
import com.mobile.elite.meowmeow.view.CustomVideoView;
import com.mobile.elite.meowmeow.view.VideoControllerView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class VideoDetail extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener,VideoControllerView.MediaPlayerControl, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    private MediaPlayer player;
    private SurfaceView videoSurface;
    private VideoControllerView controller;
    private JSONArray jsData;
    private VideoDataParser dataParser;
    private ProgressBar loadingDialog;

    private int mPosition;
    private int NEXT_TAG = 1;
    private int BACK_TAG = 2;
    private boolean isCanNextOrBack = true;

    private String url_thumb_1;
    private String url_thumb_2;

    int position_thumb_1;
    int position_thumb_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initView();
        mPosition  = getIntent().getIntExtra("position",0);
        loadVideo(mPosition);
    }

    private void initView() {
        setContentView(R.layout.video_detail);
        dataParser = new VideoDataParser();
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        loadingDialog = (ProgressBar)findViewById(R.id.loading_view);
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
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        loadingDialog.setVisibility(View.VISIBLE);
        player.setOnCompletionListener(this);
        player.setOnInfoListener(this);
        controller.setMediaPlayer(this);

        videoSurface.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoSurface.getHolder().setFormat(PixelFormat.OPAQUE);
        String videoUrl= "";

        try {
            // Get data from main menu in video tab

            // Get data for current video in index of the array
            dataParser.setJsData(jsData.getJSONObject(position));
            videoUrl = dataParser.getUrlVideo(); // Get URL for video

            player.setDataSource(this, Uri.parse(videoUrl));
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
            player.prepareAsync();

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
        isCanNextOrBack = true;
        if(loadingDialog.getVisibility() == View.VISIBLE){
            loadingDialog.setVisibility(View.INVISIBLE);
        }
        if(controller.isShowing())
            controller.hide();
        Log.d("On Prepared", "On Prepared");

        position_thumb_1 = mPosition == jsData.length()-1 ? 0 : mPosition + 1;
        position_thumb_2 = position_thumb_1 == jsData.length() - 1 ? 0 : position_thumb_1 + 1;
        controller.setThumbnail_1(getUrlThumb(position_thumb_1));
        controller.setThumbnail_2(getUrlThumb(position_thumb_2));
        controller.setTitleVideo(dataParser.getVideoTitle());
        mp.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            loadingDialog.dismiss();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(loadingDialog.getVisibility() != View.VISIBLE && !controller.isShowing())
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
        if(!isCanNextOrBack)
            return;
        if(mPosition == jsData.length() - 1)
            mPosition = 0;
        else
            mPosition++;

        isCanNextOrBack = false;
        controller.setEndTime(0);
        player.stop();
        player.reset();
//        if(controller.isShowing())
//            controller.hide();

    }

    @Override
    public void back() {
        if(!isCanNextOrBack)
            return;
        if(mPosition == 0)
            mPosition = jsData.length() - 1;
        else
            mPosition--;

        isCanNextOrBack = false;
        controller.setEndTime(0);
        player.stop();
        player.reset();
//        if(controller.isShowing())
//            controller.hide();

    }
    @Override
    public void next_2_video(){

        mPosition = position_thumb_2;
        controller.setEndTime(0);
        player.stop();
        player.reset();
//        if(controller.isShowing())
//            controller.hide();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
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

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START :
                if(loadingDialog.getVisibility() != View.VISIBLE)
                    loadingDialog.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if(loadingDialog.getVisibility() == View.VISIBLE)
                    loadingDialog.setVisibility(View.INVISIBLE);
                break;
        }
        return false;
    }
}
