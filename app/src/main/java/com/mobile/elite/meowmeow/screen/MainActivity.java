package com.mobile.elite.meowmeow.screen;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.adapter.TabsPagerAdapter;


public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private TabsPagerAdapter tabAdapter;
    private String[] tabtitle ;
    private TextView txtVideoTitle;
    private TextView txtImageTitle;
    private Typeface arialType;
    private Typeface arialBold;
    private View pointerVideo;
    private View pointerImage;
    private int VIDEO_TAG = 0;
    private int IMAGE_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.pager);
        tabAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        txtVideoTitle = (TextView)findViewById(R.id.video_title_tab);
        txtImageTitle = (TextView)findViewById(R.id.picture_title_tab);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(this);

        actionBar = getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        View customActionBar = inflater.inflate(R.layout.custom_action_bar,null);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(customActionBar);
        actionBar.setDisplayShowCustomEnabled(true);

        arialType = Typeface.createFromAsset(getAssets(),"fonts/arial.ttf");
        arialBold = Typeface.createFromAsset(getAssets(),"fonts/arial_bold.ttf");

        txtVideoTitle.setTypeface(arialType);
        txtImageTitle.setTypeface(arialType);
        pointerVideo = findViewById(R.id.pointer_video);
        pointerImage = findViewById(R.id.pointer_picture);

        txtVideoTitle.setOnClickListener(this);
        txtImageTitle.setOnClickListener(this);
        txtVideoTitle.setTag(VIDEO_TAG);
        txtImageTitle.setTag(IMAGE_TAG);

        setPointerTab(0); // always set pointer to video tab at beginning
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPointerTab(position);
    }



    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 0 for video tab
    // 1 for image tab
    private void setPointerTab(int position) {
        switch (position) {
            case 0: {
                pointerVideo.setVisibility(View.VISIBLE);
                pointerImage.setVisibility(View.INVISIBLE);
            }
            break;
            case 1: {
                pointerVideo.setVisibility(View.INVISIBLE);
                pointerImage.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() == VIDEO_TAG){
            if(viewPager.getCurrentItem() == VIDEO_TAG)
                return; // do nothing if current tab is in video
           setTabAndPosition(0);
        } else if(v.getTag() == IMAGE_TAG){
            if(viewPager.getCurrentItem() == IMAGE_TAG)
                return; // do nothing if current tab is in image
            setTabAndPosition(1);
        }
    }

    private void setTabAndPosition(int position){
        setPointerTab(position);
        viewPager.setCurrentItem(position);
    }
}
