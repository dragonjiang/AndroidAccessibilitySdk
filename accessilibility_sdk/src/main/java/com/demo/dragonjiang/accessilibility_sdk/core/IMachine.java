package com.demo.dragonjiang.accessilibility_sdk.core;

import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author DragonJiang
 * @Date 2016/8/15
 * @Time 13:38
 * @description
 */
public interface IMachine {
    /**
     * reset status
     */
    void reset();

    /**
     * deal accessibility event
     * @param event
     * @param root
     */
    void dealEvent(@NonNull final AccessibilityEvent event, @NonNull final AccessibilityNodeInfo root);
}
