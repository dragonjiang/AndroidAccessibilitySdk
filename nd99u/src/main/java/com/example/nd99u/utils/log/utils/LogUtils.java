/*
 * Copyright (C) 2014 Li Cong, forlong401@163.com http://www.360qihoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.nd99u.utils.log.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;
import com.example.nd99u.utils.FileUtil;
import com.example.nd99u.utils.log.manager.LogManager;

import java.io.File;
import java.util.Calendar;

/**
 * Log utility.
 *
 * @author Li Cong
 */
public class LogUtils {
    /**
     * enable log
     */
    public static boolean CRASH_SAVE_2_FILE = true;
    public static boolean CRASH_UPLOAD_2_NETWORK = false;
    private static boolean sDebugable = true;

    /**
     * log type
     */
    public static final int LOG_TYPE_2_LOGCAT = 0x01;
    public static final int LOG_TYPE_2_FILE = 0x02;
    public static final int LOG_TYPE_2_FILE_AND_LOGCAT = 0x03;
    public static final int LOG_TYPE_2_NETWORK = 0x04;

    /**
     * log directory name
     */
    public final static String LOG_CACHE_DIRECTORY_NAME = "log";
    public final static String CRASH_CACHE_DIRECTORY_NAME = "crash";
    private static String sLogFileName = null;
    private static String sCrashFileName = null;
    private static String sAppName = "";

    /**
     * log 2 file
     *
     * @param tag
     * @param msg
     */
    public static void FLog(String tag, String msg) {
        LogManager.getManager(ContextUtils.getAppContext()).log(tag, msg, LogUtils.LOG_TYPE_2_FILE);
    }

