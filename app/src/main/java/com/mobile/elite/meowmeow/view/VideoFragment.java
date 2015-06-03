package com.mobile.elite.meowmeow.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.elite.meowmeow.R;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class VideoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_video_layout,null);
    }
}
