package com.demo.dragonjiang.accessilibility_sdk.core.command;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdNotSupportException;

import rx.Observable;
import rx.Subscriber;


/**
 * @author DragonJiang
 * @Date 2016/8/2
 * @Time 10:37
 * @description
 */
public class BaseCmd implements ICommand {
    @Override
    public
    @RESULT
    int execute() throws CmdNotSupportException {
        throw new CmdNotSupportException();
    }

    @Override
    public void executeAsync(final ICmdCallback callback) throws CmdNotSupportException {
        throw new CmdNotSupportException();
    }

    public Observable<Integer> executeObservable() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int result = execute();
                    if (!subscriber.isUnsubscribed()) {
                        if (result == ICommand.EXEC_FAILURE) {
                            subscriber.onError(new CmdExecuteFailureException());
                        } else {
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        }
                    }
                } catch (CmdNotSupportException e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    @Override
    public ICommand addNode(AccessibilityNodeInfo node) {
        return this;
    }

    @Override
    public ICommand addUi(PurposeUiInfo ui) {
        return this;
    }

    @Override
    public ICommand addService(AccessibilityService service) {
        return this;
    }
}
