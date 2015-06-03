package com.kidzie.jeff.restclientmanager.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */

public class CustomHttpResponse  {

    private String mResponseBody;
    private int mResponseCode;
    private Context context;

    public CustomHttpResponse(Context context) {
        this.context = context;
    }

    public CustomHttpResponse() {
        super();
    }

    public void setResponseBody(String responseBody) { mResponseBody = responseBody; }
    public void setResponseCode(int responseCode) { mResponseCode = responseCode; }

    public String getResponseBody() { return mResponseBody; }
    public int getResponseCode() { return mResponseCode; }

    public JSONObject toJSONObject() {

        try {
            JSONObject object = new JSONObject();

            object.put("response_code", mResponseCode);
            object.put("response_body", mResponseBody);

            return object;
        } catch (JSONException e) {
            return null;
        }

    }

}

