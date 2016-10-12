package com.example.nd99u;

import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.demo.dragonjiang.accessilibility_sdk.BaseAccService;
import com.demo.dragonjiang.accessilibility_sdk.core.IMachine;
import com.example.nd99u.auto.AutoMachine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author DragonJiang
 * @Date 2016/7/23
 * @Time 10:57
 * @description
 */
public class AccService extends BaseAccService {

    protected static final String TAG = "AccService";
    private boolean mStart = false;


    @Subscribe
    public void onEventMainThread(MsgEvent event) {
        Log.i(TAG, "rcv event msg " + event.getMsg());
        final String msg = event.getMsg() == null ? "" : event.getMsg();

        switch (msg) {
            case Constants.EVENT_MSG.RESET:
                if (mMachine != null) {
                    mMachine.reset();
                }
                break;
            case Constants.EVENT_MSG.START:
                mStart = true;
                break;
            case Constants.EVENT_MSG.STOP:
                mStart = false;
                break;
        }

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "onServiceConnected");

        Toast.makeText(getApplicationContext(), "连接成功！", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (!mStart) {
            return;
        }

        super.onAccessibilityEvent(event);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        EventBus.getDefault().unregister(this);
        return super.onUnbind(intent);
    }

    @Override
    public IMachine getMachine() {
        return new AutoMachine(AccService.this);
    }
}
