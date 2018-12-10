package com.goach.lib.promise.core.impl;

import com.goach.lib.promise.core.DoneCallback;
import com.goach.lib.promise.core.DoneFilter;
import com.goach.lib.promise.core.FailCallback;
import com.goach.lib.promise.core.FailFilter;
import com.goach.lib.promise.core.ProgressCallback;
import com.goach.lib.promise.core.ProgressFilter;
import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 15:32.
 * Des:
 */
public class FilteredPromise<D, F, P, D_OUT, F_OUT, P_OUT> extends DeferredObject<D_OUT, F_OUT, P_OUT> implements Promise<D_OUT, F_OUT, P_OUT> {
    protected static final NoOpDoneFilter NO_OP_DONE_FILTER = new NoOpDoneFilter();
    protected static final NoOpFailFilter NO_OP_FAIL_FILTER = new NoOpFailFilter();
    protected static final NoOpProgressFilter NO_OP_PROGRESS_FILTER = new NoOpProgressFilter();

    private final DoneFilter<D, D_OUT> doneFilter;
    private final FailFilter<F, F_OUT> failFilter;
    private final ProgressFilter<P, P_OUT> progressFilter;

    public FilteredPromise(final Promise<D, F, P> promise, final DoneFilter<D, D_OUT> doneFilter, final FailFilter<F, F_OUT> failFilter, final ProgressFilter<P, P_OUT> progressFilter) {
        this.doneFilter = doneFilter == null ? NO_OP_DONE_FILTER : doneFilter;
        this.failFilter = failFilter == null ? NO_OP_FAIL_FILTER : failFilter;
        this.progressFilter = progressFilter == null ? NO_OP_PROGRESS_FILTER : progressFilter;

        promise.done(new DoneCallback<D>() {
            @Override
            public void onDone(D result) {
                FilteredPromise.this.resolve(FilteredPromise.this.doneFilter.filterDone(result));
            }
        }).fail(new FailCallback<F>() {

            @Override
            public void onFail(F result) {
                FilteredPromise.this.reject(FilteredPromise.this.failFilter.filterFail(result));
            }
        }).progress(new ProgressCallback<P>() {

            @Override
            public void onProgress(P progress) {
                FilteredPromise.this.notify(FilteredPromise.this.progressFilter.filterProgress(progress));
            }
        });
    }

    public static final class NoOpDoneFilter<D> implements DoneFilter<D, D> {
        @Override
        public D filterDone(D result) {
            return result;
        }
    }

    public static final class NoOpFailFilter<F> implements FailFilter<F, F> {
        @Override
        public F filterFail(F result) {
            return result;
        }
    }

    public static final class NoOpProgressFilter<P> implements ProgressFilter<P, P> {
        @Override
        public P filterProgress(P progress) {
            return progress;
        }
    }
}
