package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.xuf.www.experiment.util.ActionTransformatter;

/**
 * Created by dear33 on 2016/11/9.
 */
public class EventRelativeLayout extends RelativeLayout {
    private static final String TAG = "event viewGroup";

    public EventRelativeLayout(Context context) {
        super(context);
    }

    public EventRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent begin-----------------------------------");
        Log.i(TAG, "dispatchTouchEvent:" + ActionTransformatter.transformActionCode(ev.getAction()));
        boolean b = super.dispatchTouchEvent(ev);
        Log.i(TAG, "dispatchTouchEvent:" + " return:" + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent:" + ActionTransformatter.transformActionCode(ev.getAction()));
        boolean b;
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            b = super.onInterceptTouchEvent(ev);
        } else {
            b = super.onInterceptTouchEvent(ev);
        }
        Log.i(TAG, "onInterceptTouchEvent:" + " return:" + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onTouchEvent:" + ActionTransformatter.transformActionCode(ev.getAction()));
        boolean b;
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            b = super.onInterceptTouchEvent(ev);
        } else {
            b = super.onInterceptTouchEvent(ev);
        }
        Log.i(TAG, "onTouchEvent:" + " return:" + b);
        return b;
    }
}
