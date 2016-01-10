package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by lenov0 on 2015/11/17.
 */
public class DragTextView extends TextView {

    private int mLastX = 0;
    private int mLastY = 0;

    public DragTextView(Context context) {
        this(context, null, 0);
    }

    public DragTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setOnTouchListener(new MyTouchListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                dragByAnim(deltaX, deltaY);
                //dragByLayoutParams(deltaX, deltaY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    //通过动画的方式拖动
    private void dragByAnim(int deltaX, int deltaY){
        int translateX = (int)ViewHelper.getTranslationX(this) + deltaX;
        int translateY = (int)ViewHelper.getTranslationY(this) + deltaY;
        ViewHelper.setTranslationX(this, translateX);
        ViewHelper.setTranslationY(this, translateY);
    }

    //通过改变布局参数的方式拖动
    private void dragByLayoutParams(int deltaX, int deltaY){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)getLayoutParams();
        params.leftMargin += deltaX;
        params.topMargin += deltaY;
        requestLayout();
    }

    private class MyTouchListener implements OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int x = (int)event.getRawX();
            int y = (int)event.getRawY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaX = x - mLastX;
                    int deltaY = y - mLastY;
                    dragByAnim(deltaX, deltaY);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            mLastX = x;
            mLastY = y;
            return true;
        }
    }
}
