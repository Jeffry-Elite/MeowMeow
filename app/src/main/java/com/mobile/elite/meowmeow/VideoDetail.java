package com.mobile.elite.meowmeow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.MediaController;

import com.mobile.elite.meowmeow.view.CustomVideoView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class VideoDetail extends Activity {

    private CustomVideoView videoView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadVideo();
    }

    private void initView() {
        setContentView(R.layout.video_detail);
        videoView = (CustomVideoView)findViewById(R.id.video_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        videoView.setDimensionVideo(height, width);
        videoView.getHolder().setFixedSize(height, width);
    }

    private void loadVideo() {
        JSONArray jsData;
        String videoUrl= "";
        int posistion = 0;
        try {
             jsData = new JSONArray(getIntent().getStringExtra("data"));
            posistion = getIntent().getIntExtra("position",0);
            videoUrl = jsData.getJSONObject(posistion).getString("actual_content");

            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);

            Uri video = Uri.parse(videoUrl);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                videoView.start();
            }
        });

    }

}
