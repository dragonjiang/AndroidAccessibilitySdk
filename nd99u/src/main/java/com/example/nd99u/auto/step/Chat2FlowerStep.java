package com.example.nd99u.auto.step;

import android.graphics.RectF;
import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.AreaFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ClassNameFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import rx.Subscriber;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class Chat2FlowerStep implements IStep {
    private static final String TAG = "Chat2FlowerStep";

    private AutoMachine mMachine;

    private boolean mIsDealing = false;
    private boolean mIsSuccess = false;

    public PurposeUiInfo mUiInfo;

    public Chat2FlowerStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if(mIsSuccess){
            mMachine.setNextStep(mMachine.getSendFlowerStep(), true);
            return;
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        String name = mMachine.getFlowerName();
        Log.i(TAG, "send flower name : "+name);
        mUiInfo = new PurposeUiInfo.Builder()
                .setFeatureFilters("转到上一层级", name, "查看详情", "花吧")
                .setPreFilters("转到上一层级", "查看详情", "花吧")
                .setPurposeFilters(new ClassNameFilter("android.widget.TextView"),
                        new AreaFilter(new RectF(0.8f, 0.05f, 1, 0.3f)))
                .setCommand(new ClickCmd())
                .build();


        mIsDealing = true;

        mMachine.dealUiObservable(mUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getFinalStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getChat2FlowerStep());
                mMachine.setNextStep(mMachine.getSendFlowerStep(), false);
                mIsSuccess = true;
                mIsDealing = false;
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getSendFlowerStep().toString());
                mIsDealing = false;
                mMachine.setNextStep(mMachine.getSendFlowerStep(), true);
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
