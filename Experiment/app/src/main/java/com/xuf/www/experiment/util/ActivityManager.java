package com.xuf.www.experiment.util;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenov0 on 2015/9/20.
 */
public class ActivityManager {

    private static Map<String, Class<? extends Activity>> mMap = new HashMap<>();

    public static void add(String key, Class<? extends Activity> cls){
        mMap.put(key, cls);
    }

    public static Class<? extends Activity> get(String key){
        return mMap.get(key);
    }

}
