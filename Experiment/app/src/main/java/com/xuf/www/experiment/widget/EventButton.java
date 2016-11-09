package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import com.xuf.www.experiment.util.ActionTransformatter;

/**
 * Created by dear33 on 2016/11/9.
 */
public class EventButton extends TextView {
    private static final String TAG = "event view";

    public EventButton(Context context) {
        super(context);
    }

    public EventButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent:" + ActionTransformatter.transformActionCode(ev.getAction()));
        boolean b = super.dispatchTouchEvent(ev);
        Log.i(TAG, "dispatchTouchEvent:" + " return:" + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onTouchEvent:" + ActionTransformatter.transformActionCode(ev.getAction()));
        boolean b;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            b = super.onTouchEvent(ev);
        } else {
            b = super.onTouchEvent(ev);
        }
        Log.i(TAG, "onTouchEvent:" + " return:" + b);
        return b;
    }
}
