package com.accessibility.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by sc on 2017/6/20.
 */

public class MyApplication extends Application {

    private static Context myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

    }

    public static Context getMyApplication(){
        return myApplication;
    }

}
