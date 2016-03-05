package com.xuf.www.experiment.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by lenov0 on 2016/3/5.
 */
public class LocalService extends Service {

    private static final String TAG = "LocalService";
    private MyBinder myBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "LocalService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "LocalService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LocalService onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        myBinder = new MyBinder();
        return myBinder;
    }

    private class MyBinder extends Binder{

    }
}
