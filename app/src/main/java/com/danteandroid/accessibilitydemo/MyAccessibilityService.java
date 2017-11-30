package com.danteandroid.accessibilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Random;

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";
    public static boolean stopService;
    private Random random = new Random();
    private String viewId;
    private boolean needLike;
    private NotificationOnClickReceiver receiver;

    public MyAccessibilityService() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        receiver = new NotificationOnClickReceiver();
        IntentFilter filter = new IntentFilter(NotificationUtil.INTENT_FILTER);
        getApplicationContext().registerReceiver(receiver, filter);
        Log.d(TAG, "onServiceConnected: ");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (stopService) {
            Log.d(TAG, "onAccessibilityEvent: stop service");
            return;
        }
        AccessibilityNodeInfo info = getRootInActiveWindow();
        if (info == null) {
            Log.d(TAG, "onAccessibilityEvent: getRootInActiveWindow is null");
            return;
        }
        List<AccessibilityNodeInfo> l = info.findAccessibilityNodeInfosByText(Constants.OUT_OF_LIKES);
        List<AccessibilityNodeInfo> m = info.findAccessibilityNodeInfosByText(Constants.OUT_OF_LIKES_EN);
        Log.d(TAG, "outof: " + l.size());
        if (l.size() > 0 || m.size() > 0) {
            stopService = true;
            NotificationUtil.removeNotification();
            return;
        }
        needLike = SpUtil.getBoolean(Constants.LIKE_OR_NOT, true);
        List<AccessibilityNodeInfo> list = info.findAccessibilityNodeInfosByViewId(Constants.LIKE_ID);
        List<AccessibilityNodeInfo> dislikeList = info.findAccessibilityNodeInfosByViewId(Constants.DISLIKE_ID);
        if (list == null || dislikeList == null) {
            Log.d(TAG, "onAccessibilityEvent: list is null " + list + " " + dislikeList);
            return;
        }

        for (AccessibilityNodeInfo item : list) {
            int index = list.indexOf(item);
            Log.d(TAG, "onAccessibilityEvent: click index:" + index + " in " + list.size());
            AccessibilityNodeInfo node;
            if (SpUtil.getBoolean(Constants.RANDOM) && random.nextBoolean()) {
                //如果开启了随机，那么随机返回（不点击）
                if (needLike) {
                    if (dislikeList.isEmpty()) return;
                    node = dislikeList.get(0);
                } else {
                    node = list.get(index);
                }
                click(node);
                return;
            } else {
                if (needLike) {
                    node = item;
                } else {
                    if (dislikeList.isEmpty()) return;
                    node = dislikeList.get(0);
                }
                click(node);
            }
//            item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @Override
    public void onInterrupt() {

    }

    public void click(AccessibilityNodeInfo info) {
        if (info != null) {
            Log.d(TAG, "click: onAccessibilityEvent");
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


}
