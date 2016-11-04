package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuf.www.experiment.R;

/**
 * Created by Administrator on 2016/9/8.
 */
public class GlideActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide1);
        Button jump = (Button) findViewById(R.id.btn_jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlideActivity1.this, GlideActivity2.class);
                startActivity(intent);
            }
        });
    }
}
