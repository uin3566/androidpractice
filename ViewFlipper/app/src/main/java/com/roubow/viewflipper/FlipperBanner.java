package com.roubow.viewflipper;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * Created by xf on 2015/8/20.
 */
public class FlipperBanner extends FrameLayout implements  View.OnClickListener, View.OnTouchListener{

    private Context mContext;
    private LinearLayout mDotContainer;
    private RoundViewFlipper mViewFlipper;
    private float startX;
    private float endX;
    private int mLastIndex;
    private int mCurIndex;
    private int mBannerCount;
    private long mStartPressTime;
    private long mEndPressTime;

    private float startY;
    private float curX;
    private float curY;
    private float absDx;
    private float absDy;

    private Runnable mAutoSwitchRunnable = new Runnable() {
        @Override
        public void run() {
            mViewFlipper.setInAnimation(mContext, R.anim.in_right_left);
            mViewFlipper.setOutAnimation(mContext, R.anim.out_right_left);
            mViewFlipper.showNext();
            mLastIndex = mCurIndex;
            mCurIndex++;
            if (mCurIndex == mBannerCount){
                mCurIndex = 0;
            }
            if (mLastIndex == mBannerCount){
                mLastIndex = 0;
            }
            _setSelectedIndicator();
            postDelayed(this, 3000);
        }
    };

    public FlipperBanner(Context context) {
        super(context);
        mContext = context;
        _init();
    }

    public FlipperBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        _init();
    }

    private void _init(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_flipper_banner, this);
        mViewFlipper = (RoundViewFlipper)view.findViewById(R.id.vf_banner);
        mDotContainer = (LinearLayout)view.findViewById(R.id.ll_dot_container);
        setOnTouchListener(this);
        setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartPressTime = System.currentTimeMillis();
                _stopAutoSwitch();
                startX = event.getX();
                startY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                absDx = Math.abs(curX - startX);
                absDy = Math.abs(curY - startY);
                if (absDx < absDy){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                mEndPressTime = System.currentTimeMillis();
                _startAutoSwitch();
                endX = event.getX();
                if (endX - startX > 100){//right
                    mViewFlipper.setInAnimation(mContext, R.anim.in_left_right);
                    mViewFlipper.setOutAnimation(mContext, R.anim.out_left_right);
                    mViewFlipper.showPrevious();
                    mLastIndex = mCurIndex;
                    mCurIndex--;
                    if (mCurIndex == -1){
                        mCurIndex = mBannerCount - 1;
                    }
                    _setSelectedIndicator();
                } else if (endX - startX< -100){//left
                    mViewFlipper.setInAnimation(mContext, R.anim.in_right_left);
                    mViewFlipper.setOutAnimation(mContext, R.anim.out_right_left);
                    mViewFlipper.showNext();
                    mLastIndex = mCurIndex;
                    mCurIndex++;
                    if (mCurIndex == mBannerCount){
                        mCurIndex = 0;
                }
                    _setSelectedIndicator();
                }
                break;
        }

        if (mEndPressTime - mStartPressTime > 1000 || Math.abs(endX - startX) > 100){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "click:" + mCurIndex, Toast.LENGTH_SHORT).show();
    }

    public void setBanners(List<View> bannerViews){
        for (int i = 0; i < bannerViews.size(); i++){
            View view = bannerViews.get(i);
            mViewFlipper.addView(view, i);
        }
        mBannerCount = mViewFlipper.getChildCount();
        mLastIndex = 0;
        mCurIndex = 0;
        _addDots();
    }

    private void _addDots(){
        for (int i = 0; i < mBannerCount; i++){
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.dot_normal);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(_dp2px(mContext, 5), _dp2px(mContext, 5));
            lp.setMargins(_dp2px(mContext, 2.5f), 0, _dp2px(mContext, 2.5f), 0);
            imageView.setLayoutParams(lp);
            mDotContainer.addView(imageView, i);
        }
        _setSelectedIndicator();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        _startAutoSwitch();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        _stopAutoSwitch();
    }

    private void _stopAutoSwitch(){
        removeCallbacks(mAutoSwitchRunnable);
    }

    private void _startAutoSwitch(){
        removeCallbacks(mAutoSwitchRunnable);
        postDelayed(mAutoSwitchRunnable, 3000);
    }

    private void _setSelectedIndicator(){
        ((ImageView)mDotContainer.getChildAt(mLastIndex)).setImageResource(R.drawable.dot_normal);
        ((ImageView)mDotContainer.getChildAt(mCurIndex)).setImageResource(R.drawable.dot_selected);
    }

    private int _dp2px(Context context, float dp)
    {
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                        .getDisplayMetrics()));
        return px;
    }
}
