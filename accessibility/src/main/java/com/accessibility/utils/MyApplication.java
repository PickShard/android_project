package com.accessibility.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by sc on 2017/6/20.
 */

public class MyApplication extends Application {

    private static Context myApplication;
    private static boolean enabled = true;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

    }

    public static Context getMyApplication(){
        return myApplication;
    }


    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        MyApplication.enabled = enabled;
    }
}
