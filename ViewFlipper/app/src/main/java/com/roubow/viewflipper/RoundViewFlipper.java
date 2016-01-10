package com.roubow.viewflipper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * Created by Administrator on 2015/8/21.
 */
public class RoundViewFlipper extends ViewFlipper {

    private float corner = 0.0f;
    private PaintFlagsDrawFilter filter;
    private Path mRoundPath;

    public RoundViewFlipper(Context context) {
        this(context, null);
    }

    public RoundViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundViewFlipper);
        if (a.hasValue(R.styleable.RoundViewFlipper_corner)){
            corner = a.getDimension(R.styleable.RoundViewFlipper_corner, 0f);
        }
        a.recycle();
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        //RoundViewFlipper继承于FrameLayout，默认不调用onDraw
        setWillNotDraw(false);
    }

    private void _setPath(){
        mRoundPath = new Path();
        if (corner == 0){
            return;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float s = 2 * corner;
        RectF rectTopLeft = new RectF(0, 0, s, s);
        RectF rectTopRight = new RectF(width-s, 0, width, s);
        RectF rectBottomLeft = new RectF(0, height-s, s, height);
        RectF rectBottomRight = new RectF(width-s, height-s, width, height);

        mRoundPath.arcTo(rectTopLeft, 180, 90);
        mRoundPath.moveTo(rectTopLeft.left + s / 2, rectTopLeft.top);

        mRoundPath.lineTo(rectTopRight.right - s / 2, rectTopRight.top);
        mRoundPath.arcTo(rectTopRight, 270, 90);

        mRoundPath.lineTo(rectBottomRight.right, rectBottomRight.bottom - s /2);
        mRoundPath.arcTo(rectBottomRight, 0, 90);

        mRoundPath.lineTo(rectBottomLeft.left + s / 2, rectBottomLeft.bottom);
        mRoundPath.arcTo(rectBottomLeft, 90, 90);

        mRoundPath.lineTo(rectBottomLeft.left, rectTopLeft.top + s / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mRoundPath){
            _setPath();
        }

        if (!mRoundPath.isEmpty()) {
            canvas.setDrawFilter(filter);
            canvas.clipPath(mRoundPath);
        }

        super.onDraw(canvas);
    }
}
