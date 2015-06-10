package com.mobile.elite.meowmeow.listener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public interface ImageClickListener {
    void onImageClick(JSONObject jsData, JSONArray jsArray, int position);
}
