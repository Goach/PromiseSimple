package com.goach.lib.promise.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

import com.goach.lib.promise.android.AndroidDeferredManager;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 14:12.
 * Des:
 */
public class VUiKit {
    private static final AndroidDeferredManager gDM = new AndroidDeferredManager();
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

    public static AndroidDeferredManager defer() {
        return gDM;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

