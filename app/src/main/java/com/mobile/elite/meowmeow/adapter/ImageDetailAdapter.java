package com.mobile.elite.meowmeow.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mobile.elite.meowmeow.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class ImageDetailAdapter extends PagerAdapter {

    private JSONArray jsData = new JSONArray();
    private ViewHolder viewHolder = new ViewHolder();
    private Context context;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((LinearLayout)object);
    }

    public ImageDetailAdapter(Context context,JSONArray jsData) {
        this.jsData = jsData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return jsData.length();
    }

    public void addData(JSONArray jsData){
        this.jsData = jsData;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.image_detail_activity, null);
        viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_detail);
        String url = null;
        try {
            url = jsData.getJSONObject(position).getString("actual_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(context).load(url).into(viewHolder.imageView);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private static class ViewHolder{
        ImageView imageView;
    }
}
