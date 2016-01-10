package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2015/12/1.
 */
public abstract class BaseTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Button jump = (Button)findViewById(R.id.btn_jump);
        jumpClick(jump);

        TextView label = (TextView)findViewById(R.id.tv_activity_label);
        setLabel(label);
    }

    public abstract void jumpClick(Button jump);

    public abstract void setLabel(TextView label);
}
