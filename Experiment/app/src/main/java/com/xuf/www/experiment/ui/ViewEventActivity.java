package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.widget.EventButton;
import com.xuf.www.experiment.widget.EventRelativeLayout;

/**
 * Created by dear33 on 2016/11/9.
 */
public class ViewEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        EventRelativeLayout layout = (EventRelativeLayout) findViewById(R.id.rl);
        EventButton button = (EventButton) findViewById(R.id.btn);
    }
}
