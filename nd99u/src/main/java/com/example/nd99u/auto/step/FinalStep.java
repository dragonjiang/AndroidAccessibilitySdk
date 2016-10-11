package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.BackCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.HomeCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ClassNameFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ContentFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.Constants;
import com.example.nd99u.auto.AutoMachine;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class FinalStep implements IStep {
    private static final String TAG = "FinalState";

    private AutoMachine mMachine;
    private int mFailureCnt = 0;
    private Subscription mSubscription;

    public final PurposeUiInfo mUpdateDlgUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("发现新版本", "取消", "立即更新")
            .setPreFilters("发现新版本")
            .setNextFilters("立即更新")
            .setPurposeFilters(new ContentFilter("取消"), new ClassNameFilter("android.widget.Button"))
            .setCommand(new ClickCmd())
            .build();

    public final PurposeUiInfo mLotteryDlgUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("恭喜", "抽奖", "抽奖", "以后再抽")
            .setPreFilters("恭喜", "抽奖")
            .setPurposeFilters(new ContentFilter("以后再抽"), new ClassNameFilter("android.widget.Button"))
            .setCommand(new ClickCmd())
            .build();

    public FinalStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mMachine.getLatestSuccessStep() != null
                && mMachine.getLatestSuccessStep().toString().equals(mMachine.getLastStep().toString())) {
            mMachine.setResult(Constants.RUN_RESULT.SUCCESS);
            mMachine.reset();
            mMachine.setNextStep(mMachine.getExitStep(), true);
            if(mSubscription != null && !mSubscription.isUnsubscribed()){
                mSubscription.unsubscribe();
            }
            return;
        }

        mMachine.dealUiObservable(mUpdateDlgUiInfo).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i(TAG, this.toString() + " dealUpdateDlgUiInfo " + throwable.getLocalizedMessage());
            }
        });

        mMachine.dealUiObservable(mLotteryDlgUiInfo).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.i(TAG, this.toString() + " dealLotteryDlgUiInfo  " + throwable.getLocalizedMessage());
            }
        });

        IStep next = mMachine.getLatestSuccessStep() == null ? mMachine.getFirstStep() : mMachine
                .getLatestSuccessStep();
        Log.i(TAG, this.toString() + " dealFinalState go to state " + next.toString());
        mMachine.setNextStep(next, false);


        Log.i(TAG, "FailureCnt = " + mFailureCnt);
        //如果失败次数太多，回退次数就增加
        if (mFailureCnt > 5) {
            int n = mFailureCnt - 5;
            for (int i = 0; i <= n; i++) {
                mMachine.execCmd(new BackCmd());
            }
        }

        if(mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        //如果30s不动，就执行一下回退
        mSubscription = Observable.timer(30, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mMachine.execCmd(new BackCmd());
                mFailureCnt++;
                Log.i(TAG, "dealFinalState back OK!");
            }
        });
    }

    @Override
    public void reset() {
        mFailureCnt = 0;
        mMachine.setLatestSuccessStep(null);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
