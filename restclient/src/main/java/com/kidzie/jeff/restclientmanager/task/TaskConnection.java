package com.kidzie.jeff.restclientmanager.task;

import android.content.Context;

import com.kidzie.jeff.restclientmanager.config.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */
public class TaskConnection extends BaseTask {

    private TaskConnectionListener mListener = null;

    public String mUrl;
    public String mRequest;

    private boolean isUsePairKey;
    private Calendar mCalDevice;
    private String requestMethod = Global.HTTP_POST;
    private String contentType = Global.HEADER_CONTENT_TYPE;
    private String mHeader = "";
    private String authorizationValue = "";
    private boolean needAuthorization = false;
    private boolean needCacheControl = false;

    private HashMap<String, String> headerMap;
    private Object tag = null;


    public void setNeedCacheControl(boolean needCacheControl) {
        super.setNeedCache(needCacheControl);
        this.needCacheControl = needCacheControl;
    }


    public TaskConnection(Context context) {
        super(context);
    }

    public void setHeaderMap(HashMap<String, String> headerMap){
        this.headerMap = headerMap;
        super.setHeaderMap(headerMap);
    }


    public void setNeedAuthorization(boolean needAuthorization) {
        super.setNeedAuthorization(needAuthorization);
        this.needAuthorization = needAuthorization;
    }


    public void setHeader(String header) {
        super.setHeader(header);
        mHeader = header;
    }

    public void setAuthorizationValue(String authorizationValue) {
        super.setAuthorizationValue(authorizationValue);
        this.authorizationValue = authorizationValue;
    }

    public void setUrl(String url) {
        super.setUrl(url);

        mUrl = url;
    }

    public void setIsUsePairKey(boolean isUsePairKey) {
        this.isUsePairKey = isUsePairKey;
    }

    public void setCalDevice(Calendar calDevice) {
        super.setCalDevice(calDevice);
        mCalDevice = calDevice;
    }

    public Calendar getCalDevice() {
        return mCalDevice;
    }


    public void setRequest(String request) {
        super.setRequest(request);

        mRequest = request;
    }


    public TaskConnectionListener getTaskConnectionListener(){
        return mListener;
    }

    public void setTag(Object tag){
        this.tag = tag;
    }

    public String getHeader() {
        return mHeader;
    }

    public String getUrl() { return mUrl; }
    public String getRequest() { return mRequest; }

    @Override
    protected void onPreExecute() {
        if (mListener != null) mListener.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        int responseCode = super.getResponseCode();

        if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
            if(response == null)
                response = new JSONObject();
            try {
                response.put("response_code", responseCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mListener != null) mListener.onTaskRequestSuccess(getTag(), response);

        }else if(responseCode == 400 || responseCode == 401 || responseCode == 403 || responseCode == 409 ){
            if(mListener != null){
                try {
                    mListener.onTaskRequestFailed(getTag(), response.put("response_code", responseCode));
                } catch (JSONException e) {
                    mListener.onTaskRequestFailed(getTag(), response);
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (mListener != null) mListener.onTaskConnectionFailedWithErrorCode(getTag(), super.getResponseCode());
        }
    }

    public boolean isNeedAuthorization() {
        return needAuthorization;
    }

    public String getRequestMethod() {
        return requestMethod;
    }


    public Object getTag() {
        return tag;
    }


    public void setRequestMethod(String requestMethod){
        this.requestMethod = requestMethod;
    }

    public String getAuthorizationValue() {
        return authorizationValue;
    }

    public HashMap<String, String> getHeaderMap(){
        return headerMap;
    }


    @Override
    public void executeRequest() {
        super.setNeedAuthorization(needAuthorization);
        super.setRequestMethod(requestMethod);
        super.setAuthorizationValue(authorizationValue);
        super.setHeader(mHeader);
        super.setRequest(mRequest);
        super.setUrl(mUrl);
        if(tag != null)
            super.setTag(tag);
        this.execute();
    }

    @Override
    protected JSONObject doInBackground(Void... arg0) {
        return super.requestToService();
    }

    public void setTaskConnectionListener(TaskConnectionListener listener) {
        mListener = listener;
    }

    public interface TaskConnectionListener {
        public void onPreExecute();
        public void onTaskConnectionFailedWithErrorCode(Object tag, int responseCode);
        public void onTaskRequestSuccess(Object tag, JSONObject response);
        public void onTaskRequestFailed(Object tag, JSONObject response);
    }

}