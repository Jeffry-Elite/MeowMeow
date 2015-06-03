package com.kidzie.jeff.restclientmanager.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.kidzie.jeff.restclientmanager.Logging;
import com.kidzie.jeff.restclientmanager.Logging.LOGTYPE;
import com.kidzie.jeff.restclientmanager.config.Global;
import com.kidzie.jeff.restclientmanager.model.CustomHttpResponse;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */
public abstract class BaseTask extends AsyncTask<Void, Void, JSONObject> {

    public abstract void executeRequest();

    private String mUrl;
    private Object mRequest;
    private String mRequestMethod;
    private Calendar mCalDevice;
    private Context mContext;
    private Object mTag;
    private String header = "";
    private boolean needAuthorization = false;
    private String authorizationValue = "";
    private int mResponseCode;
    private boolean isNeedCache=false;

    private String headerKey = "";
    private String headerValue = "";
    private HashMap<String, String> headerMap = new HashMap<String, String>();


    public void setNeedCache(boolean isNeedCache) {
        this.isNeedCache = isNeedCache;
    }
    protected void setUrl(String url) { mUrl = url; }
    protected void setRequestMethod(String requestMethod) { mRequestMethod = requestMethod; }
    protected void setRequest(Object request) { mRequest = request; }
    protected void setCalDevice(Calendar calDevice) { mCalDevice = calDevice; }
    protected void setHeader(String header){
        this.header = header;
    }
    protected void setNeedAuthorization(boolean needAuthorization) {
        this.needAuthorization = needAuthorization;
    }
    protected void setAuthorizationValue(String authorizationValue) {
        this.authorizationValue = authorizationValue;
    }

    protected void setHeaderMap(HashMap<String, String> headerMap){
        this.headerMap = headerMap;

    }
    protected int getResponseCode() { return mResponseCode; }
    public void setTag(Object tag) { mTag = tag; }
    public Object getTag() { return mTag; }

    public BaseTask(Context context) {
        mContext = context;
    }

    protected void mapToJSONString(HashMap<String, ?> map) {
        JSONObject jsonRequest = new JSONObject(map);

        mRequest = jsonRequest.toString();
    }

    protected JSONObject requestToService() {
        if (mRequest == null) {
            Logging.setLog(LOGTYPE.warn,Global.TAG,"REQUEST IS EMPTY",null);
        }

        if (!isHasConnection()) {
            mResponseCode = 600;

            return null;
        }

        try {
            HttpService service = new HttpService();
            if(headerMap.size() > 0){
                service.setHeaderMap(headerMap);
            }
            CustomHttpResponse response = null;
            if(isNeedCache){
                response = service.connectToServiceWithCache(header, mUrl, mRequest, mRequestMethod, mCalDevice , isNeedCache);
            }
            else{
                if(headerMap.size() != 0){
                    service.setHeaderMap(headerMap);
                }
                response = service.connectToServiceWithHeader(header,mUrl, mRequest, mRequestMethod, mCalDevice,needAuthorization,authorizationValue);
            }
            Logging.setLog(LOGTYPE.debug,Global.TAG,
                    "URL: " + mUrl + ", RESPONSE CODE: " + response.getResponseCode() + ", RESPONSE: " + response.getResponseBody(),null);

            mResponseCode = response.getResponseCode();

            String responseBody = response.getResponseBody();
            if(responseBody == null){
                responseBody = "{}";
            }
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
        catch (Throwable th) {
            Log.e("ABC", "BASETASK th: " + th.getMessage());
            th.printStackTrace();
            try {
                System.gc();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("ABC", "BASETASK2 ex: "+ ex.getMessage());
            }

            return null;
        }

    }

    public boolean isHasConnection() {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        }

        return false;
    }

}
