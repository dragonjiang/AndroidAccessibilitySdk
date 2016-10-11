package com.demo.dragonjiang.accessilibility_sdk.core.filter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 17:01
 * @description
 */
public class AreaFilter extends Filter<RectF> {
    private static final String TAG = "AreaFilter";

    private final int mScreenW;
    private final int mScreenH;

    public AreaFilter(RectF purpose) {
        super(purpose);

        WindowManager wm = (WindowManager) ContextUtils.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenW = wm.getDefaultDisplay().getWidth();
        mScreenH = wm.getDefaultDisplay().getHeight();
        Log.i(TAG, "screenWdth = " + mScreenW + "   screenHeight = " + mScreenH);
    }

    @Override
    public boolean match(AccessibilityNodeInfo node) {
        if (node == null) {
            return false;
        }

        if (mPurpose == null) {
            return false;
        }

        Rect r = new Rect();
        node.getBoundsInScreen(r);

        int left = (int) (mPurpose.left * mScreenW);
        int top = (int) (mPurpose.top * mScreenH);
        int right = (int) (mPurpose.right * mScreenW);
        int bottom = (int) (mPurpose.bottom * mScreenH);
        Log.i(TAG, "area no match" + mPurpose.toString() + "        " + mScreenH);

        Rect purpose = new Rect(left, top, right, bottom);

        if (r.top >= purpose.top && r.right <= purpose.right && r.bottom <= purpose.bottom && r.left >= purpose.left) {
            Log.i(TAG, purpose.toString());
            return true;
        }
        Log.i(TAG, "area no match" + purpose.toString() + "        " + r.toString());

        return false;
    }
}
