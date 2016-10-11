package com.demo.dragonjiang.accessilibility_sdk.core;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.core.command.CmdExecutor;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;

import rx.Observable;

/**
 * @author DragonJiang
 * @Date 2016/9/20
 * @Time 12:44
 * @description
 * @deprecated
 */
public class PurposeActionHelper {

    /**
     * perform action
     *
     * @param cmd
     * @return
     */
    public static
    @ICommand.RESULT
    int performAction(AccessibilityService context, @NonNull ICommand cmd) {
        return CmdExecutor.execute(cmd.addService(context));
    }

    /**
     * perform action
     *
     * @param node
     * @param ui
     * @return
     */
    public static
    @ICommand.RESULT
    int performAction(final AccessibilityNodeInfo node, final PurposeUiInfo ui) {
        if (node == null || ui == null || ui.command == null) {
            return ICommand.EXEC_FAILURE;
        }

        return CmdExecutor.execute(ui.command.addNode(node).addUi(ui));
    }

    /**
     * perform action
     *
     * @param cmd
     * @return
     */
    public static Observable<Integer> performActionObservable(AccessibilityService context, @NonNull ICommand cmd) {
        return CmdExecutor.executeObservable(cmd.addService(context));
    }

    /**
     * perform action
     *
     * @param node
     * @param ui
     * @return
     */
    public static Observable<Integer> performActionObservable(final AccessibilityNodeInfo node,
                                                              final PurposeUiInfo ui) {
        if (node == null || ui == null || ui.command == null) {
            return Observable.error(new CmdExecuteFailureException());
        }

        return CmdExecutor.executeObservable(ui.command.addNode(node).addUi(ui));
    }

    /**
     * perform action
     *
     * @param cmd
     * @return
     */
    public static Observable<Integer> performActionObservable(AccessibilityService context, @NonNull ICommand cmd,
                                                              long delay, boolean canBeCancel) {
        return CmdExecutor.executeCancelable(cmd.addService(context), delay, canBeCancel);
    }

}
