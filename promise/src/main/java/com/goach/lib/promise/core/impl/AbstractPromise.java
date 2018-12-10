package com.goach.lib.promise.core.impl;

import android.util.Log;

import com.goach.lib.promise.core.AlwaysCallback;
import com.goach.lib.promise.core.DoneCallback;
import com.goach.lib.promise.core.DoneFilter;
import com.goach.lib.promise.core.DonePipe;
import com.goach.lib.promise.core.FailCallback;
import com.goach.lib.promise.core.FailFilter;
import com.goach.lib.promise.core.FailPipe;
import com.goach.lib.promise.core.ProgressCallback;
import com.goach.lib.promise.core.ProgressFilter;
import com.goach.lib.promise.core.ProgressPipe;
import com.goach.lib.promise.core.Promise;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:48.
 * Des:
 */
public abstract class AbstractPromise<D,F,P> implements Promise<D,F,P> {
    private static final String TAG = AbstractPromise.class.getSimpleName();
    protected volatile State state = State.PENDING;
    protected final List<DoneCallback<D>> doneCallbacks = new CopyOnWriteArrayList<>();
    private final List<FailCallback<F>> failCallbacks = new CopyOnWriteArrayList<>();
    private final List<ProgressCallback<P>> progressCallbacks = new CopyOnWriteArrayList<>();
    private final List<AlwaysCallback<D,F>> alwaysCallbacks = new CopyOnWriteArrayList<>();

    protected D resolveResult;
    protected F rejectResult;

    @Override
    public State state() {
        return state;
    }

    @Override
    public Promise<D, F, P> done(DoneCallback<D> callback) {
        synchronized (this){
            if(isResolved()){
                triggerDone(callback,resolveResult);
            }else{
                doneCallbacks.add(callback);
            }
        }
        return this;
    }
    @Override
    public Promise<D, F, P> fail(FailCallback<F> callback) {
        synchronized (this) {
            if(isRejected()){
                triggerFail(callback, rejectResult);
            }else{
                failCallbacks.add(callback);
            }
        }
        return this;
    }
    @Override
    public Promise<D, F, P> always(AlwaysCallback<D, F> callback) {
        synchronized (this) {
            if(isPending()){
                alwaysCallbacks.add(callback);
            }else{
                triggerAlways(callback, state, resolveResult, rejectResult);
            }
        }
        return this;
    }
    protected void triggerDone(D resolved) {
        for (DoneCallback<D> callback : doneCallbacks) {
            try {
                triggerDone(callback, resolved);
            } catch (Exception e) {
                Log.e(TAG,"an uncaught exception occured in a DoneCallback", e);
            }
        }
        doneCallbacks.clear();
    }
    protected void triggerDone(DoneCallback<D> callback, D resolved) {
        callback.onDone(resolved);
    }
    protected void triggerFail(F rejected) {
        for (FailCallback<F> callback : failCallbacks) {
            try {
                triggerFail(callback, rejected);
            } catch (Exception e) {
                Log.e(TAG,"an uncaught exception occured in a FailCallback", e);
            }
        }
        failCallbacks.clear();
    }
    protected void triggerFail(FailCallback<F> callback, F rejected) {
        callback.onFail(rejected);
    }
    protected void triggerProgress(P progress) {
        for (ProgressCallback<P> callback : progressCallbacks) {
            try {
                triggerProgress(callback, progress);
            } catch (Exception e) {
                Log.e(TAG,"an uncaught exception occured in a ProgressCallback", e);
            }
        }
    }

    protected void triggerProgress(ProgressCallback<P> callback, P progress) {
        callback.onProgress(progress);
    }
    protected void triggerAlways(State state, D resolve, F reject) {
        for (AlwaysCallback<D, F> callback : alwaysCallbacks) {
            try {
                triggerAlways(callback, state, resolve, reject);
            } catch (Exception e) {
                Log.e(TAG,"an uncaught exception occured in a AlwaysCallback", e);
            }
        }
        alwaysCallbacks.clear();

        synchronized (this) {
            this.notifyAll();
        }
    }
    protected void triggerAlways(AlwaysCallback<D, F> callback, State state, D resolve, F reject) {
        callback.onAlways(state, resolve, reject);
    }
    @Override
    public Promise<D, F, P> progress(ProgressCallback<P> callback) {
        progressCallbacks.add(callback);
        return this;
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> callback) {
        return done(callback);
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback) {
        done(doneCallback);
        fail(failCallback);
        return this;
    }

    @Override
    public Promise<D, F, P> then(DoneCallback<D> doneCallback, FailCallback<F> failCallback,
                                 ProgressCallback<P> progressCallback) {
        done(doneCallback);
        fail(failCallback);
        progress(progressCallback);
        return this;
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DoneFilter<D, D_OUT> doneFilter) {
        return new FilteredPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, null, null);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter) {
        return new FilteredPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, failFilter, null);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DoneFilter<D, D_OUT> doneFilter, FailFilter<F, F_OUT> failFilter,
            ProgressFilter<P, P_OUT> progressFilter) {
        return new FilteredPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, failFilter, progressFilter);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter) {
        return new PipedPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, null, null);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter,
            FailPipe<F, D_OUT, F_OUT, P_OUT> failFilter) {
        return new PipedPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, failFilter, null);
    }

    @Override
    public <D_OUT, F_OUT, P_OUT> Promise<D_OUT, F_OUT, P_OUT> then(
            DonePipe<D, D_OUT, F_OUT, P_OUT> doneFilter,
            FailPipe<F, D_OUT, F_OUT, P_OUT> failFilter,
            ProgressPipe<P, D_OUT, F_OUT, P_OUT> progressFilter) {
        return new PipedPromise<D, F, P, D_OUT, F_OUT, P_OUT>(this, doneFilter, failFilter, progressFilter);
    }

    @Override
    public boolean isPending() {
        return state == State.PENDING;
    }

    @Override
    public boolean isResolved() {
        return state == State.RESOLVED;
    }

    @Override
    public boolean isRejected() {
        return state == State.REJECTED;
    }

    public void waitSafely() throws InterruptedException {
        waitSafely(-1);
    }
    public void waitSafely(long timeout) throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        synchronized (this) {
            while (this.isPending()) {
                try {
                    if (timeout <= 0) {
                        wait();
                    } else {
                        final long elapsed = (System.currentTimeMillis() - startTime);
                        final long waitTime = timeout - elapsed;
                        wait(waitTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw e;
                }

                if (timeout > 0 && ((System.currentTimeMillis() - startTime) >= timeout)) {
                    return;
                } else {
                    continue; // keep looping
                }
            }
        }
    }
}
