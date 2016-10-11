package com.example.nd99u;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <action android:name="android.intent.action.BOOT_COMPLETED"/>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
 * <action android:name="android.intent.action.USER_PRESENT"/>
 * <action android:name="android.intent.action.TIME_SET"/>
 * <action android:name="android.intent.action.DATE_CHANGED"/>
 * <action android:name="android.intent.action.TIMEZONE_CHANGED"/>
 * <action android:name="android.intent.action.LOCALE_CHANGED"/>
 * <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
 * <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED"/>
 * 接收这些广播，接收广播后激活NotificationService
 * 以此保持service常驻
 *
 * @author DragonJiang
 * @date 2015/11/17
 * @Description:
 * @deprecated
 */
public class NotificationServiceLauncherReceiver extends BroadcastReceiver {
    private static final String TAG = "ServiceLauncherReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationService.startService(context);
    }
}
