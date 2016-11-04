package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.util.MyApplication;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ActivityTaskActivity extends BaseTaskActivity {

    private static final String TAG = "ActivityTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().createAc(this);
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().destroyAc(this);
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void jumpClick(Button jump) {
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTaskActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                MyApplication.getInstance().finishActivityButMainAndThis(ActivityTaskActivity.this);
            }
        });
    }

    @Override
    public void setLabel(TextView label) {
        label.setText(TAG);
    }
}
