package com.mobile.elite.meowmeow.adapter.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.listener.ImageClickListener;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class GridVideoAdapter extends BaseAdapter {

    private JSONArray jsData;
    private Context context;
    private ImageClickListener listener;

    public GridVideoAdapter(Context context, JSONArray jsData,ImageClickListener listener) {
        this.context = context;
        this.jsData = jsData;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return jsData.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  void clear(){
        jsData = null;
    }

    public void add(JSONArray jsData){
        this.jsData = jsData;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Typeface typeface;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.video_item_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.imageHolder = (ImageView)convertView.findViewById(R.id.image_video);
            viewHolder.textHolder = (TextView)convertView.findViewById(R.id.txt_title_video);
            typeface = Typeface.createFromAsset(context.getAssets(),"fonts/arial.ttf");
            viewHolder.textHolder.setTypeface(typeface);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String url = "";
        String title = "";
        try {
            url = jsData.getJSONObject(position).getString("160x120");
            title = jsData.getJSONObject(position).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.textHolder.setText(title);

        viewHolder.imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (listener != null)
                        listener.onImageClick(jsData.getJSONObject(position), jsData, position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Glide.with(context).load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.imageHolder.setBackgroundDrawable(new BitmapDrawable(context.getResources(), resource));
                    }
                });

        return convertView;
    }

    private class ViewHolder{
        ImageView imageHolder;
        TextView textHolder;
    }
}
