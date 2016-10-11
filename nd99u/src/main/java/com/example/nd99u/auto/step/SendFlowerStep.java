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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class SendFlowerStep implements IStep {
    private static final String TAG = "SendFlowerStep";

    private AutoMachine mMachine;

    private boolean mIsDealing = false;
    private boolean mIsSuccess = false;


    public PurposeUiInfo mUiInfo;

    public SendFlowerStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getHome2ErpStep(), true);
            return;
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }


        String name = mMachine.getFlowerName();
        Log.i(TAG, "send flower name : " + name);
        mUiInfo = new PurposeUiInfo.Builder()
                .setFeatureFilters("为" + name + "送花", "取消", "赠送")
                .setPreFilters("为" + name + "送花", "取消")
                .setPurposeFilters("赠送")
                .setCommand(new ClickCmd())
                .build();

        mIsDealing = true;

        mMachine.dealUiObservable(mUiInfo)
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == ICommand.EXEC_SUCCESS;
                    }
                })
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        boolean b = mMachine.isLastFlowerName();
                        Log.i(TAG, "isLastFlowerName : " + b);
                        return b;
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getFinalStep().toString());

                        mMachine.setNextStep(mMachine.getFinalStep(), true);
                        mIsDealing = false;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getHome2ErpStep().toString());

                            mMachine.setLatestSuccessStep(mMachine.getSendFlowerStep());
                            mMachine.setNextStep(mMachine.getHome2ErpStep(), false);
                            mIsDealing = false;
                            mIsSuccess = true;
                            mMachine.execCmdObservable(new BackCmd())
                                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                                    .delay(2000, TimeUnit.MILLISECONDS)
                                    .subscribe(new Subscriber<Integer>() {
                                        @Override
                                        public void onCompleted() {
                                            mMachine.execCmd(new BackCmd());
                                            mMachine.execCmd(new BackCmd());
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Integer integer) {

                                        }
                                    });
                        } else {
                            Log.i(TAG, this.toString() + " Ok! not last flower name ");

                            mMachine.removeFlowerName();
                            mMachine.resetFlowerStep();
                            Observable.timer(2000, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    mMachine.execCmd(new BackCmd());
                                }
                            });
                        }
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
