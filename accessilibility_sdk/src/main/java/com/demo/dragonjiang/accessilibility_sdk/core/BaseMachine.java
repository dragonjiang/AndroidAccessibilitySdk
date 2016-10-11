package com.demo.dragonjiang.accessilibility_sdk.core;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.CmdExecuteFailureException;
import com.demo.dragonjiang.accessilibility_sdk.core.command.CmdExecutor;
import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.NotMatchUiException;
import com.demo.dragonjiang.accessilibility_sdk.core.filter.Filter;
import com.demo.dragonjiang.accessilibility_sdk.core.step.IStep;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:43
 * @description
 */
public abstract class BaseMachine implements IMachine {

    protected static final String TAG = "BaseMachine";

    protected List<AccessibilityNodeInfo> mPageNodes;

    protected AccessibilityService mContext;
    protected IStep mCurStep;
    protected IStep mLatestSuccessStep;
    protected IStep mFirstStep;
    protected IStep mLastStep;

    public BaseMachine(AccessibilityService context) {
        mContext = context;
        init();
    }

    protected void init() {
        mPageNodes = new ArrayList<>();
    }

    public void setNextStep(IStep state, boolean goNextImmediately) {
        mCurStep = state;

        if (goNextImmediately) {
            mCurStep.dealStep();
        }
    }

    public IStep getCurStep() {
        return mCurStep;
    }

    public IStep getLatestSuccessStep() {
        return mLatestSuccessStep;
    }

    /**
     * @param step
     */
    public void setLatestSuccessStep(IStep step) {
        this.mLatestSuccessStep = step;
    }

    public IStep getFirstStep() {
        return mFirstStep;
    }

    public IStep getLastStep() {
        return mLastStep;
    }

    /**
     * print all children nodes of input node
     *
     * @param node
     */
    protected void printChild(final AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }

        Log.i(TAG, "child: " + node.getClassName() + ":" + node.getText() + "   " + node.toString());

