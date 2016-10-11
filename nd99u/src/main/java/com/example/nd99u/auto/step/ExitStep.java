package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.BackCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.HomeCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd.ShellBackCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class ExitStep implements IStep {
    private static final String TAG = "ExitStep";

    private AutoMachine mMachine;

    public ExitStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mMachine.execCmdObservable(new ShellBackCmd()).subscribe();
                mMachine.execCmdObservable(new ShellBackCmd()).subscribe();
                mMachine.execCmdObservable(new ShellBackCmd()).subscribe();
//                mMachine.execCmd(new BackCmd());
//                mMachine.execCmd(new BackCmd());
//                mMachine.execCmd(new BackCmd());
//                mMachine.execCmd(new HomeCmd());

                Log.i(TAG, "dealExitStep Ok!");
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
