package com.demo.dragonjiang.accessilibility_sdk.core.step;

/**
 * @author DragonJiang
 * @Date 2016/8/3
 * @Time 18:28
 * @description
 */
public interface IStepCallback {
    void onNotMatchUi();

    void onScheduled();

    void onCancel();

    void onFailure();

    void onSuccess();
}
