package com.goach.lib.promise.core.impl;

import com.goach.lib.promise.core.DeferredCallable;
import com.goach.lib.promise.core.DeferredFutureTask;
import com.goach.lib.promise.core.DeferredManager;
import com.goach.lib.promise.core.DeferredRunnable;
import com.goach.lib.promise.core.Promise;
import com.goach.lib.promise.core.multiple.MasterDeferredObject;
import com.goach.lib.promise.core.multiple.MasterProgress;
import com.goach.lib.promise.core.multiple.MultipleResults;
import com.goach.lib.promise.core.multiple.OneReject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:43.
 * Des:
 */
public abstract class AbstractDeferredManager implements DeferredManager {
    protected abstract void submit(Runnable runnable);
    protected abstract void submit(Callable callable);

    public abstract boolean isAutoSubmit();

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(Runnable... runnables) {
        assertNotEmpty(runnables);

        Promise[] promises = new Promise[runnables.length];

        for (int i = 0; i < runnables.length; i++) {
            if (runnables[i] instanceof DeferredRunnable)
                promises[i] = when((DeferredRunnable) runnables[i]);
            else
                promises[i] = when(runnables[i]);
        }

        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(Callable<?>... callables) {
        assertNotEmpty(callables);

        Promise[] promises = new Promise[callables.length];

        for (int i = 0; i < callables.length; i++) {
            if (callables[i] instanceof DeferredCallable)
                promises[i] = when((DeferredCallable) callables[i]);
            else
                promises[i] = when(callables[i]);
        }

        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredRunnable<?>... runnables) {
        assertNotEmpty(runnables);

        Promise[] promises = new Promise[runnables.length];

        for (int i = 0; i < runnables.length; i++) {
            promises[i] = when(runnables[i]);
        }

        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredCallable<?, ?>... callables) {
        assertNotEmpty(callables);

        Promise[] promises = new Promise[callables.length];

        for (int i = 0; i < callables.length; i++) {
            promises[i] = when(callables[i]);
        }

        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(DeferredFutureTask<?, ?>... tasks) {
        assertNotEmpty(tasks);

        Promise[] promises = new Promise[tasks.length];

        for (int i = 0; i < tasks.length; i++) {
            promises[i] = when(tasks[i]);
        }
        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(Future<?>... futures) {
        assertNotEmpty(futures);

        Promise[] promises = new Promise[futures.length];

        for (int i = 0; i < futures.length; i++) {
            promises[i] = when(futures[i]);
        }
        return when(promises);
    }

    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(Promise... promises) {
        assertNotEmpty(promises);
        return new MasterDeferredObject(promises).promise();
    }

    @Override
    public <D, F, P> Promise<D, F, P> when(Promise<D, F, P> promise) {
        return promise;
    }

    @Override
    public <P> Promise<Void, Throwable, P> when(DeferredRunnable<P> runnable) {
        return when(new DeferredFutureTask<Void, P>(runnable));
    }

    @Override
    public <D, P> Promise<D, Throwable, P> when(DeferredCallable<D, P> runnable) {
        return when(new DeferredFutureTask<D, P>(runnable));
    }

    @Override
    public Promise<Void, Throwable, Void> when(Runnable runnable) {
        return when(new DeferredFutureTask<Void, Void>(runnable));
    }

    @Override
    public <D> Promise<D, Throwable, Void> when(Callable<D> callable) {
        return when(new DeferredFutureTask<D, Void>(callable));
    }

    @Override
    public <D, P> Promise<D, Throwable, P> when(
            DeferredFutureTask<D, P> task) {
        if (task.getStartPolicy() == StartPolicy.AUTO
                || (task.getStartPolicy() == StartPolicy.DEFAULT && isAutoSubmit()))
            submit(task);

        return task.promise();
    }

    @Override
    public <D> Promise<D, Throwable, Void> when(final Future<D> future) {
        // make sure the task is automatically started

        return when(new DeferredCallable<D, Void>(StartPolicy.AUTO) {
            @Override
            public D call() throws Exception {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    throw e;
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof Exception)
                        throw (Exception) e.getCause();
                    else throw e;
                }
            }
        });
    }

    protected void assertNotEmpty(Object[] objects) {
        if (objects == null || objects.length == 0)
            throw new IllegalArgumentException(
                    "Arguments is null or its length is empty");
    }
}
