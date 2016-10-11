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
public class Erp2SignInStep implements IStep {
    private static final String TAG = "Erp2SignInStep";

    private AutoMachine mMachine;

    private boolean mIsDealing;
    private boolean mIsSuccess;

    public final PurposeUiInfo mSignInUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("办公平台", "签到", "时间表", "任务", "审批", "消息")
            .setPreFilters("办公平台")
            .setNextFilters("时间表", "任务", "审批", "消息")
            .setPurposeFilters("签到")
            .setCommand(new ClickCmd())
            .build();

    public Erp2SignInStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getErp2RewardStep(), true);
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        mIsDealing = true;

        mMachine.dealUiObservable(mSignInUiInfo)
                .delaySubscription(3000, TimeUnit.MILLISECONDS)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == ICommand.EXEC_SUCCESS;
                    }
                })
                .flatMap(new Func1<Integer, Observable<? extends Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return mMachine.execCmdObservable(new BackCmd()).delaySubscription(3000, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getErp2RewardStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getErp2SignInStep());
                mMachine.setNextStep(mMachine.getErp2RewardStep(), false);
                mIsDealing = false;
                mIsSuccess = true;
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getErp2RewardStep().toString());

                mIsDealing = false;
                mIsSuccess = false;
                mMachine.setNextStep(mMachine.getErp2RewardStep(), true);
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
