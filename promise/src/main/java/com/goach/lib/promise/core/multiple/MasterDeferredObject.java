package com.goach.lib.promise.core.multiple;

import com.goach.lib.promise.core.DoneCallback;
import com.goach.lib.promise.core.FailCallback;
import com.goach.lib.promise.core.ProgressCallback;
import com.goach.lib.promise.core.Promise;
import com.goach.lib.promise.core.impl.DeferredObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:48.
 * Des:
 */
public class MasterDeferredObject extends
        DeferredObject<MultipleResults, OneReject, MasterProgress>
        implements Promise<MultipleResults, OneReject, MasterProgress> {
    private final int numberOfPromises;
    private final AtomicInteger doneCount = new AtomicInteger();
    private final AtomicInteger failCount = new AtomicInteger();
    private final MultipleResults results;

    @SuppressWarnings("unchecked")
    public MasterDeferredObject(Promise... promises) {
        if (promises == null || promises.length == 0)
            throw new IllegalArgumentException("Promises is null or empty");
        this.numberOfPromises = promises.length;
        results = new MultipleResults(numberOfPromises);

        int count = 0;
        for (final Promise promise : promises) {
            final int index = count++;
            promise.fail(new FailCallback<Object>() {
                public void onFail(Object result) {
                    synchronized (MasterDeferredObject.this) {
                        if (!MasterDeferredObject.this.isPending())
                            return;

                        final int fail = failCount.incrementAndGet();
                        MasterDeferredObject.this.notify(new MasterProgress(
                                doneCount.get(),
                                fail,
                                numberOfPromises));

                        MasterDeferredObject.this.reject(new OneReject(index, promise, result));
                    }
                }
            }).progress(new ProgressCallback() {
                public void onProgress(Object progress) {
                    synchronized (MasterDeferredObject.this) {
                        if (!MasterDeferredObject.this.isPending())
                            return;

                        MasterDeferredObject.this.notify(new OneProgress(
                                doneCount.get(),
                                failCount.get(),
                                numberOfPromises, index, promise, progress));
                    }
                }
            }).done(new DoneCallback() {
                public void onDone(Object result) {
                    synchronized (MasterDeferredObject.this) {
                        if (!MasterDeferredObject.this.isPending())
                            return;

                        results.set(index, new OneResult(index, promise,
                                result));
                        int done = doneCount.incrementAndGet();

                        MasterDeferredObject.this.notify(new MasterProgress(
                                done,
                                failCount.get(),
                                numberOfPromises));

                        if (done == numberOfPromises) {
                            MasterDeferredObject.this.resolve(results);
                        }
                    }
                }
            });
        }
    }
}

