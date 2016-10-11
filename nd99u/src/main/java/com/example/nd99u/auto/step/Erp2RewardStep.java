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
public class Erp2RewardStep implements IStep {
    private static final String TAG = "Erp2RewardStep";

    private AutoMachine mMachine;

    public final PurposeUiInfo mRewardUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("办公平台", "更多奖励", "时间表", "任务", "审批", "消息")
            .setPreFilters("办公平台")
            .setNextFilters("时间表", "任务", "审批", "消息")
            .setPurposeFilters("更多奖励")
            .setCommand(new ClickCmd())
            .build();

    public Erp2RewardStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        mMachine.dealUiObservable(mRewardUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getReward2ClearStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getErp2RewardStep());
                mMachine.setNextStep(mMachine.getReward2ClearStep(), false);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getReward2ClearStep().toString());

                mMachine.setNextStep(mMachine.getReward2ClearStep(), true);
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
