package com.mobile.elite.meowmeow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobile.elite.meowmeow.view.ImageFragment;
import com.mobile.elite.meowmeow.view.VideoFragment;

/**
 * Created by Jeffry on 03-Jun-15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new VideoFragment();

            case 1 : return  new ImageFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
