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
import com.mobile.elite.meowmeow.parser.VideoDataParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class GridVideoAdapter extends BaseAdapter {

    private JSONArray jsData;
    private Context context;
    private ImageClickListener listener;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    private VideoDataParser dataParser;

    public GridVideoAdapter(Context context, JSONArray jsData,ImageClickListener listener) {
        this.context = context;
        this.jsData = jsData;
        this.listener = listener;
        dataParser = new VideoDataParser();
    }

    @Override
    public int getCount() {
        return list.size();
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
        jsData = new JSONArray();
        list = new ArrayList<JSONObject>();
    }

    public void add(JSONArray jsData){
        for(int i = 0 ; i < jsData.length() ; i++){
            try {
                list.add(jsData.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        dataParser.setJsData(list.get(position));
        String url = dataParser.getUrlImageVideo();
        String title = dataParser.getVideoTitle();

        viewHolder.textHolder.setText(title);

        viewHolder.imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    JSONArray jsArray = convertListToJsonArray(list);
                    listener.onImageClick(dataParser.getJsData(), jsArray, position);
                }
            }
        });

        Glide.with(context).load(url).placeholder(R.drawable.icon).into(viewHolder.imageHolder);
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        viewHolder.imageHolder.setBackgroundDrawable(new BitmapDrawable(context.getResources(), resource));
//                    }
//                });

        return convertView;
    }

    private JSONArray convertListToJsonArray(List<JSONObject> list) {
        JSONArray jsArray = new JSONArray();
        for(int i =0 ; i < list.size() ; i++){
            JSONObject jsObject = list.get(i);
            try {
                jsArray.put(i,jsObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsArray;
    }

    private class ViewHolder{
        ImageView imageHolder;
        TextView textHolder;
    }
}
