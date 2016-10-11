package com.example.nd99u.auto;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.BaseMachine;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;
import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;
import com.demo.dragonjiang.accessilibility_sdk.utils.SPUtils;
import com.example.nd99u.Constants;
import com.example.nd99u.auto.step.BlessingStep;
import com.example.nd99u.auto.step.Chat2FlowerStep;
import com.example.nd99u.auto.step.Erp2RewardStep;
import com.example.nd99u.auto.step.Erp2SignInStep;
import com.example.nd99u.auto.step.ExitStep;
import com.example.nd99u.auto.step.FinalStep;
import com.example.nd99u.auto.step.Home2ErpStep;
import com.example.nd99u.auto.step.Home2SearchStep;
import com.example.nd99u.auto.step.Launch2HomeStep;
import com.example.nd99u.auto.step.Reward2BlessStep;
import com.example.nd99u.auto.step.Reward2ClearStep;
import com.example.nd99u.auto.step.Search2ChatStep;
import com.example.nd99u.auto.step.SearchNameStep;
import com.example.nd99u.auto.step.SendFlowerStep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:43
 * @description
 */
public class AutoMachine extends BaseMachine {

    private static final String TAG = "AutoMachine";

    private IStep mLaunch2HomeStep;
    private IStep mHome2SearchStep;
    private IStep mSearchNameStep;
    private IStep mSearch2ChatStep;
    private IStep mChat2FlowerStep;
    private IStep mSendFlowerStep;
    private IStep mHome2ErpStep;
    private IStep mErp2SignInStep;
    private IStep mErp2RewardStep;
    private IStep mReward2ClearStep;
    private IStep mReward2BlessStep;
    private IStep mBlessingStep;
    private IStep mExitStep;
    private IStep mFinalStep;

    private List<String> mFlowerName;


    public AutoMachine(AccessibilityService context) {
        super(context);
    }

    @Override
    public void dealEvent(@NonNull final AccessibilityEvent event, @NonNull final AccessibilityNodeInfo root) {

        if (hasSuccess()) {
            return;
        }

        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                && event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                && event.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED
                && event.getEventType() != AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            return;
        }

        gatherPageNodes(root);
        mCurStep.dealStep();
    }

    @Override
    public void reset() {
        mLaunch2HomeStep.reset();
        mHome2SearchStep.reset();
        mSearchNameStep.reset();
        mSearch2ChatStep.reset();
        mChat2FlowerStep.reset();
        mSendFlowerStep.reset();
        mHome2ErpStep.reset();
        mErp2SignInStep.reset();
        mErp2RewardStep.reset();
        mReward2ClearStep.reset();
        mReward2BlessStep.reset();
        mBlessingStep.reset();
        mExitStep.reset();
        mFinalStep.reset();

        mCurStep = mLaunch2HomeStep;
        mFirstStep = mLaunch2HomeStep;
        mLastStep = mBlessingStep;

        if (mFlowerName == null) {
            mFlowerName = new ArrayList<>();
        } else {
            mFlowerName.clear();
        }
        String nameStr = (String) SPUtils.get(mContext, Constants.SP_KEY.NAME, "");
        if (!TextUtils.isEmpty(nameStr)) {
            String[] names = nameStr.trim().split(" ");
            int size = names.length;
            for (int i = 0; i < size; i++) {
                mFlowerName.add(names[i]);
            }
        }
    }


    protected void init() {
        super.init();

        mLaunch2HomeStep = new Launch2HomeStep(this);
        mHome2SearchStep = new Home2SearchStep(this);
        mSearchNameStep = new SearchNameStep(this);
        mSearch2ChatStep = new Search2ChatStep(this);
        mChat2FlowerStep = new Chat2FlowerStep(this);
        mSendFlowerStep = new SendFlowerStep(this);
        mHome2ErpStep = new Home2ErpStep(this);
        mErp2SignInStep = new Erp2SignInStep(this);
        mErp2RewardStep = new Erp2RewardStep(this);
        mReward2ClearStep = new Reward2ClearStep(this);
        mReward2BlessStep = new Reward2BlessStep(this);
        mBlessingStep = new BlessingStep(this);
        mExitStep = new ExitStep(this);
        mFinalStep = new FinalStep(this);

        reset();
    }

    public IStep getLaunch2HomeStep() {
        return mLaunch2HomeStep;
    }

    public IStep getHome2SearchStep() {
        return mHome2SearchStep;
    }

    public IStep getSearchNameStep() {
        return mSearchNameStep;
    }

    public IStep getSearch2ChatStep() {
        return mSearch2ChatStep;
    }

    public IStep getChat2FlowerStep() {
        return mChat2FlowerStep;
    }

    public IStep getSendFlowerStep() {
        return mSendFlowerStep;
    }

    public IStep getHome2ErpStep() {
        return mHome2ErpStep;
    }

    public IStep getErp2SignInStep() {
        return mErp2SignInStep;
    }

    public IStep getErp2RewardStep() {
        return mErp2RewardStep;
    }

    public IStep getReward2ClearStep() {
        return mReward2ClearStep;
    }

    public IStep getReward2BlessStep() {
        return mReward2BlessStep;
    }

    public IStep getBlessingStep() {
        return mBlessingStep;
    }

    public IStep getExitStep() {
        return mExitStep;
    }

    public IStep getFinalStep() {
        return mFinalStep;
    }

    public void resetFlowerStep() {
        mSearchNameStep.reset();
        mSearch2ChatStep.reset();
        mChat2FlowerStep.reset();
        mSendFlowerStep.reset();
        mLatestSuccessStep = mSearchNameStep;
        mCurStep = mSearchNameStep;
    }

    public String getFlowerName() {
        if (mFlowerName.isEmpty()) {
            return "";
        }

        return this.mFlowerName.get(0);
    }

    public void removeFlowerName() {
        if (mFlowerName.isEmpty()) {
            return;
        }

        mFlowerName.remove(0);
    }

    public boolean isLastFlowerName() {
        return mFlowerName.size() == 1;
    }

    public void setResult(int result) {
        SPUtils.put(ContextUtils.getAppContext(), Constants.SP_KEY.RESULT, result);
    }

    public boolean hasSuccess() {
        return (int)(SPUtils.get(ContextUtils.getAppContext(), Constants.SP_KEY.RESULT, Constants.RUN_RESULT.NONE)) ==
                Constants.RUN_RESULT.SUCCESS;
    }


}
