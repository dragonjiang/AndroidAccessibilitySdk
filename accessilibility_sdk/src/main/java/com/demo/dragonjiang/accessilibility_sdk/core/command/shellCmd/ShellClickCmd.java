package com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.shell_support.IShellCmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.shell_support.ShellCmdUtil;

import rx.Observable;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:49
 * @description
 */
public class ShellClickCmd extends BaseCmd {

    private static final String TAG = "ShellClickCmd";

    private AccessibilityNodeInfo mNode;

    @Override
    public ICommand addNode(AccessibilityNodeInfo node) {
        this.mNode = node;
        return this;
    }

    @Override
    public Observable<Integer> executeObservable() {
        if(mNode == null){
            return Observable.error(new CmdExecuteFailureException());
        }

        Rect rect = new Rect();
        mNode.getBoundsInScreen(rect);
        Log.i(TAG, "点击 point：" + String.format("input tap %d %d", rect.centerX(), rect.centerY()));

        final String cmd = ShellCmdUtil.buildTapCmd(rect.centerX(), rect.centerY());
        return ShellCmdUtil.execShellCmdObservable(cmd);
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performShellClick(mNode, callback);
    }

    /**
     * click view by send shell cmd
     *
     * @param node
     * @return
     */
    private void performShellClick(@Nullable final AccessibilityNodeInfo node, final ICmdCallback callback) {
        if (node == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        final String cmd = String.format("input tap %d %d", rect.centerX(), rect.centerY());
        Log.i(TAG, "点击 point：" + cmd);

        ShellCmdUtil.execShellCommand(ShellCmdUtil.buildTapCmd(rect.centerX(), rect.centerY()), new IShellCmdCallback() {
            @Override
            public void OnInputError(String errorMsg) {
                Log.e(TAG, "点击 失败：" + errorMsg);
                if (callback != null) {
                    callback.onFailure();
                }
            }

            @Override
            public void OnComplete(String result) {
                Log.i(TAG, "点击 成功：" + result);
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        });
        Log.i(TAG, "点击成功：" + node.getText());

        if (callback != null) {
            callback.onScheduled();
        }
    }
}
