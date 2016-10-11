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
public class Reward2BlessStep implements IStep {
    private static final String TAG = "Reward2BlessStep";

    private AutoMachine mMachine;


    public final PurposeUiInfo mRewardUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("更多奖励", "参与以下活动", "生日祝福", "每日签到", "日事日清")
            .setPreFilters("更多奖励", "参与以下活动")
            .setNextFilters("每日签到", "日事日清")
            .setPurposeFilters("生日祝福")
            .setCommand(new ClickCmd())
            .build();

    public Reward2BlessStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        mMachine.dealUiObservable(mRewardUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getBlessingStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getReward2BlessStep());
                mMachine.setNextStep(mMachine.getBlessingStep(), false);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getBlessingStep().toString());

                mMachine.setNextStep(mMachine.getBlessingStep(), true);
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
