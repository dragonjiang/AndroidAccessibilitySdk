package com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:49
 * @description
 */
public class ClickCmd extends BaseCmd {

    private static final String TAG = "ClickCmd";

    private AccessibilityNodeInfo mNode;

    @Override
    public ICommand addNode(AccessibilityNodeInfo node) {
        this.mNode = node;
        return this;
    }

    @Override
    public
    @RESULT
    int execute() {
        return performClick(this.mNode);
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performClickAsync(this.mNode, callback);
    }

    /**
     * click view
     *
     * @param node
     * @return
     */
    private
    @RESULT
    int performClick(@Nullable final AccessibilityNodeInfo node) {
        if (node == null) {
            return EXEC_FAILURE;
        }

        if (node.isClickable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.i(TAG, "点击成功：" + node.getText());
            return EXEC_SUCCESS;
        } else {
            return performClick(node.getParent());
        }
    }

    /**
     * click view
     *
     * @param node
     * @param callback
     */
    private void performClickAsync(final AccessibilityNodeInfo node, final ICmdCallback callback) {
        if (node == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        if (node.isClickable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.i(TAG, "点击成功：" + node.getText());
            if (callback != null) {
                callback.onSuccess();
            }
        } else {
            if (callback != null) {
                callback.onFailure();
            }
        }
    }
}
