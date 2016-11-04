package com.xuf.www.experiment.util;

import android.app.Activity;
import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xuf.www.experiment.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/11/1.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    private List<Activity> acList = new ArrayList<>();

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void finishActivityButMainAndThis(Activity target) {
        for (Activity activity : acList) {
            if (activity instanceof MainActivity || target == activity) {
                continue;
            }
            activity.finish();
        }
    }

    public void createAc(Activity ac) {
        acList.add(ac);
    }

    public void destroyAc(Activity ac) {
        acList.remove(ac);
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
