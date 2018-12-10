package com.goach.lib.promise.core;

import com.goach.lib.promise.core.impl.Deferred;
import com.goach.lib.promise.core.impl.DeferredObject;
import com.goach.lib.promise.core.DeferredManager.StartPolicy;
import java.util.concurrent.Callable;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 11:06.
 * Des:
 */
public abstract class DeferredCallable<D,P> implements Callable<D> {
    private final Deferred<D, Throwable, P> deferred = new DeferredObject<D, Throwable, P>();
    private final StartPolicy startPolicy;

    public DeferredCallable() {
        this.startPolicy = StartPolicy.DEFAULT;
    }

    public DeferredCallable(StartPolicy startPolicy) {
        this.startPolicy = startPolicy;
    }
    protected void notify(P progress) {
        deferred.notify(progress);
    }

    protected Deferred<D, Throwable, P> getDeferred() {
        return deferred;
    }

    public StartPolicy getStartPolicy() {
        return startPolicy;
    }
}
