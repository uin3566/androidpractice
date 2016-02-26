package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.xuf.www.experiment.widget.GestureDetectorLayout;

/**
 * Created by Administrator on 2016/2/26.
 */
public class GestureDetectorActivity extends Activity {

    private GestureDetectorLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = new GestureDetectorLayout(this);
        setContentView(mContent);
    }
}
