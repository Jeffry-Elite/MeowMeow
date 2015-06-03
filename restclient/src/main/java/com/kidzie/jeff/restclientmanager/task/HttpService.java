package com.kidzie.jeff.restclientmanager.task;

/**
 * Created by ${Jeffry} on 03-Jun-15.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kidzie.jeff.restclientmanager.Logging;
import com.kidzie.jeff.restclientmanager.Logging.LOGTYPE;
import com.kidzie.jeff.restclientmanager.config.Global;
import com.kidzie.jeff.restclientmanager.model.CustomHttpResponse;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpService {
    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);

        }

        public MySSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
            super(null);

            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }

    }

    private Context context;

    private HashMap<String , String> headerMap = new HashMap<String, String>();
    public void setHeaderMap(HashMap<String, String> headerMap){
        this.headerMap = headerMap;
    }


    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }

    }

    public static final String TAG = "HttpService";

    public CustomHttpResponse connectToServiceWithHTTPClient(String headerValue,String url, Object request, String httpMethod,
                                                             Calendar calDevice,boolean needAuthorization, String authorizationValue,boolean isNeedCacheControl) {
        HttpClient httpclient;

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Global.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, Global.CONNECTION_TIMEOUT);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);

            httpclient = new DefaultHttpClient(ccm, httpParameters);
        } catch (Exception e) {
            e.printStackTrace();

            httpclient = new DefaultHttpClient(httpParameters);
        }

        HttpRequestBase http = null;
        String value = headerValue.equals("") ? Global.TYPE_JSON + "; charset=UTF-8" : headerValue;

        if (httpMethod.trim() == Global.HTTP_POST) {
            http = new HttpPost(url);

        }
        else if (httpMethod.trim() == Global.HTTP_PUT){
            http = new HttpPut(url);
            http.setHeader(Global.HEADER_CONTENT_TYPE, value);
        }
        else {
            http = new HttpGet(url);
            http.setHeader(Global.HEADER_ACCEPT, Global.TYPE_JSON + "; charset-UTF-8");
        }
        if(needAuthorization){
            http.setHeader(Global.HEADER_AUTHORIZATION, authorizationValue);

        }
        if(isNeedCacheControl){
            http.setHeader(Global.HEADER_CACHE_CONTROL, Global.HEADER_CACHE_VALUE);
        }

        if(headerMap.size() > 0){
            for(Map.Entry<String, String> map : headerMap.entrySet()){
                http.setHeader(map.getKey(), map.getValue());
            }
        }

        CustomHttpResponse customResponse = new CustomHttpResponse();

        try {

            if (httpMethod == Global.HTTP_POST || httpMethod == Global.HTTP_PUT) {
                ((HttpEntityEnclosingRequest) http).setEntity(new StringEntity(request.toString(),HTTP.UTF_8));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {

            HttpResponse response = httpclient.execute((HttpUriRequest) http);

            int responseCode = response.getStatusLine().getStatusCode();
            if(responseCode == 204){
                httpclient.getConnectionManager().shutdown();
                customResponse.setResponseCode(204);
                return customResponse;
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            httpclient.getConnectionManager().shutdown();

            if (responseCode == 200 || responseCode == 201 || responseCode == 401 || responseCode == 409) {
                customResponse.setResponseCode(responseCode);
                customResponse.setResponseBody(responseBody);

                return customResponse;
            } else {
                customResponse.setResponseCode(responseCode);
                customResponse.setResponseBody(responseBody);

                return customResponse;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();

            httpclient.getConnectionManager().shutdown();

            customResponse.setResponseCode(300);

            return customResponse;

        } catch (NoHttpResponseException e) {
            e.printStackTrace();

            httpclient.getConnectionManager().shutdown();

            customResponse.setResponseCode(409);

            return customResponse;
        } catch (ConnectionPoolTimeoutException e) {
            e.printStackTrace();

            httpclient.getConnectionManager().shutdown();

            customResponse.setResponseCode(406);

            return customResponse;
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();

            httpclient.getConnectionManager().shutdown();

            customResponse.setResponseCode(407);

            return customResponse;
        } catch (IOException e) {
            e.printStackTrace();

            httpclient.getConnectionManager().shutdown();
            customResponse.setResponseCode(408);

            return customResponse;
        } catch (Exception e) {
            e.printStackTrace();
            httpclient.getConnectionManager().shutdown();

            customResponse.setResponseCode(408);

            return customResponse;
        }
    }

    public CustomHttpResponse connectToService(String url, Object request, String requestMethod, Calendar calDevice) {

        Logging.setLog(LOGTYPE.info,Global.TAG,"URL: " + url,null);
        Logging.setLog(LOGTYPE.info, Global.TAG, "PARAMS: " + request.toString(), null);

        return connectToServiceWithHTTPClient("",url, request, requestMethod, calDevice,false,"",false);

    }

    public CustomHttpResponse connectToServiceWithHeader(String headerValue,String url, Object request, String requestMethod, Calendar calDevice,boolean needAuthorization,String authorizationValue){

        Logging.setLog(LOGTYPE.info,Global.TAG,"URL: " + url,null);
        Logging.setLog(LOGTYPE.info,Global.TAG,"PARAMS: " + request.toString(),null);

        return connectToServiceWithHTTPClient(headerValue, url, request, requestMethod, calDevice, needAuthorization,authorizationValue,false);
    }

    public CustomHttpResponse connectToServiceWithCache(String headerValue,String url, Object request, String requestMethod, Calendar calDevice,boolean needCacheControl){

        Logging.setLog(LOGTYPE.info, Global.TAG, "URL: " + url, null);
        Logging.setLog(LOGTYPE.info, Global.TAG, "PARAMS: " + request.toString(), null);

        return connectToServiceWithHTTPClient(headerValue, url, request, requestMethod, calDevice, false,"",needCacheControl);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                if (ni.isConnected()){
                    haveConnectedWifi = true;
                    Logging.setLog(LOGTYPE.info, "connect wifi", "connect wifi=" + haveConnectedWifi, null);
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if (ni.isConnected()){
                    haveConnectedMobile = true;
                    Logging.setLog(LOGTYPE.info, "connect wifi", "connect mobile=" + haveConnectedMobile, null);
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
