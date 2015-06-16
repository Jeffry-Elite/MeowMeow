package com.mobile.elite.meowmeow.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffry on 11-Jun-15.
 */
public class VideoDataParser {

    private JSONObject jsData;

    public JSONObject getJsData() {
        return jsData;
    }

    public void setJsData(JSONObject jsData) {
        this.jsData = jsData;
    }

    public String getUrlVideo(){
        String url = "";
        try {
            url = jsData.getString("actual_content");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return url;
    }

    public String getUrlThumbnail(){
        String urlThumb = "";
        try {
            urlThumb = jsData.getString("thumb");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  urlThumb;
    }

    public String getVideoTitle(){
        String title = "";
        try {
            title = jsData.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return title;
    }

    public String getUrlImageVideo(){
        String url = "";
        try {
            url = jsData.getString("160x120");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  url;
    }

}
