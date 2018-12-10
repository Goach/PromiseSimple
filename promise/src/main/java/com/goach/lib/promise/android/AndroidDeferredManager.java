package com.goach.lib.promise.android;

import android.annotation.SuppressLint;
import android.os.Build;

import com.goach.lib.promise.core.DeferredFutureTask;
import com.goach.lib.promise.core.Promise;
import com.goach.lib.promise.core.impl.DefaultDeferredManager;
import com.goach.lib.promise.core.multiple.MasterProgress;
import com.goach.lib.promise.core.multiple.MultipleResults;
import com.goach.lib.promise.core.multiple.OneReject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:51.
 * Des:
 */
public class AndroidDeferredManager extends DefaultDeferredManager {
    private static Void[] EMPTY_PARAMS = new Void[]{};

    public AndroidDeferredManager() {
        super();
    }

    public AndroidDeferredManager(ExecutorService executorService) {
        super(executorService);
    }

    @SuppressLint("NewApi")
    public <Progress, Result> Promise<Result, Throwable, Progress> when(
            DeferredAsyncTask<Void, Progress, Result> task) {

        if (task.getStartPolicy() == StartPolicy.AUTO
                || (task.getStartPolicy() == StartPolicy.DEFAULT && isAutoSubmit())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(getExecutorService(), EMPTY_PARAMS);
            } else {
                task.execute(EMPTY_PARAMS);
            }
        }

        return task.promise();
    }

    @SuppressWarnings("rawtypes")
    public Promise<MultipleResults, OneReject, MasterProgress> when(
            DeferredAsyncTask<Void, ?, ?> ... tasks) {
        assertNotEmpty(tasks);

        Promise[] promises = new Promise[tasks.length];

        for (int i = 0; i < tasks.length; i++) {
            promises[i] = when(tasks[i]);
        }

        return when(promises);
    }

    @SuppressWarnings("rawtypes")
    public Promise<MultipleResults, OneReject, MasterProgress> when(AndroidExecutionScope scope,
                                                                    DeferredAsyncTask<Void, ?, ?> ... tasks) {
        assertNotEmpty(tasks);
        Promise[] promises = new Promise[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            promises[i] = when(tasks[i]);
        }
        return when(scope, promises);
    }

    @Override
    public <D, P> Promise<D, Throwable, P> when(DeferredFutureTask<D, P> task) {
        return new AndroidDeferredObject<D, Throwable, P>(super.when(task)).promise();
    }

    @Override
    public <D, F, P> Promise<D, F, P> when(Promise<D, F, P> promise) {
        if (promise instanceof AndroidDeferredObject) {
            return promise;
        }
        return new AndroidDeferredObject<D, F, P>(promise).promise();
    }

    public <D, F, P> Promise<D, F, P> when(Promise<D, F, P> promise, AndroidExecutionScope scope) {
        if (promise instanceof AndroidDeferredObject) {
            return promise;
        }
        return new AndroidDeferredObject<D, F, P>(promise, scope).promise();
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Promise<MultipleResults, OneReject, MasterProgress> when(Promise... promises) {
        return new AndroidDeferredObject<MultipleResults, OneReject, MasterProgress>
                (super.when(promises)).promise();
    }

    @SuppressWarnings({ "rawtypes" })
    public Promise<MultipleResults, OneReject, MasterProgress> when(AndroidExecutionScope scope, Promise... promises) {
        return new AndroidDeferredObject<MultipleResults, OneReject, MasterProgress>
                (super.when(promises), scope).promise();
    }
}
