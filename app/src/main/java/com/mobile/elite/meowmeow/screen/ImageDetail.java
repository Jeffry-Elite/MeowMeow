package com.mobile.elite.meowmeow.screen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.adapter.image.ImageDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */
public class ImageDetail extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    String url;
    private ViewPager viewPager;
    private ImageDetailAdapter imageAdapter;
    private ImageButton buttonNext;
    private ImageButton buttonBack;
    private int NEXT_TAG = 1;
    private int BACK_TAG = 2;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    private void initView() {
        setContentView(R.layout.image_detail_layout);
        int position  = getIntent().getIntExtra("position", 0);
        JSONArray jsArr = new JSONArray();
        try {
            jsArr = new JSONArray(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewPager = (ViewPager)findViewById(R.id.image_detail_pager);
        buttonBack = (ImageButton)findViewById(R.id.button_back);
        buttonNext = (ImageButton)findViewById(R.id.button_next);
        buttonBack.setTag(BACK_TAG);
        buttonNext.setTag(NEXT_TAG);

        buttonNext.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        imageAdapter = new ImageDetailAdapter(this,jsArr);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() == BACK_TAG){
            if(viewPager.getCurrentItem() == 0)
                viewPager.setCurrentItem(imageAdapter.getCount() - 1);
            else
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else if (v.getTag() == NEXT_TAG){
            if(viewPager.getCurrentItem() == imageAdapter.getCount() -1)
                viewPager.setCurrentItem(0);
            else
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
