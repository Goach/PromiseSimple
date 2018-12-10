package com.goach.lib.promise.core;

import com.goach.lib.promise.core.impl.Deferred;
import com.goach.lib.promise.core.impl.DeferredObject;
import com.goach.lib.promise.core.DeferredManager.StartPolicy;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:42.
 * Des: 状态模式，学习下
 */
public abstract class DeferredRunnable<P> implements Runnable {
    private final Deferred<Void,Throwable,P> deferred = new DeferredObject<>();
    private final StartPolicy startPolicy;

    public DeferredRunnable() {
        this.startPolicy = DeferredManager.StartPolicy.DEFAULT;
    }
    public DeferredRunnable(DeferredManager.StartPolicy startPolicy) {
        this.startPolicy = startPolicy;
    }
    protected void notify(P progress) {
        deferred.notify(progress);
    }

    protected Deferred<Void, Throwable, P> getDeferred() {
        return deferred;
    }

    public StartPolicy getStartPolicy() {
        return startPolicy;
    }
}
