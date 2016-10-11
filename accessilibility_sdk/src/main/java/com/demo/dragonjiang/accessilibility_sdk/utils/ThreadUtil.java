package com.demo.dragonjiang.accessilibility_sdk.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 *
 * @author DavidDing
 * @date 2015/6/17
 * @time 11:39
 * @description
 */
public final class ThreadUtil {

    private ThreadUtil() {
    }

    /**
     * 并发进程池
     */
    public static final ExecutorService sMoreExecutor = Executors.newCachedThreadPool();

    /**
     * 队列进程池 容量为1
     */
    private static final ExecutorService sFixedExecutor = Executors.newFixedThreadPool(1);

    /**
     * 队列进程池 容量为1
     */
    private static final ExecutorService sSingleExecutor = Executors.newSingleThreadExecutor();

    /**
     * 进入 并发线程池
     *
     * @param runnable
     */
    public static final void executeMore(Runnable runnable) {
        sMoreExecutor.execute(runnable);
    }

    /**
     * 进入 队列线程池
     *
     * @param runnable
     */
    public static final void executeQueue(Runnable runnable) {
        sFixedExecutor.execute(runnable);
    }

    /**
     * 进入 单个线程
     *
     * @param runnable
     * @return
     */
    public static final void executeSingle(Runnable runnable) {
        sSingleExecutor.execute(runnable);
    }
}
