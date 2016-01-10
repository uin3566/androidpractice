package com.roubow.viewflipper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.IllegalFormatCodePointException;

/**
 * Created by Administrator on 2015/8/24.
 */
//自定义的listView，适用于包含水平滑动item的情况。
public class HorizontalScrollListView extends ListView {
    private float mDX, mDY, mLX, mLY;
    int mLastAct = -1;
    boolean mIntercept = false;

    public HorizontalScrollListView(Context context) {
        super(context);
    }

    public HorizontalScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*默认拦截*/
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            return false;
        } else {
            return true;
        }




        /*
        当用户滑动时，判断滑动方向，若大致水平，则返回false，不拦截TouchEvent，交给子View处理，
        当子View如ViewFlipper,HorizontalScrollView时，能比较灵敏的响应横向滑动事件。
        默认情况下，不严格的横向滑动事件将会被ListView处理。
        */
        /*switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                mDX = mDY = 0f;
                mLX = ev.getX();
                mLY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE :
                final float curX = ev.getX();
                final float curY = ev.getY();
                mDX += Math.abs(curX - mLX);
                mDY += Math.abs(curY - mLY);
                mLX = curX;
                mLY = curY;

                if (mIntercept && mLastAct == MotionEvent.ACTION_MOVE) {
                    return false;
                }

                if (mDX > mDY) {

                    mIntercept = true;
                    mLastAct = MotionEvent.ACTION_MOVE;
                    return false;
                }

        }
        mLastAct = ev.getAction();
        mIntercept = false;
        */
    }
}
