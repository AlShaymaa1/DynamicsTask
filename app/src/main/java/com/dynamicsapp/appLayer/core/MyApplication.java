package com.dynamicsapp.appLayer.core;

import android.app.Application;
import android.content.Context;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
