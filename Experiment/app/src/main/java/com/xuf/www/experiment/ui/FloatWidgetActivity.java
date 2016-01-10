package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.service.FloatWidgetService;

/**
 * Created by Administrator on 2015/11/19.
 */
public class FloatWidgetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Intent intent = new Intent(this, FloatWidgetService.class);
        startService(intent);
    }
}
