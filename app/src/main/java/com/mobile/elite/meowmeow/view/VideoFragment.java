package com.mobile.elite.meowmeow.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.kidzie.jeff.restclientmanager.task.TaskConnection;
import com.mobile.elite.meowmeow.Config;
import com.mobile.elite.meowmeow.R;
import com.mobile.elite.meowmeow.adapter.GridVideoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class VideoFragment extends Fragment implements TaskConnection.TaskConnectionListener {

    private GridView gridView;
    private GridVideoAdapter gridAdapter;

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
        gridAdapter = new GridVideoAdapter(getActivity(), new JSONArray());
        gridView.setAdapter(gridAdapter);
        requestVideoApi();
    }

    private void requestVideoApi() {
        TaskConnection taskConnection = new TaskConnection(getActivity());
        taskConnection.setUrl(Config.videoUrl);
        taskConnection.setRequest("");
        taskConnection.setTaskConnectionListener(this);
        taskConnection.executeRequest();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onTaskConnectionFailedWithErrorCode(Object tag, int responseCode) {

    }

    @Override
    public void onTaskRequestSuccess(Object tag, JSONObject response) {
        gridAdapter.clear();
        try {
            gridAdapter.add(response.getJSONArray("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskRequestFailed(Object tag, JSONObject response) {

    }
}
