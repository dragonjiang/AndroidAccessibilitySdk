package com.demo.dragonjiang.accessilibility_sdk;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.IMachine;

/**
 * @author DragonJiang
 * @Date 2016/7/23
 * @Time 10:57
 * @description
 */
public abstract class BaseAccService extends AccessibilityService {

    protected static final String TAG = "BaseAccService";
    protected IMachine mMachine;


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.i(TAG, "onServiceConnected");
        mMachine = getMachine();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "onAccessibilityEvent");

        Log.i(TAG, "EventType = " + event.getEventType());
        if (event.getText() != null && event.getText().size() > 0) {
            for (int i = 0; i < event.getText().size(); i++) {
                Log.i(TAG, "EventText" + i + " = " + event.getText().get(i));
            }
        }

        //source怎么确定的
//        AccessibilityNodeInfo source = event.getSource();
//        if (source != null && source.getText() != null) {
//            Log.i(TAG, "source = " + source.toString());
//            Log.i(TAG, "sourceClassName = " + source.getClassName());
//            Log.i(TAG, "sourceChildCnt = " + source.getChildCount());
//            Log.i(TAG, "sourceContentt = " + source.getContentDescription());
//            Log.i(TAG, "sourceStr = " + source.getText().toString());
//            printChild(source);
//        }

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            Log.e(TAG, "rootWindow为空");
            return;
        }

        mMachine.dealEvent(event, root);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public abstract IMachine getMachine();
}
