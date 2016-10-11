package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.BackCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class Reward2ClearStep implements IStep {
    private static final String TAG = "Reward2ClearStep";

    private AutoMachine mMachine;

    private boolean mIsDealing;
    private boolean mIsSuccess;

    public final PurposeUiInfo mUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("更多奖励", "参与以下活动", "日事日清奖励")
            .setPreFilters("更多奖励", "参与以下活动")
            .setPurposeFilters("日事日清奖励")
            .setCommand(new ClickCmd())
            .build();

    public Reward2ClearStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getReward2BlessStep(), true);
            return;
        }

        mIsDealing = true;

        mMachine.dealUiObservable(mUiInfo)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == ICommand.EXEC_SUCCESS;
                    }
                })
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return mMachine.execCmdObservable(new BackCmd()).delaySubscription(3000, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getReward2BlessStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getReward2ClearStep());
                mMachine.setNextStep(mMachine.getReward2BlessStep(), false);
                mIsDealing = false;
                mIsSuccess = true;
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getReward2BlessStep().toString());

                mMachine.setNextStep(mMachine.getReward2BlessStep(), true);
                mIsDealing = false;
            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }


    @Override
    public void reset() {
        mIsDealing = false;
        mIsSuccess = false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
