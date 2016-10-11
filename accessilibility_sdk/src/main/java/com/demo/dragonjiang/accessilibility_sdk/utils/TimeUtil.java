package com.demo.dragonjiang.accessilibility_sdk.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 *
 * @author Chenyw
 *         Created on 2015/6/19.
 * @description
 */
public class TimeUtil {
    public static final long MINUTE_MILLIS = 60 * 1000l;
    public static final long DAY_MILLIS = 24 * 60 * MINUTE_MILLIS;

    public final static String FORMAT_DATE = "yyyy年MM月dd日 HH:mm";
    public final static String FORMAT_DATE1 = "HH时mm分ss秒_yyyy-MM-dd";
    public final static String FORMAT_DATE2 = "yyyy-MM-dd";
    public final static String FORMAT_DATE3 = "yyMMdd";
    public final static String FORMAT_DATE4 = "HH:mm";
    public final static String FORMAT_DATE5 = "yyyy年 MM月dd日 ";
    public final static String FORMAT_DATE6 = "yyyy年MM月dd日";
    public final static String FORMAT_DATE8 = "yyyy";
    public final static String FORMAT_DATE7 = "MM-dd";
    public final static String FORMAT_DATE9 = "yyyy/MM/dd";
    public final static String FORMAT_DATE10 = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE11 = "MM月dd日";
    public final static String FORMAT_DATE12 = "yyyyMMdd";
    public final static String FORMAT_DATE13 = "MM月dd日 hh:mm";
    public final static String FORMAT_DATE14 = "yyyy年  MM月dd日  HH时mm分";
    public final static String FORMAT_DATE15 = "mm:ss";
    public final static String FORMAT_DATE16 = "yyyyMMdd_HHmmss";


    public static String formatDate2(long timestamp) {
        return formatDate(timestamp, FORMAT_DATE2);
    }

    /**
     * @Title: formatDate
     * @Description: 时间格式转换
     * @param: timestamp
     * @param: dateFormat
     * @return:
     * @throws:
     */
    public static String formatDate(long timestamp, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        try {
            return format.format(timestamp);
        } catch (Exception e) {
            Log.e("TimeUtil", e.toString());
        }
        return "";
    }

    /**
     * 字符串转成long
     *
     * @param dateString 时间
     * @param formatStr  时间格式
     * @return
     */
    public static long parseDateStr(String dateString, String formatStr) {
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String convertTimeToFormat(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        String result = "";
        if (time < 60 && time >= 0) { // 小于1分钟
            result = "刚刚";
        } else if (time >= 60 && time < 3600) { // 1-60分钟
            result = time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) { // 1-24小时
            result = time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30 * 12) { // 1年之内
            result = formatDate(timeStamp * 1000, FORMAT_DATE13);
        } else if (time >= 3600 * 24 * 30 * 12) { // 超过一年
            result = formatDate(timeStamp * 1000, FORMAT_DATE);
        }
        return result;
    }


    /**
     * 毫秒转成分:秒
     *
     * @return
     */
    public static String getMSSTime(long time) {
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        final long m = seconds / 60;
        final long s = seconds % 60;
        if (s > 9) {
            return m + ":" + s;
        } else {
            return m + ":0" + s;
        }
    }

    /**
     * HH:mm转为今天的HH：mm的格林威治时间
     * @param time HH:mm
     * @return
     */
    public static long parseTime(@NonNull final String time) {
        String date = TimeUtil.formatDate(System.currentTimeMillis(), TimeUtil.FORMAT_DATE2);
        return TimeUtil.parseDateStr(date + " " + time, TimeUtil.FORMAT_DATE10);
    }

    /**
     * 0 hour  1 minute
     * @return
     */
    public static int[] getCurHourMinute() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int[] result = new int[2];
        result[0] = c.get(Calendar.HOUR_OF_DAY);
        result[1] = c.get(Calendar.MINUTE);
        return result;
    }

}
