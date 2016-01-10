package com.xuf.www.experiment.widget;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.AnimatorSet;
import com.xuf.www.experiment.bean.Point;

/**
 * Created by Administrator on 2015/11/30.
 */
public class AnimatorView extends View {

    private Point mCurrentPoint;
    private Paint mPaint;
    private int color;

    private static final String TAG = "AnimatorView";
    private static final int RADIUS = 50;
    private static final int COLOR = Color.YELLOW;

    public AnimatorView(Context context) {
        super(context);
        _init();
    }

    public AnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init();
    }

    public AnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentPoint == null){
            mCurrentPoint = new Point(getWidth() / 2, RADIUS);
            _drawCircle(canvas, mCurrentPoint);
            _startAnimation();
        } else {
            _drawCircle(canvas, mCurrentPoint);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        mPaint.setColor(color);
    }

    private void _init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        color = COLOR;
        mPaint.setColor(color);
    }

    private void _drawCircle(Canvas canvas, Point point){
        canvas.drawCircle(point.getX(), point.getY(), RADIUS, mPaint);
    }

    private void _startAnimation(){
        Point startPoint = new Point(getWidth() / 2, RADIUS);
        Point endPoint = new Point(getWidth() / 2, getHeight() - RADIUS);
        ValueAnimator animator1 = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentPoint = (Point)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator1.setInterpolator(new DecelerateAccelerateInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(), Color.YELLOW, Color.BLACK);
        AnimatorSet set = new AnimatorSet();
        set.play(animator1).with(animator2);
        set.setStartDelay(500);
        set.setDuration(3000);
        set.start();
    }

    private class PointEvaluator implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point)startValue;
            Point endPoint = (Point)endValue;
            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

            String log = "PointEvaluator: fraction = " + fraction + ", x = " + x + ", y = " + y;
            Log.d(TAG, log);

            return new Point(x, y);
        }
    }

    private class ColorEvaluator implements TypeEvaluator{
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            int startColor = (int)startValue;
            int endColor = (int)endValue;

            int alpha = (int)(Color.alpha(startColor) + fraction * (Color.alpha(endColor) - Color.alpha(startColor)));
            int red = (int)(Color.red(startColor) + fraction * (Color.red(endColor) - Color.red(startColor)));
            int green = (int)(Color.green(startColor) + fraction * (Color.green(endColor) - Color.green(startColor)));
            int blue = (int)(Color.blue(startColor) + fraction * (Color.blue(endColor) - Color.blue(startColor)));

            String log = "ColorEvaluator: fraction = " + fraction + ", red = " + red + ", green = " + green + ", blue = " + blue;
            Log.d(TAG, log);

            return Color.argb(alpha, red, green, blue);
        }
    }

    private class DecelerateAccelerateInterpolator implements Interpolator{
        @Override
        public float getInterpolation(float input) {
            float result;

            if (input <= 0.5f){
                result = (float)(Math.sin(input * Math.PI)) / 2;
            } else {
                result = (float)(2 - Math.sin(input * Math.PI)) / 2;
            }

            return result;
        }
    }
}
