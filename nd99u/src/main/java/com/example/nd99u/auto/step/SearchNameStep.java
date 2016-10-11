package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.InputCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ClassNameFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ContentFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import rx.Subscriber;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class SearchNameStep implements IStep {
    private static final String TAG = "SearchNameStep";

    private AutoMachine mMachine;

    private boolean mIsDealing = false;
    private boolean mIsSuccess = false;

    public PurposeUiInfo mUiInfo;

    public SearchNameStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getSearch2ChatStep(), true);
            return;
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        mUiInfo = new PurposeUiInfo.Builder()
                .setFeatureFilters(new ContentFilter("转到上一层级"), new ClassNameFilter("android.widget.EditText"),
                        new ContentFilter("清除查询"))
                .setPreFilters(new ContentFilter("转到上一层级"))
                .setNextFilters(new ContentFilter("清除查询"))
                .setPurposeFilters(new ClassNameFilter("android.widget.EditText"))
                .setInputText(mMachine.getFlowerName())
                .setCommand(new InputCmd())
                .build();

        mIsDealing = true;

        mMachine.dealUiObservable(mUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getSearch2ChatStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getSearchNameStep());
                mMachine.setNextStep(mMachine.getSearch2ChatStep(), false);
                mIsDealing = false;
                mIsSuccess = true;
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getSearch2ChatStep().toString());

                mIsDealing = false;
                mMachine.setNextStep(mMachine.getSearch2ChatStep(), true);
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
