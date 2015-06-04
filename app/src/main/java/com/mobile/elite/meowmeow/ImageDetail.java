package com.mobile.elite.meowmeow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobile.elite.meowmeow.adapter.ImageDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */
public class ImageDetail extends Activity implements ImageDetailAdapter.ListenerButtonClick {
    String url;
    private ViewPager viewPager;
    private ImageDetailAdapter imageAdapter;

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
        imageAdapter = new ImageDetailAdapter(this,jsArr,this);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onButtonBackClick() {
        if(viewPager.getCurrentItem() == 0)
            viewPager.setCurrentItem(imageAdapter.getCount()-1);
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

    }

    @Override
    public void onButtonNextClick() {
        if(viewPager.getCurrentItem() == imageAdapter.getCount()-1)
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }
}