    /**
     * log 2 file and logcat
     *
     * @param tag
     * @param msg
     */
    public static void FLLog(String tag, String msg) {
        LogManager.getManager(ContextUtils.getAppContext()).log(tag, msg, LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
    }

    /**
     * log 2 logcat
     *
     * @param tag
     * @param msg
     */
    public static void LLog(String tag, String msg) {
        LogManager.getManager(ContextUtils.getAppContext()).log(tag, msg, LogUtils.LOG_TYPE_2_LOGCAT);
    }

    /**
     * log 2 new work
     *
     * @param tag
     * @param msg
     */
    public static void NLog(String tag, String msg) {
        LogManager.getManager(ContextUtils.getAppContext()).log(tag, msg, LogUtils.LOG_TYPE_2_NETWORK);
    }

    public static void setDebugable(boolean debug) {
        sDebugable = debug;
    }

    public static boolean getDebugable() {
        return sDebugable;
    }

    /************************************* dir and file *******************************************/
    /**
     * get log file name
     *
     * @param context
     * @return
     */
    public static String getLogFileName(Context context) {
//        if (TextUtils.isEmpty(sLogFileName)) {
//            File sub = new File(getLogDir(context), "log_"
//                    + getCurrentDate() + ".txt");
//            sLogFileName = sub.getAbsolutePath();
//        }
//
//        return sLogFileName;

        long date = getDateFromFileName(sLogFileName);
        if (date > 0 && date == getCurrentDate()) {
            return sLogFileName;
        }
        File sub = new File(getLogDir(context), "log_" + getCurrentDate() + ".txt");
        sLogFileName = sub.getAbsolutePath();
        return sLogFileName;
    }

    /**
     * get crash file name
     *
     * @param context
     * @return
     */
    public static String getCrashFileName(Context context) {
//        if (TextUtils.isEmpty(sCrashFileName)) {
//            File sub = new File(getCrashDir(context), "crash_"
//                    + Utils.getCurrentDate() + ".txt");
//            sCrashFileName = sub.getAbsolutePath();
//        }
//
//        return sCrashFileName;
        File sub = new File(getCrashDir(context), "crash_" + getCurrentDate() + ".txt");
        return sub.getAbsolutePath();
    }

    /**
     * get log dir
     *
     * @param context
     * @return
     */
    public static File getLogDir(Context context) {
        return getAppCacheDir(context, LOG_CACHE_DIRECTORY_NAME);
    }

    /**
     * get crash dir
     *
     * @param context
     * @return
     */
    public static File getCrashDir(Context context) {
        return getAppCacheDir(context, CRASH_CACHE_DIRECTORY_NAME);
    }


    /**
     * get log dir
     *
     * @param context
     * @param subName
     * @return
     */
    public static File getAppCacheDir(Context context, String subName) {
        if (!sdAvailible()) {
            return null;
        }

        File dir = new File(FileUtil.getAppCachePath(), "cache");
        File sub = new File(dir, subName);

        try {
            if (!sub.exists()) {
                sub.mkdirs();
            }
            return sub;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get current date
     *
     * @return
     */
    public static long getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * is sd card availible
     *
     * @return
     */
    public static boolean sdAvailible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    private static long getDateFromFileName(String name) {
        if (TextUtils.isEmpty(name)) {
            return -1;
        }

        int index1 = name.lastIndexOf("_");
        int index2 = name.lastIndexOf(".");
        if (index1 < 0 || index2 < 0 || index1 >= index2) {
            return -1;
        }
        String date = name.substring(index1 + 1, index2);
        return Long.parseLong(date);
    }

    /************************************* build log *******************************************/
    /**
     * encrypt log
     *
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        // TODO: encrypt data.
        return str;
    }

    /**
     * build sys info
     *
     * @param context
     * @return
     */
    public static String buildSystemInfo(Context context) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");
        buffer.append("#-------system info-------");
        buffer.append("\n");
        buffer.append("version-name:");
        buffer.append(getVersionName(context));
        buffer.append("\n");
        buffer.append("version-code:");
        buffer.append(getVersionCode(context));
        buffer.append("\n");
        buffer.append("system-version:");
        buffer.append(getSystemVersion(context));
        buffer.append("\n");
        buffer.append("model:");
        buffer.append(getModel(context));
        buffer.append("\n");
        buffer.append("density:");
        buffer.append(getDensity(context));
        buffer.append("\n");
        buffer.append("screen-height:");
        buffer.append(getScreenHeight(context));
        buffer.append("\n");
        buffer.append("screen-width:");
        buffer.append(getScreenWidth(context));
//        buffer.append("\n");
//        buffer.append("imei:");
//        buffer.append(getIMEI(context));
//        buffer.append("\n");
//        buffer.append("unique-code:");
//        buffer.append(getUniqueCode(context));
//        buffer.append("\n");
//        buffer.append("mobile:");
//        buffer.append(getMobile(context));
//        buffer.append("\n");
//        buffer.append("imsi:");
//        buffer.append(getProvider(context));
        buffer.append("\n");
        buffer.append("isWifi:");
        buffer.append(isWifi(context));
        buffer.append("\n");
        return buffer.toString();
    }

    /**
     * get unique code
     *
     * @param context
     * @return
     */
    public static String getUniqueCode(Context context) {
        if (context == null)
            return null;
        String imei = getIMEI(context);
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return imei + "_" + info.getMacAddress();
    }

    /**
     * is in wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * get mobile
     *
     * @param context
     * @return
     */
    public static String getMobile(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     * get provider
     *
     * @param context
     * @return
     */
    public static String getProvider(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    /**
     * get imei
     *
     * @param context
     * @return
     */
    public static final String getIMEI(final Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    /**
     * get screen width
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * get screen height
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * get system version
     *
     * @param context
     * @return
     */
    public static String getSystemVersion(Context context) {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getModel(Context context) {
        return android.os.Build.MODEL != null ? android.os.Build.MODEL.replace(
                " ", "") : "unknown";
    }

    /**
     * get density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * get version name
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pinfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return "";
    }

    /**
     * get app name
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        if (TextUtils.isEmpty(sAppName)) {
            sAppName = "com_forlong401_log";
            try {
                PackageInfo pinfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);
                String packageName = pinfo.packageName;
                if (!TextUtils.isEmpty(packageName)) {
                    sAppName = packageName.replaceAll("\\.", "_");
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        return sAppName;
    }

    /**
     * get version code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pinfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return 1;
    }
}
