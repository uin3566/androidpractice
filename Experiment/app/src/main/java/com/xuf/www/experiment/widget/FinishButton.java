package com.xuf.www.experiment.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.DimenUtil;

/**
 * Created by Xuf on 2015/10/8.
 */
public class FinishButton extends TextView {

    private int mTotalCount = 0;

    public FinishButton(Context context, int totalCount) {
        this(context, null);
        mTotalCount = totalCount;
    }

    public FinishButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateButton(0);
    }

    public void updateButton(int curCount){
        if (curCount == 0){
            setText("完成");
            setTextColor(Color.parseColor("#99ba91"));
            setEnabled(false);
            setBackgroundResource(R.drawable.shape_button_disabled);
        } else {
            if (curCount <= mTotalCount) {
                String text = String.format("完成(%d/%d)", curCount, mTotalCount);
                setText(text);
                setTextColor(Color.parseColor("#ffffff"));
                setEnabled(true);
                setBackgroundResource(R.drawable.shape_button_enabled);
            }
        }
    }
}
