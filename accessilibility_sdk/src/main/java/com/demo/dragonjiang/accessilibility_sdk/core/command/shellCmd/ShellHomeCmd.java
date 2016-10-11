package com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.shell_support.IShellCmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.shell_support.ShellCmdUtil;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;

import rx.Observable;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:49
 * @description
 */
public class ShellHomeCmd extends BaseCmd {

    private static final String TAG = "ShellHomeCmd";

    @Override
    public Observable<Integer> executeObservable() {
        final String cmd = ShellCmdUtil.buildKeyEventCmd(ShellCmdUtil.KEY_CODE.KEYCODE_HOME);
        return ShellCmdUtil.execShellCmdObservable(cmd);
    }

    @Override
    public void executeAsync(final ICmdCallback callback) {
        performShellClick(callback);
    }

    /**
     * click view by send shell cmd
     *
     * @return
     */
    private void performShellClick(final ICmdCallback callback) {

        final String cmd = ShellCmdUtil.buildKeyEventCmd(ShellCmdUtil.KEY_CODE.KEYCODE_HOME);

        ShellCmdUtil.execShellCommand(cmd, new IShellCmdCallback() {
            @Override
            public void OnInputError(String errorMsg) {
                if (callback != null) {
                    callback.onFailure();
                }

                Log.e(TAG, "点击 失败：" + errorMsg);
            }

            @Override
            public void OnComplete(String result) {
                if (callback != null) {
                    callback.onSuccess();
                }

                Log.i(TAG, "点击 成功：" + result);
            }
        });

        if (callback != null) {
            callback.onScheduled();
        }
    }
}
