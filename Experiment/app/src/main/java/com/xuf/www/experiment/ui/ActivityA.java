package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ActivityA extends BaseTaskActivity {

    private static final String TAG = "ActivityA";

    @Override
    public void jumpClick(Button jump) {
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityA.this, ActivityB.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setLabel(TextView label) {
        label.setText(TAG);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }
}
