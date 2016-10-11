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
public class ScrollBackwardCmd extends BaseCmd {

    private static final String TAG = "ScrollBackwardCmd";

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
        return performScroll(this.mNode);
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performScrollAsync(this.mNode, callback);
    }

    /**
     * click view
     *
     * @param node
     * @return
     */
    private
    @RESULT
    int performScroll(@Nullable final AccessibilityNodeInfo node) {
        if (node == null) {
            return EXEC_FAILURE;
        }

        if (node.isScrollable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            Log.i(TAG, "向后滚动成功：" + node.getText());
            return EXEC_SUCCESS;
        } else {
            return performScroll(node.getParent());
        }
    }

    /**
     * click view
     *
     * @param node
     * @param callback
     */
    private void performScrollAsync(final AccessibilityNodeInfo node, final ICmdCallback callback) {
        if (node == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        if (node.isScrollable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            Log.i(TAG, "向后滚动成功：" + node.getText());
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
