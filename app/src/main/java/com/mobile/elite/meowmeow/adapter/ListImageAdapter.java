package com.mobile.elite.meowmeow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mobile.elite.meowmeow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jeffry
 * 01-Jun-2015
 */
public class ListImageAdapter extends BaseAdapter {
    private Context context ;
    private JSONArray jsArray;
    private ImageClickListener listener;

    public ListImageAdapter(Context context, JSONArray jsArray, ImageClickListener listener) {
        super();
        this.context = context;
        this.jsArray = jsArray;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return jsArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        this.jsArray = null;
    }

    public void add(JSONArray jsArray){
        this.jsArray = jsArray;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
           convertView = inflater.inflate(R.layout.item_meow_list, null);
            viewHolder = new ViewHolder();
            viewHolder.imageHolder = (ImageView)convertView.findViewById(R.id.image_meow);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder)convertView.getTag();
        }
        String url = null;
         JSONObject jsData;
        try {

            url = jsArray.getJSONObject(position).getString("actual_url");
        } catch (JSONException e) {
            url = "";

        }
        viewHolder.imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.onImageClick(jsArray.getJSONObject(position),jsArray,position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(url).centerCrop().into(viewHolder.imageHolder);
        return convertView;
    }


    private static class ViewHolder{
        ImageView imageHolder;
    }

    public interface ImageClickListener{
        public void onImageClick(JSONObject jsData,JSONArray jsArray, int position);
    }
}
