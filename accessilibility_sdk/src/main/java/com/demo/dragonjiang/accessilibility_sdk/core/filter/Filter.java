package com.demo.dragonjiang.accessilibility_sdk.core.filter;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author DragonJiang
 * @Date 2016/8/20
 * @Time 13:13
 * @description
 */
public abstract class Filter<T> {
    protected T mPurpose;

    public Filter(T purpose) {
        this.mPurpose = purpose;
    }

    public abstract boolean match(final AccessibilityNodeInfo node);
}
