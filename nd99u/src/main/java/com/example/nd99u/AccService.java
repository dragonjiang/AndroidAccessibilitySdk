package com.example.nd99u;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.demo.dragonjiang.accessilibility_sdk.core.IMachine;
import com.demo.dragonjiang.accessilibility_sdk.core.ResetStateEvent;
import com.example.nd99u.auto.AutoMachine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author DragonJiang
 * @Date 2016/7/23
 * @Time 10:57
 * @description
 */
public class AccService extends AccessibilityService {

    protected static final String TAG = "AccService";
    protected IMachine mMachine;

    @Subscribe
    public void onEvent(ResetStateEvent event) {
        Log.i(TAG, "rcv reset event");
        if (mMachine != null) {
            mMachine.reset();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand intent" + intent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected");

        Toast.makeText(getApplicationContext(), "连接成功！", Toast.LENGTH_SHORT).show();

        mMachine = new AutoMachine(AccService.this);
        EventBus.getDefault().register(this);
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

        //source不知道是怎么确定的
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
        EventBus.getDefault().unregister(this);
        return super.onUnbind(intent);
    }
}
