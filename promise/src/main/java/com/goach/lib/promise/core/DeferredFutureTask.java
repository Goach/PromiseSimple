package com.goach.lib.promise.core;

import com.goach.lib.promise.core.impl.Deferred;
import com.goach.lib.promise.core.DeferredManager.StartPolicy;
import com.goach.lib.promise.core.impl.DeferredObject;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:34.
 * Des:
 */
public class DeferredFutureTask<D,P> extends FutureTask<D> {
    protected final Deferred<D,Throwable,P> deferred;
    protected final StartPolicy startPolicy;
    public DeferredFutureTask(Callable<D> callable) {
        super(callable);
        this.deferred = new DeferredObject<>();
        this.startPolicy = StartPolicy.DEFAULT;
    }

    public DeferredFutureTask(Runnable runnable) {
        super(runnable, null);
        this.deferred = new DeferredObject<>();
        this.startPolicy = StartPolicy.DEFAULT;
    }
    public DeferredFutureTask(DeferredCallable<D, P> callable) {
        super(callable);
        this.deferred = callable.getDeferred();
        this.startPolicy = callable.getStartPolicy();
    }

    @SuppressWarnings("unchecked")
    public DeferredFutureTask(DeferredRunnable<P> runnable) {
        super(runnable, null);
        this.deferred = (Deferred<D, Throwable, P>) runnable.getDeferred();
        this.startPolicy = runnable.getStartPolicy();
    }
    public Promise<D, Throwable, P> promise() {
        return deferred.promise();
    }

    @Override
    protected void done() {
        try {
            if (isCancelled()) {
                deferred.reject(new CancellationException());
            }
            D result = get();
            deferred.resolve(result);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            deferred.reject(e.getCause());
        }
    }

    public StartPolicy getStartPolicy() {
        return startPolicy;
    }
}
