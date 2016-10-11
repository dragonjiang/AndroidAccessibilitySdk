package com.example.nd99u.auto.step;

import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.PurposeUiInfo;
import com.demo.dragonjiang.accessilibility_sdk.core.command.accessibilityCmd.ClickCmd;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.AreaFilter;
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
public class Launch2HomeStep implements IStep {
    private static final String TAG = "Launch2HomeStep";

    private AutoMachine mMachine;

    public final PurposeUiInfo mUiInfo = new PurposeUiInfo.Builder()
            .setFeatureFilters("聊天", "ERP", "学习", "广场", "我")
            .setNextFilters("ERP", "学习", "广场", "我")
            .setPurposeFilters(new ContentFilter("聊天"), new AreaFilter(new RectF(0, 0.8f, 0.3f, 1)))
            .setCommand(new ClickCmd())
            .build();

    public Launch2HomeStep(AutoMachine machine) {
        this.mMachine = machine;
    }

    @Override
    public void dealStep() {

        //获取需要送花的名字，没有名字就跳过送花
        final IStep next = TextUtils.isEmpty(mMachine.getFlowerName()) ? mMachine.getHome2ErpStep() : mMachine.getHome2SearchStep();

        mMachine.dealUiObservable(mUiInfo).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, this.toString() + " Ok! go to step " + next.toString());

                mMachine.setLatestSuccessStep(mMachine.getLaunch2HomeStep());
                mMachine.setNextStep(next, false);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, this.toString() + " onError: " + e.getLocalizedMessage() + " go to step " + next.toString());

                mMachine.setNextStep(next, true);
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
