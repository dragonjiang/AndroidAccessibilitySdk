package com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;


/**
 * @author DragonJiang
 * @Date 2016/8/2
 * @Time 13:49
 * @description
 */
public class HomeCmd extends BaseCmd {

    private static final String TAG = "HomeCmd";

    private AccessibilityService mService;

    @Override
    public ICommand addService(AccessibilityService service) {
        mService = service;
        return this;
    }

    @Override
    public
    @RESULT
    int execute() {
        return performClick(mService);
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performClickAsync(mService, callback);
    }

    /**
     * click view
     *
     * @param service
     * @return
     */
    private
    @RESULT
    int performClick(final AccessibilityService service) {
        if (service == null) {
            return EXEC_FAILURE;
        }

        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        Log.i(TAG, "点击成功：GLOBAL_ACTION_HOME");

        return EXEC_SUCCESS;
    }

    /**
     * click view
     *
     * @param service
     * @return
     */
    private void performClickAsync(final AccessibilityService service, ICmdCallback callback) {
        if (service == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        Log.i(TAG, "点击成功：GLOBAL_ACTION_HOME");

        if (callback != null) {
            callback.onSuccess();
        }
    }

}
