package com.mobile.elite.meowmeow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobile.elite.meowmeow.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class GridVideoAdapter extends BaseAdapter {

    private JSONArray jsData;
    private Context context;

    public GridVideoAdapter(Context context, JSONArray jsData) {
        this.context = context;
        this.jsData = jsData;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.video_item_grid, null);
            viewHolder = new ViewHolder();
            viewHolder.imageHolder = (ImageView)convertView.findViewById(R.id.image_video);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String url = "";
        try {
            url = jsData.getJSONObject(position).getString("160x120");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(context).load(url).fitCenter().into(viewHolder.imageHolder);

        return convertView;
    }

    private class ViewHolder{
        ImageView imageHolder;
    }
}