        int cnt = node.getChildCount();
        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                printChild(node.getChild(i));
            }
        }
    }


    /**
     * gather all nodes of this page
     *
     * @param root
     */
    protected void gatherPageNodes(@NonNull AccessibilityNodeInfo root) {
        if (mPageNodes == null) {
            mPageNodes = new ArrayList<>();
        } else {
            mPageNodes.clear();
        }

        gatherPageNodesRecursion(root, mPageNodes);
    }


    /**
     * gather all nodes by recursion
     *
     * @param node
     * @param nodeList
     */
    private void gatherPageNodesRecursion(final AccessibilityNodeInfo node, final List<AccessibilityNodeInfo>
            nodeList) {

        if (node == null || nodeList == null) {
            return;
        }

        nodeList.add(node);
        Log.i(TAG, "gather index : " + nodeList.size() + "  " + node.getClassName() + ":" + node.getText() + "   "
                + node.toString());

        int cnt = node.getChildCount();
        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                gatherPageNodesRecursion(node.getChild(i), nodeList);
            }
        }
    }

    /**
     * search node in nodeList by compare the input text
     *
     * @param nodeList
     * @param filter
     * @return
     */
    protected AccessibilityNodeInfo searchNode(final List<AccessibilityNodeInfo> nodeList, final Filter filter) {

        if (filter == null) {
            return null;
        }

        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }

        for (AccessibilityNodeInfo node : nodeList) {
            if (filter.match(node)) {
                return node;
            }
        }

        return null;
    }


    /**
     * search node in mPageNodes by some condition defined in PurposeUiInfo
     *
     * @param nodeList
     * @param purposeFilters
     * @param preFilters
     * @param nextFilters
     * @return
     */
    protected AccessibilityNodeInfo searchNode(final List<AccessibilityNodeInfo> nodeList, final Filter[]
            purposeFilters,
                                               final Filter[] preFilters, final Filter[] nextFilters) {

        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }

        if (purposeFilters == null) {
            return null;
        }

        int size = nodeList.size();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo node = nodeList.get(i);
            Log.i(TAG, "searching : " + node.toString());

            if (PurposeUiHelper.match(purposeFilters, node)) {
                boolean p = checkPrevious(i, preFilters);
                boolean n = checkNext(i, nextFilters);
                if (p && n) {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * search node in mPageNodes by some condition defined in PurposeUiInfo
     *
     * @param ui
     * @return
     */
    protected AccessibilityNodeInfo searchNode(final PurposeUiInfo ui) {
        if (ui == null || ui.purposeFilters == null) {
            return null;
        }

        if (mPageNodes.isEmpty()) {
            return null;
        }

        int size = mPageNodes.size();
        for (int i = 0; i < size; i++) {
            AccessibilityNodeInfo node = mPageNodes.get(i);
            Log.i(TAG, "searching : " + node.toString());

            if (PurposeUiHelper.match(ui.purposeFilters, node)) {
                boolean p = checkPrevious(i, ui.preFilters);
                boolean n = checkNext(i, ui.nextFilters);
                if (p && n) {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * check whether has nodes' text the same as args in mPageNodes(0--position)
     *
     * @param position
     * @param filters
     * @return
     */
    protected boolean checkPrevious(int position, Filter[] filters) {
        if (filters == null || filters.length == 0) {
            return true;
        }

        if (position >= mPageNodes.size()) {
            return false;
        }

        List<AccessibilityNodeInfo> nodes = mPageNodes.subList(0, position);
        for (int i = filters.length - 1; i >= 0; i--) {

            if (searchNode(nodes, filters[i]) == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * check whether has nodes' text the same as args in mPageNodes(position+1 -- length)
     *
     * @param position
     * @param filters
     * @return
     */
    protected boolean checkNext(int position, Filter[] filters) {
        if (filters == null || filters.length == 0) {
            return true;
        }

        if (position >= mPageNodes.size() || position < 0) {
            return false;
        }

        List<AccessibilityNodeInfo> nodes = mPageNodes.subList(position + 1, mPageNodes.size());
        for (int i = filters.length - 1; i >= 0; i--) {

            if (searchNode(nodes, filters[i]) == null) {
                return false;
            }
        }

        return true;
    }


    /**
     * check this is the page we want by the input args
     *
     * @param filters
     * @return
     */
    public boolean checkUi(final Filter[] filters) {
        if (filters == null || filters.length == 0) {
            return false;
        }

        for (Filter filter : filters) {
            if (searchNode(mPageNodes, filter) == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * search the purpose node and execute cmd observable
     *
     * @param ui
     * @return
     */
    public Observable<Integer> dealUiObservable(@NonNull final PurposeUiInfo ui) {
        return Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(checkUi(ui.featureFilters))
                        .flatMap(new Func1<Boolean, Observable<? extends Integer>>() {
                            @Override
                            public Observable<Integer> call(Boolean aBoolean) {
                                if (aBoolean) {
                                    if (ui.command == null) {
                                        return Observable.error(new CmdExecuteFailureException());
                                    }

                                    AccessibilityNodeInfo node = searchNode(ui);
                                    if (node != null) {
                                        return CmdExecutor.executeObservable(ui.command.addNode(node).addUi(ui));
                                    }
                                }

                                return Observable.error(new NotMatchUiException());
                            }
                        });
            }
        });
    }

    /**
     * execute the cmd sync
     *
     * @param cmd
     * @return
     */
    public
    @ICommand.RESULT
    int execCmd(@NonNull ICommand cmd) {
        return CmdExecutor.execute(cmd.addService(mContext));
    }

    /**
     * execute the cmd observable
     *
     * @param cmd
     * @return
     */
    public Observable<Integer> execCmdObservable(@NonNull ICommand cmd) {
        return CmdExecutor.executeObservable(cmd.addService(mContext));
    }

    /**
     * execute the cmd async and observable
     *
     * @param cmd
     * @return
     * @deprecated
     */
    public Observable<Integer> execCmdObservable(@NonNull ICommand cmd, long delay, boolean canBeCancel) {
        return CmdExecutor.executeCancelable(cmd.addService(mContext), delay, canBeCancel);
    }
}
