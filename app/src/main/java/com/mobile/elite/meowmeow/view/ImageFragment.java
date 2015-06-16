package com.mobile.elite.meowmeow.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.kidzie.jeff.restclientmanager.task.TaskConnection;
import com.mobile.elite.meowmeow.utility.Config;
import com.mobile.elite.meowmeow.screen.ImageDetail;
import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.adapter.image.ListImageAdapter;
import com.mobile.elite.meowmeow.listener.ImageClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class ImageFragment extends Fragment implements ImageClickListener, TaskConnection.TaskConnectionListener, AbsListView.OnScrollListener {

    private  ListView listView;
    private  ListImageAdapter listImageAdapter;
    private Context context;
    private int mPage = 1;
    private boolean isLoadMore = true;
    private int limit = 20;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_image_layout,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        listView = (ListView)getActivity().findViewById(R.id.list_picture);
        listImageAdapter = new ListImageAdapter(getActivity(), new JSONArray(), this);
        listView.setAdapter(listImageAdapter);
        listView.setOnScrollListener(this);
        requestImageAPI();

    }

    /**
     * Request Image API, call this method on first time create Activity
     */
    private void requestImageAPI() {
        TaskConnection taskConnection = new TaskConnection(getActivity());
        taskConnection.setUrl(Config.imageUrl.replace("{page}", "" +mPage));
        taskConnection.setRequest("");
        taskConnection.setTaskConnectionListener(this);
        taskConnection.executeRequest();
    }


    @Override
    public void onImageClick(JSONObject jsData,JSONArray jsonArray, int position) {
        Intent intent = new Intent(getActivity(),ImageDetail.class);
        intent.putExtra("position",position);
        intent.putExtra("data", jsonArray.toString());
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onTaskConnectionFailedWithErrorCode(Object tag, int responseCode) {

    }

    @Override
    public void onTaskRequestSuccess(Object tag, JSONObject response) {

        try {
            listImageAdapter.add(response.getJSONArray("data"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskRequestFailed(Object tag, JSONObject response) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(isLoadMore){
            if((mPage * limit) - visibleItemCount < visibleItemCount + firstVisibleItem ){
                mPage++;
                requestImageAPI();
            }
        }
    }
}
