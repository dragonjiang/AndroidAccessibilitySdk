package com.example.nd99u.auto.step;

import android.graphics.RectF;
import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.AreaFilter;
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
public class Home2SearchStep implements IStep {
    private static final String TAG = "Home2SearchStep";

    private AutoMachine mMachine;

    public final PurposeUiInfo mHomeUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("聊天", "聊天", "ERP", "学习", "广场", "我")
            .setPreFilters(new ContentFilter("聊天"), new ClassNameFilter("android.widget.TextView"))
            .setNextFilters(new ClassNameFilter("android.widget.TextView"),
                    new ClassNameFilter("android.widget.RelativeLayout"),
                    new ContentFilter("聊天"), new ContentFilter("ERP"))
            .setPurposeFilters(new ClassNameFilter("android.widget.TextView"),
                    new AreaFilter(new RectF(0.7f, 0, 0.98f, 0.2f)))
            .setCommand(new ClickCmd())
            .build();

    public Home2SearchStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        mMachine.dealUiObservable(mHomeUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + mMachine.getSearchNameStep().toString());

                mMachine.setLatestSuccessStep(mMachine.getHome2SearchStep());
                mMachine.setNextStep(mMachine.getSearchNameStep(), false);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + mMachine.getSearchNameStep().toString());

                mMachine.setNextStep(mMachine.getSearchNameStep(), true);
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
