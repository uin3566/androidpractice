package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.ToastUtil;

/**
 * Created by Administrator on 2016/2/26.
 */
public class GestureDetectorLayout extends LinearLayout {

    private GestureDetector mDetector;
    private Context mContext;

    public GestureDetectorLayout(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_gesture_detector, this);
        mDetector = new GestureDetector(context, new MySimpleGestureDetector());
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private class MySimpleGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            ToastUtil.showShort(mContext, "onDown");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            ToastUtil.showShort(mContext, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ToastUtil.showShort(mContext, "onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
