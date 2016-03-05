package com.xuf.www.experiment.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xuf.www.experiment.R;
import com.xuf.www.experiment.service.LocalService;

/**
 * Created by lenov0 on 2016/3/5.
 */
public class LocalServiceActivity extends Activity {
    private static final String TAG = "LocalServiceActivity";
    private MyServiceConnection mConn;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_service);
        Button start = (Button) findViewById(R.id.btn_start);
        Button bind = (Button) findViewById(R.id.btn_bind);
        Button unBind = (Button) findViewById(R.id.btn_unbind);
        Button stop = (Button) findViewById(R.id.btn_stop);

        mIntent = new Intent(LocalServiceActivity.this, LocalService.class);
        mConn = new MyServiceConnection();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(mIntent);
            }
        });

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService(mIntent, mConn, BIND_AUTO_CREATE);
            }
        });

        unBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(mConn);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(mIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    }
}
