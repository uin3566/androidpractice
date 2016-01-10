package com.xuf.www.experiment.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xuf.www.experiment.widget.FloatBall;

/**
 * Created by Administrator on 2015/11/19.
 */
public class FloatBallManager {

    private Context mContext;

    private FloatBall mFloatBall;

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams mLayoutParams;

    private boolean mBallIsShowing = false;

    public FloatBallManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        _createFloatBall();
        _generateDefaultParams();
    }

    private void _createFloatBall(){
        mFloatBall = new FloatBall(mContext);
    }

    private void _generateDefaultParams(){
        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_PHONE
                , WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.width = mFloatBall.getBallWidth();
        mLayoutParams.height = mFloatBall.getBallHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mLayoutParams.x = metrics.widthPixels;
        mLayoutParams.y = metrics.heightPixels / 2;
    }

    public void addFloatBall(){
        mWindowManager.addView(mFloatBall, mLayoutParams);
        mBallIsShowing = true;
    }

    public void removeFloatBall(){
        mWindowManager.removeView(mFloatBall);
        mBallIsShowing = false;
    }

    public boolean ballIsShowing(){
        return mBallIsShowing;
    }
}
