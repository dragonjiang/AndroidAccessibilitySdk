package com.example.nd99u;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;
import com.demo.dragonjiang.accessilibility_sdk.utils.SPUtils;

public class NotificationService extends Service {
    /**
     * tag
     */
    private static final String TAG = "NotificationService";

    /**
     * launch time
     */
    private String mStrLaunchTime;
    private long mLaunchTime = 0;

    /**
     * time tick receiver
     */
    private TimeTickReceiver mTickReceiver;

    /**
     * notification
     */
    private Notification mNotification;
    private NotificationManager mNotificationMgr;
    private NotificationHelper mHelper;

    /**
     * 启动service
     *
     * @param context
     */
    public static final void startService(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(Constants.ACTION.START_FOREGROUND);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            mNotification = mHelper.buildNotification();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);

            mHelper.clearResult();
            mHelper.resetStep();
            update();

        } else {

            if (intent.getAction() != null && intent.getAction().equals(Constants.ACTION.START_FOREGROUND)) {
                Toast.makeText(this, "开启服务", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Received Start Foreground Intent ");

                mNotification = mHelper.buildNotification();
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);

                mHelper.clearResult();
                mHelper.resetStep();
                update();

            } else if (intent.getAction() != null && intent.getAction().equals(Constants.ACTION.STOP_FOREGROUND)) {
                Toast.makeText(this, "关闭服务", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTickReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private void init() {
        mNotificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mHelper = new NotificationHelper(this);

        mStrLaunchTime = (String) SPUtils.get(this, Constants.SP_KEY.LAUNCH_TIME, "08:50");

        mHelper.clearResult();
        mHelper.resetStep();

        mTickReceiver = new TimeTickReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mTickReceiver, filter);
    }

    public void update() {
        mLaunchTime = mHelper.parseNextTime(mStrLaunchTime);

        String result = mHelper.updateResult(mLaunchTime);
        mNotification.contentView.setTextViewText(R.id.tv_launch_time, mStrLaunchTime);
        mNotification.contentView.setTextViewText(R.id.tv_send_flower_result, result);
        mNotificationMgr.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mNotification);
    }

    public static class NotificationSendFlowerHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "sendflower Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public class TimeTickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (!intent.getAction().equals(Intent.ACTION_TIME_TICK)
                    && !intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                return;
            }

            Log.i(TAG, "ACTION_TIME_TICK");

            int result = (int) SPUtils.get(ContextUtils.getAppContext(), Constants.SP_KEY.RESULT,
                    Constants.RUN_RESULT.NONE);

            if (result == Constants.RUN_RESULT.SUCCESS) {
                //设置成功
                SPUtils.put(ContextUtils.getAppContext(), Constants.SP_KEY.RESULT, Constants.RUN_RESULT.NONE);
                SPUtils.put(ContextUtils.getAppContext(), Constants.SP_KEY.LAST_RESULT, Constants.RUN_RESULT.SUCCESS);

                mHelper.resetStep();
                mHelper.releaseWakeLock();

                Log.i(TAG, "send flower success");

            } else {
                final long current = System.currentTimeMillis();

                if (current >= mLaunchTime && current < mLaunchTime + Constants.MAX_PROCESSING_TIME) {
                    //如果没有成功，才继续
                    if ((int) SPUtils.get(ContextUtils.getAppContext(), Constants.SP_KEY.LAST_RESULT,
                            Constants.RUN_RESULT.NONE) != Constants.RUN_RESULT.SUCCESS) {
                        mHelper.wakeLock();
                        mHelper.resetStep();
                        mHelper.launchApp();
                        Log.i(TAG, "send flower ing");
                    }

                } else {
                    if (current >= mLaunchTime + Constants.MAX_PROCESSING_TIME) {
                        //时间超出，失败。
                        SPUtils.put(ContextUtils.getAppContext(), Constants.SP_KEY.RESULT, Constants.RUN_RESULT.NONE);
                        SPUtils.put(ContextUtils.getAppContext(), Constants.SP_KEY.LAST_RESULT, Constants.RUN_RESULT
                                .FAILURE);
                    }

                    mHelper.resetStep();
                    mHelper.releaseWakeLock();
                    Log.i(TAG, "not match time");
                }
            }

            update();
        }
    }
}