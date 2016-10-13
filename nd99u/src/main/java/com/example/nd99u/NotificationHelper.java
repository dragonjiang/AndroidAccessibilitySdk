package com.example.nd99u;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.demo.dragonjiang.accessilibility_sdk.core.command.CmdExecutor;
import com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd.ShellHomeCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd.ShellPowerCmd;
import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;
import com.demo.dragonjiang.accessilibility_sdk.utils.LaunchUtil;
import com.demo.dragonjiang.accessilibility_sdk.utils.SPUtils;
import com.demo.dragonjiang.accessilibility_sdk.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;
import rx.functions.Action1;

import static android.content.Context.POWER_SERVICE;

/**
 * @author DragonJiang
 * @Date 2016/10/10
 * @Time 22:38
 * @description
 */
public class NotificationHelper {
    Context mContext;

    /**
     * tag
     */
    private static final String TAG = "NotificationService";

    /**
     * wakelock
     */
    private PowerManager.WakeLock mWakelock;

    public NotificationHelper(Context context) {
        this.mContext = context;
    }

    /**
     * build notification
     *
     * @return
     */
    public Notification buildNotification() {
        RemoteViews notificationView = new RemoteViews(mContext.getPackageName(), R.layout.notification);

        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.LAUNCH_MAIN_ACTIVITY);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.ll_container, mainPendingIntent);

//        Intent signInIntent = new Intent(mContext, NotificationService.NotificationSendFlowerHandler.class);
//        signInIntent.putExtra("action", "Flower");
//        PendingIntent sendFlowerPendingIntent = PendingIntent.getBroadcast(mContext, 0, signInIntent, 0);
//        notificationView.setOnClickPendingIntent(R.id.tv_send_flower_result, sendFlowerPendingIntent);

        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle("99U自动插件")
                .setContentText("99U插件服务运行中...")
                .setTicker("99U插件服务启动")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContent(notificationView)
                .setOngoing(true)
                .build();

//        notification.bigContentView = notificationView;

        return notification;
    }

    /**
     * clear send flower result
     */
    public void clearResult() {
        SPUtils.put(mContext, Constants.SP_KEY.RESULT, Constants.RUN_RESULT.NONE);
        SPUtils.put(mContext, Constants.SP_KEY.LAST_RESULT, Constants.RUN_RESULT.NONE);
    }

    /**
     * reset send flower step
     */
    public void resetStep() {
        EventBus.getDefault().post(new MsgEvent(Constants.EVENT_MSG.RESET));
    }

    public long parseNextTime(@NonNull final String time) {
        long next = TimeUtil.parseTime(time);
        if (System.currentTimeMillis() - next >= Constants.MAX_PROCESSING_TIME) {
            next += 86400000;//+1天
        }
        return next;
    }

    /**
     * build sign result string
     *
     * @param launchTime
     * @return
     */
    public String updateResult(long launchTime) {
        long current = System.currentTimeMillis();
        String remainTime = "倒计时：" + getRemainTime(current, launchTime);
        int sendFlowerResult = (int) SPUtils.get(ContextUtils.getAppContext(), Constants.SP_KEY.LAST_RESULT, Constants
                .RUN_RESULT.NONE); //0 未开始或者处理中， 1 失败， 2 成功

        String result;
        if (sendFlowerResult == Constants.RUN_RESULT.NONE) {
            result = remainTime;
        } else if (sendFlowerResult == Constants.RUN_RESULT.SUCCESS) {
            result = remainTime + "    " + "上次结果：成功";
        } else {
            result = remainTime + "    " + "上次结果：失败";
        }

        return result;
    }

    /**
     * build remain time string
     *
     * @param curTime
     * @param purposeTime
     * @return
     */
    public String getRemainTime(long curTime, long purposeTime) {
        if (curTime >= purposeTime) {
            return "0分钟";
        }

        StringBuilder sb = new StringBuilder();
        long remain = purposeTime - curTime;
        int hour = (int) (remain / 3600000);
        int min = (int) ((remain % 3600000) / 60000) + 1;
        if (hour > 0) {
            sb.append(hour);
            sb.append("小时");
        }
        sb.append(min);
        sb.append("分钟");

        return sb.toString();
    }

    /**
     * wake lock
     */
    public void wakeLock() {
        PowerManager pm = (PowerManager) mContext.getSystemService(POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (!isScreenOn) {
            Log.i(TAG, "screen is off");
            CmdExecutor.executeObservable(new ShellPowerCmd()).doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.e(TAG, throwable.getMessage());
                }
            }).subscribe();
        }

        releaseWakeLock();
        if (mWakelock == null) {
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_DIM_WAKE_LOCK, "target"); // this target for tell OS which app control screen
        }

        mWakelock.setReferenceCounted(false);
        mWakelock.acquire();
        Log.i(TAG, "wakelock");


        // 键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        // 这里参数”tag”作为调试时LogCat中的Tag
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("tag");
//        if (km.inKeyguardRestrictedInputMode()) {
            // 解锁键盘
            kl.disableKeyguard();
            Log.i(TAG, "locking");
//        }
    }

    /**
     * release wake lock
     */
    public void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            try {
                mWakelock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * launch app
     */
    public void launchApp() {
        if (!LaunchUtil.isAppOnForeground(ContextUtils.getAppContext(), Constants.APP_PKG)) {
            LaunchUtil.launchApp(Constants.APP_PKG, ContextUtils.getAppContext());

        } else {
            CmdExecutor.executeObservable(new ShellHomeCmd()).subscribe(new Subscriber<Integer>() {
                @Override
                public void onCompleted() {
                    LaunchUtil.launchApp(Constants.APP_PKG, ContextUtils.getAppContext());
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {

                }
            });
        }
    }

}
