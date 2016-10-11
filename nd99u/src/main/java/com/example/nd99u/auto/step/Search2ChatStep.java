package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class Search2ChatStep implements IStep {
    private static final String TAG = "Search2ChatStep";

    private AutoMachine mMachine;

    private boolean mIsDealing = false;
    private boolean mIsSuccess = false;

    public PurposeUiInfo mUiInfo;

    public Search2ChatStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getChat2FlowerStep(), true);
            return;
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        String name = mMachine.getFlowerName();
        Log.i(TAG, "send flower name : "+name);
        mUiInfo = new PurposeUiInfo.Builder()
                .setFeatureFilters("转到上一层级", name, "清除查询", name)
                .setPreFilters("转到上一层级", name, "清除查询")
                .setPurposeFilters(name)
                .setCommand(new ClickCmd())
                .build();

        mIsDealing = true;

        mMachine.dealUiObservable(mUiInfo)
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getChat2FlowerStep().toString());

                        mMachine.setLatestSuccessStep(mMachine.getSearch2ChatStep());
                        mMachine.setNextStep(mMachine.getChat2FlowerStep(), false);
                        mIsDealing = false;
                        mIsSuccess = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getChat2FlowerStep().toString());

                        mIsDealing = false;
                        mMachine.setNextStep(mMachine.getChat2FlowerStep(), true);
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }

    @Override
    public void reset() {
        mIsSuccess = false;
        mIsDealing = false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
