package com.demo.dragonjiang.accessilibility_sdk.core.command;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdCancelException;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdNotSupportException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

/**
 * @author DragonJiang
 * @Date 2016/8/2
 * @Time 14:04
 * @description
 */
public class CmdExecutor {

    /**
     * tag
     */
    private static final String TAG = "CmdExecutor";

    /**
     * 同步执行命令
     */
    public static synchronized
    @ICommand.RESULT
    int execute(final ICommand cmd) {
        if (cmd == null) {
            return ICommand.EXEC_FAILURE;
        }

        try {
            return cmd.execute();
        } catch (CmdNotSupportException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            return ICommand.EXEC_FAILURE;
        }
    }

    /**
     * 返回observable对象，执行命令
     *
     * @param cmd
     * @return
     */
    public static Observable<Integer> executeObservable(final ICommand cmd) {
        return cmd.executeObservable();
    }













/**********************************************deprecated****************************************************/

    /**
     * 线程池
     */
    private static ScheduledExecutorService mScheduledExecutor = Executors.newScheduledThreadPool(3);

    /**
     * task
     */
    private static ScheduledTask mScheduledTask;

    /**
     * 返回observable对象，异步执行命令,命令可以被取消
     *
     * @param cmd
     * @param delayInMilliseconds
     * @param canBeCancel 如果设为true，下一次调用这个方法，会把上一次的命令取消
     * @return
     * @deprecated
     */
    public static Observable<Integer> executeCancelable(final ICommand cmd, final long delayInMilliseconds,
                                                        final boolean canBeCancel) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                executeAsync(cmd, delayInMilliseconds, new ICmdCallback() {
                    @Override
                    public void onScheduled() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(ICommand.EXEC_SCHEDULED);
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new CmdCancelException());
                        }
                    }

                    @Override
                    public void onFailure() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new CmdExecuteFailureException());
                        }
                    }

                    @Override
                    public void onSuccess() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(ICommand.EXEC_SUCCESS);
                            subscriber.onCompleted();
                        }
                    }
                }, canBeCancel);
            }
        });
    }

    /**
     * 异步执行命令，如果有命令还在schedule，就取消那个命令，schedule新的命令
     */
    public static synchronized void executeAsync(final ICommand cmd, final long delayInMilliseconds,
                                                 final ICmdCallback callback, final boolean canBeCancel) {

        if (cmd == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        if (delayInMilliseconds <= 0) {
            try {
                cmd.executeAsync(callback);
            } catch (CmdNotSupportException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                if (callback != null) {
                    callback.onFailure();
                }
            }
            return;
        }


        long delay = delayInMilliseconds < 0 ? 0 : delayInMilliseconds;

        //cancel pre task
        if (mScheduledTask != null && mScheduledTask.feature != null && !mScheduledTask.feature.isCancelled()
                && mScheduledTask.canBeCancel) {

            mScheduledTask.feature.cancel(true);
            if (mScheduledTask.callback != null) {
                mScheduledTask.callback.onCancel();
            }
            mScheduledTask = null;
        }

        //schedule new task
        mScheduledTask = new ScheduledTask(new Runnable() {
            @Override
            public void run() {
                //这里的callback是之前shedule的
                //这里不能使用mScheduledTask.callback
                try {
                    cmd.executeAsync(callback);
                } catch (CmdNotSupportException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                    if (callback != null) {
                        callback.onFailure();
                    }
                }
            }
        }, null, callback, canBeCancel);
        mScheduledTask.feature = mScheduledExecutor.schedule(mScheduledTask.cmd, delay, TimeUnit.MILLISECONDS);

        if (callback != null) {
            callback.onScheduled();
        }
    }


    /**
     * cmd.
     * 封装了runnable 和 ScheduledFuture
     */
    private static class ScheduledTask {

        public Runnable cmd;
        public ScheduledFuture feature;
        public ICmdCallback callback;
        public boolean canBeCancel;

        public ScheduledTask(Runnable cmd, ScheduledFuture feature, ICmdCallback callback, boolean canBeCancel) {
            this.feature = feature;
            this.cmd = cmd;
            this.callback = callback;
            this.canBeCancel = canBeCancel;
        }
    }
}
