package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2016/7/1.
 */
public class UINestStackOverflowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_overflow);
        ViewGroup target = (LinearLayout) findViewById(R.id.ll_root);
        for (int i = 0; i < 23; i++) {
            FrameLayout layout = new FrameLayout(this);
            target.addView(layout);
            target = layout;
        }
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(100, 100));
        imageView.setImageResource(R.drawable.bg_float_ball);
        target.addView(imageView);
    }
}
