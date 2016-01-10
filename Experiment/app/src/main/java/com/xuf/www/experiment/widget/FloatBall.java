package com.xuf.www.experiment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2015/11/19.
 */
public class FloatBall extends FrameLayout {

    private FrameLayout mFrameLayout;

    public FloatBall(Context context) {
        this(context, null, 0);
    }

    public FloatBall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _initView(context);
    }

    private void _initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_float_ball, this);
        mFrameLayout = (FrameLayout)findViewById(R.id.fl_float_ball);
    }

    public int getBallWidth(){
        return mFrameLayout.getLayoutParams().width;
    }

    public int getBallHeight(){
        return mFrameLayout.getLayoutParams().height;
    }
}
