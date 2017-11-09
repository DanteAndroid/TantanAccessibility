package com.danteandroid.accessibilitydemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by yons on 17/11/7.
 */

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }
}
