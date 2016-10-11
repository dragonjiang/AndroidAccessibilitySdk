package com.example.nd99u.auto.step;

import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ScrollForwardCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ClassNameFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.ContentFilter;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.example.nd99u.auto.AutoMachine;

import rx.Subscriber;
import rx.functions.Func1;


/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:40
 * @description
 */
public class BlessingStep implements IStep {
    private static final String TAG = "BlessingStep";

    private AutoMachine mMachine;

    private boolean mIsDealing;
    private boolean mIsSuccess;
    private boolean mIsScrollSuccess;


    public final PurposeUiInfo mBlessingUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("生日祝福", "农历", "祝福")
            .setPreFilters("生日祝福")
            .setPurposeFilters(new ContentFilter("祝福"), new ClassNameFilter("android.widget.Button"))
            .setCommand(new ClickCmd())
            .build();


    public final PurposeUiInfo mScrollUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("生日祝福", "农历")
            .setPreFilters("生日祝福")
            .setNextFilters(new ClassNameFilter("LinearLayout"),
                    new ClassNameFilter("ImageView"),
                    new ClassNameFilter("TextView"))
            .setPurposeFilters(new ClassNameFilter("android.support.v7.widget.RecyclerView"))
            .setCommand(new ScrollForwardCmd())
            .build();

    public BlessingStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        if (mIsSuccess) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }

        if (mIsDealing) {
            mMachine.setNextStep(mMachine.getFinalStep(), true);
            return;
        }


        mIsDealing = true;

        mMachine.dealUiObservable(mBlessingUiInfo)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer == ICommand.EXEC_SUCCESS;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getFinalStep().toString());

                        mMachine.setNextStep(mMachine.getFinalStep(), false);
                        mIsDealing = false;
                        mIsScrollSuccess = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to scroll");

                        if (!mIsScrollSuccess) {
                            Log.i(TAG, " go to scroll");

                            scroll();

                        } else {
                            Log.i(TAG, " setLatestSuccessStep " + mMachine.getBlessingStep());

                            mIsSuccess = true;
                            mIsDealing = false;
                            mMachine.setLatestSuccessStep(mMachine.getBlessingStep());
                        }
                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }

    private void scroll() {
        mMachine.dealUiObservable(mScrollUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Scroll Ok! go to step " + mMachine.getBlessingStep().toString());

                mMachine.setNextStep(mMachine.getBlessingStep(), false);
                mIsDealing = false;
                mIsScrollSuccess = true;
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Scroll onError: " + e.getLocalizedMessage() + " go to step " + mMachine
                        .getFinalStep().toString());

                mIsDealing = false;
                mMachine.setNextStep(mMachine.getFinalStep(), true);
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
        mIsScrollSuccess = false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
