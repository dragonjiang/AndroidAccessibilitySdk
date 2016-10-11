package com.demo.dragonjiang.accessilibility_sdk.core.filter;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 17:01
 * @description
 */
public class ContentFilter extends Filter<String> {


    public ContentFilter(String purpose) {
        super(purpose);
    }

    @Override
    public boolean match(AccessibilityNodeInfo node) {
        if (node == null) {
            return false;
        }

        final String s = mPurpose == null ? "" : mPurpose;
        CharSequence determinand = TextUtils.isEmpty(node.getText()) ? node.getContentDescription() : node.getText();

        if (!TextUtils.isEmpty(determinand) && determinand.toString().contains(s)) {
            return true;
        }

        return false;
    }
}
