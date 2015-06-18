package com.mobile.elite.meowmeow.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffry on 11-Jun-15.
 */
public class ImageDataParser {

    private JSONObject jsData;

    public JSONObject getJsData() {
        return jsData;
    }

    public void setJsData(JSONObject jsData) {
        this.jsData = jsData;
    }

    public String getUrl(){
        String url = "";
        try {
            url = getJsData().getString("actual_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
}
