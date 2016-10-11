package com.demo.dragonjiang.accessilibility_sdk.utils;

import android.content.Context;

/**
 * @author LTA
 * @ClassName: ContextUtils
 * @Description:
 * @date 2016/3/22 16:00
 */
public class ContextUtils {
    private static Context mAppContext;

    public static synchronized void setAppContext(Context appContext) {
        mAppContext = appContext;
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
