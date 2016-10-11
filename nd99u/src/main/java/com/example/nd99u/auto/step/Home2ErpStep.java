package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import rx.Subscriber;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class Home2ErpStep implements IStep {
    private static final String TAG = "Home2ErpStep";

    private AutoMachine mMachine;

    public final PurposeUiInfo mHomeUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("聊天", "ERP", "学习", "广场", "我")
            .setPreFilters("聊天")
            .setNextFilters("学习", "广场", "我")
            .setPurposeFilters("ERP")
            .setCommand(new ClickCmd())
            .build();

    public Home2ErpStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        mMachine.dealUiObservable(mHomeUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getErp2SignInStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getHome2ErpStep());
                mMachine.setNextStep(mMachine.getErp2SignInStep(), false);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " +
                        mMachine.getErp2SignInStep().toString());

                mMachine.setNextStep(mMachine.getErp2SignInStep(), true);
            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }

    @Override
    public void reset() {

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
