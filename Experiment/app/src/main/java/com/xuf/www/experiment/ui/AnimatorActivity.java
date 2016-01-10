package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.xuf.www.experiment.widget.AnimatorView;

/**
 * Created by Administrator on 2015/11/30.
 */
public class AnimatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AnimatorView(this));
    }
}
