package com.mobile.elite.meowmeow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Jeffry on 04-Jun-15.
 */
public class CustomVideoView extends VideoView {

    private int height = 0;
    private int width = 0;

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context) {
        super(context);
    }

    //Set video height and width
    public void setDimensionVideo(int height, int width){
        this.height = height;
        this.width = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }
}
