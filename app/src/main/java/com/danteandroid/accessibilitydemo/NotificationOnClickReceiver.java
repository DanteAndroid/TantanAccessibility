package com.danteandroid.accessibilitydemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yons on 17/11/7.
 */

public class NotificationOnClickReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationOnClickRece";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyAccessibilityService.stopService = true;
        Log.d(TAG, "onReceive: clicked");
    }
}
