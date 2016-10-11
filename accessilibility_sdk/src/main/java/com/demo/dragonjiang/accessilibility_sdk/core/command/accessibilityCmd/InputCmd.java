package com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICmdCallback;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.BaseCmd;
import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:49
 * @description
 */
public class InputCmd extends BaseCmd {

    private static final String TAG = "InputCmd";

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
    public
    @RESULT
    int execute() {
        return performInput(mNode, mText);
    }

    @Override
    public void executeAsync(ICmdCallback callback) {
        performInputAsync(mNode, mText, callback);
    }

    /**
     * input text
     *
     * @param node
     * @param input
     * @return
     */
    private
    @RESULT
    int performInput(@Nullable final AccessibilityNodeInfo node, @Nullable final String input) {
        if (node == null || input == null) {
            return EXEC_FAILURE;
        }

        //焦点  （n是AccessibilityNodeInfo对象）
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);

        //android>21 = 5.0时可以用ACTION_SET_TEXT
        //android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
        //使用剪切板
        ClipboardManager clipboard = (ClipboardManager) ContextUtils.getAppContext().getSystemService(Context
                .CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", "");
        clipboard.setPrimaryClip(clip);

        //清除edit上的文本
        Bundle arguments = new Bundle();
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE);
        arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
                true);
        node.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);//往前清除
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        node.performAction(AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, arguments);//往后清除
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        // no work 因为密码取不到文本
//        Bundle arguments = new Bundle();
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, node.getText() == null ? 0 : node
//                .getText().length());
//        node.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
//        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);

        //粘贴进入内容
        clip = ClipData.newPlainText("text", input);
        clipboard.setPrimaryClip(clip);
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        return EXEC_SUCCESS;
    }

    /**
     * input text
     *
     * @param node
     * @param input
     * @return
     */
    private void performInputAsync(@Nullable final AccessibilityNodeInfo node, @Nullable final String input,
                                   @Nullable ICmdCallback callback) {
        if (node == null || input == null) {
            if (callback != null) {
                callback.onFailure();
            }
            return;
        }

        //焦点  （n是AccessibilityNodeInfo对象）
        node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);

        //android>21 = 5.0时可以用ACTION_SET_TEXT
        //android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
        //使用剪切板
        ClipboardManager clipboard = (ClipboardManager) ContextUtils.getAppContext().getSystemService(Context
                .CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", "");
        clipboard.setPrimaryClip(clip);

        //清除edit上的文本
        Bundle arguments = new Bundle();
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE);
        arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
                true);
        node.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);//往前清除
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        node.performAction(AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, arguments);//往后清除
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        // no work 因为密码取不到文本
//        Bundle arguments = new Bundle();
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
//        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, node.getText() == null ? 0 : node
//                .getText().length());
//        node.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
//        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);

        //粘贴进入内容
        clip = ClipData.newPlainText("text", input);
        clipboard.setPrimaryClip(clip);
        node.performAction(AccessibilityNodeInfo.ACTION_PASTE);

        if (callback != null) {
            callback.onSuccess();
        }
    }
}
