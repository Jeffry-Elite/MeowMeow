package com.mobile.elite.meowmeow.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.kidzie.jeff.restclientmanager.Logging;
import com.kidzie.jeff.restclientmanager.task.TaskConnection;
import com.mobile.elite.meowmeow.utility.Config;
import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.screen.VideoDetail;
import com.mobile.elite.meowmeow.adapter.video.GridVideoAdapter;
import com.mobile.elite.meowmeow.listener.ImageClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class VideoFragment extends Fragment implements TaskConnection.TaskConnectionListener,ImageClickListener, AbsListView.OnScrollListener {

    private GridView gridView;
    private GridVideoAdapter gridAdapter;
    private boolean isLoadMore = true;
    private  int mPage = 1;
    private  int limit = 20;
    private ProgressBar loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_video_layout,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        gridView = (GridView)getActivity().findViewById(R.id.grid_image);
        loading = (ProgressBar)getActivity().findViewById(R.id.loading);
        gridAdapter = new GridVideoAdapter(getActivity(), new JSONArray(),this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnScrollListener(this);
        requestVideoApi();
    }

    private void requestVideoApi() {
        TaskConnection taskConnection = new TaskConnection(getActivity());
        taskConnection.setUrl(Config.videoUrl.replace("{page}","" +mPage));
        taskConnection.setRequest("");
        taskConnection.setTaskConnectionListener(this);
        taskConnection.executeRequest();
    }

    @Override
    public void onPreExecute() {
       if(!isLoadingShowing()){
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskConnectionFailedWithErrorCode(Object tag, int responseCode) {
        if(isLoadingShowing()){
            loading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTaskRequestSuccess(Object tag, JSONObject response) {
        if(isLoadingShowing()){
            loading.setVisibility(View.INVISIBLE);
        }
        try {
            if(response.getJSONArray("data").length() != 0)
                gridAdapter.add(response.getJSONArray("data"));
            else {
                isLoadMore = false;
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskRequestFailed(Object tag, JSONObject response) {
        if(isLoadingShowing()){
            loading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onImageClick(JSONObject jsData, JSONArray jsArray, int position) {
        Intent intent = new Intent(getActivity(), VideoDetail.class);
        intent.putExtra("data", jsArray.toString());
        intent.putExtra("position", position);
        getActivity().startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(isLoadMore){
            Logging.setLog(Logging.LOGTYPE.debug,"scroll","firstItem=" + firstVisibleItem,null);
            Logging.setLog(Logging.LOGTYPE.debug,"scroll","visibleItem=" + visibleItemCount,null);
            Logging.setLog(Logging.LOGTYPE.debug,"scroll","totalItemCount=" + totalItemCount,null);
            if((mPage * limit) - visibleItemCount < visibleItemCount + firstVisibleItem ){

                mPage++;
                requestVideoApi();
            }
        }
    }

    private boolean isLoadingShowing(){
        return loading.getVisibility() == View.VISIBLE;
    }
}
