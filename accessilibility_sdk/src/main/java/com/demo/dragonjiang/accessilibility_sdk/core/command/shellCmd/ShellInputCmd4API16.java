package com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.shell_support.IShellCmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.shell_support.ShellCmdUtil;

import rx.Observable;
import rx.functions.Func1;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:49
 * @description
 */
public class ShellInputCmd4API16 extends BaseCmd {

    private static final String TAG = "ShellInputCmd";

    private AccessibilityNodeInfo mNode;
    private String mText;

    @Override
    public ICommand addNode(AccessibilityNodeInfo node) {
        this.mNode = node;
        return this;
    }

    @Override
    public ICommand addUi(PurposeUiInfo ui) {
        this.mText = ui == null ? "" : ui.input;
        return this;
    }

    @Override
    public Observable<Integer> executeObservable() {
        if (mNode == null || mText == null) {
            return Observable.error(new CmdExecuteFailureException());
        }

        //焦点  （n是AccessibilityNodeInfo对象）
        mNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);

        //清除edit上的文本
        Rect rect = new Rect();
        mNode.getBoundsInScreen(rect);

        final String cmd = ShellCmdUtil.buildTapCmd(rect.right - 20, rect.centerY());
        return ShellCmdUtil.execShellCmdObservable(cmd).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                if (integer != null && integer == ICommand.EXEC_SUCCESS) {
                    Log.e(TAG, "删除 成功");
                    return ShellCmdUtil.execShellCmdObservable(ShellCmdUtil.buildTextCmd(mText));
                } else {
                    Log.e(TAG, "删除 失败");
                    return Observable.error(new CmdExecuteFailureException());
                }
            }
        });
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performShellInput(mNode, mText, callback);
    }

    /**
     * input text
     *
     * @param node
     * @param input
     * @return
     */
    private void performShellInput(@Nullable final AccessibilityNodeInfo node, @Nullable final String input,
                                   final ICmdCallback callback) {
        if (node == null || input == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        //焦点  （n是AccessibilityNodeInfo对象）
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);

        //清除edit上的文本
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);


        ShellCmdUtil.execShellCommand(ShellCmdUtil.buildTapCmd(rect.right - 20, rect.centerY()), new IShellCmdCallback
                () {
            @Override
            public void OnInputError(String errorMsg) {
                if (callback != null) {
                    callback.onFailure();
                }
                Log.e(TAG, "删除 失败：" + errorMsg);
            }

            @Override
            public void OnComplete(String result) {
                Log.e(TAG, "删除 成功：" + result);

                ShellCmdUtil.execShellCommand(ShellCmdUtil.buildTextCmd(input), new IShellCmdCallback() {
                    @Override
                    public void OnInputError(String errorMsg) {
                        if (callback != null) {
                            callback.onFailure();
                        }
                        Log.e(TAG, "输入 失败：" + errorMsg);
                    }

                    @Override
                    public void OnComplete(String result) {
                        Log.e(TAG, "输入 成功：" + result);
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }
        });


        if (callback != null) {
            callback.onScheduled();
        }
    }
}
