package com.demo.dragonjiang.accessilibility_sdk.core.command;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.IntDef;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdNotSupportException;

import rx.Observable;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 13:48
 * @description
 */
public interface ICommand {

    int EXEC_FAILURE = 0;
    int EXEC_SCHEDULED = 1;
    int EXEC_SUCCESS = 2;

    @IntDef({EXEC_FAILURE, EXEC_SCHEDULED, EXEC_SUCCESS})
    @interface RESULT {
    }

    /**
     * @return
     */
    @RESULT
    int execute() throws CmdNotSupportException;

    /**
     * @return
     */
    void executeAsync(final ICmdCallback callback) throws CmdNotSupportException;

    /**
     * @return
     */
    Observable<Integer> executeObservable();

    /**
     * @param node
     * @return
     */
    ICommand addNode(AccessibilityNodeInfo node);

    /**
     * @param ui
     * @return
     */
    ICommand addUi(PurposeUiInfo ui);

    /**
     * @param service
     * @return
     */
    ICommand addService(AccessibilityService service);
}
