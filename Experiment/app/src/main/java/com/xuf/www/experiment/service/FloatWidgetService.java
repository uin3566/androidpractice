package com.xuf.www.experiment.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import com.xuf.www.experiment.util.FloatBallManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.PasswordCallback;

/**
 * Created by Administrator on 2015/11/19.
 */
public class FloatWidgetService extends Service {

    private FloatBallManager mFloatBallManager;

    private Timer mTimer;

    private Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFloatBallManager = new FloatBallManager(this);
        if (mTimer == null){
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new CheckDesktopTask(), 0, 500);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer = null;
    }

    private class CheckDesktopTask extends TimerTask{
        @Override
        public void run() {
            if (_isDesktop() && mFloatBallManager.ballIsShowing()){
                //todo,update memory info
            }
            if (_isDesktop() && !mFloatBallManager.ballIsShowing()){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatBallManager.addFloatBall();
                    }
                });
            }
            if (!_isDesktop() && mFloatBallManager.ballIsShowing()){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatBallManager.removeFloatBall();
                    }
                });
            }
        }
    }

    private boolean _isDesktop(){
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(1);
        return getHomes().contains(taskInfoList.get(0).topActivity.getPackageName());
    }

    private List<String> getHomes(){
        List<String> names = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfoList){
            names.add(info.activityInfo.packageName);
        }

        return names;
    }
}
