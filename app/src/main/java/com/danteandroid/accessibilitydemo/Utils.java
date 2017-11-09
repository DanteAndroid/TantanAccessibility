package com.danteandroid.accessibilitydemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

/**
 * Created by yons on 17/11/7.
 */

public class Utils {
    private static final String TAG = "Utils";
    
    public static boolean isTantanInstalled() {
        final PackageManager packageManager = App.context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(Constants.TANTAN_PACKAGE_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void goTantan() {
        PackageManager packageManager = App.context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Constants.TANTAN_PACKAGE_NAME);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.context.startActivity(intent);
        }
    }

    public static void goMarket(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.TANTAN_PACKAGE_NAME));
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/apk/" + Constants.TANTAN_PACKAGE_NAME));
            activity.startActivity(intent);
        }
    }

    public static void donate(Activity activity) {
        if (AlipayZeroSdk.hasInstalledAlipayClient(activity.getApplicationContext())) {
            AlipayZeroSdk.startAlipayClient(activity, Constants.ALI_PAY);
        } else {
            Toast.makeText(activity, "支付宝未安装", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAccessibilityOpen() {
        int accessibilityEnabled = 0;
        final String service = BuildConfig.APPLICATION_ID + "/" + MyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    App.context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    App.context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
}
