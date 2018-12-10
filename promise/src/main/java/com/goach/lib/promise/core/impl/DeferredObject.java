package com.goach.lib.promise.core.impl;

import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:47.
 * Des: 代理模式
 */
public class DeferredObject<D,F,P> extends AbstractPromise<D,F,P> implements Deferred<D, F, P> {

    @Override
    public Deferred<D, F, P> resolve(final D resolve) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot resolve again");

            this.state = State.RESOLVED;
            this.resolveResult = resolve;

            try {
                triggerDone(resolve);
            } finally {
                triggerAlways(state, resolve, null);
            }
        }
        return this;
    }

    @Override
    public Deferred<D, F, P> notify(final P progress) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot notify progress");

            triggerProgress(progress);
        }
        return this;
    }

    @Override
    public Deferred<D, F, P> reject(final F reject) {
        synchronized (this) {
            if (!isPending())
                throw new IllegalStateException("Deferred object already finished, cannot reject again");
            this.state = State.REJECTED;
            this.rejectResult = reject;

            try {
                triggerFail(reject);
            } finally {
                triggerAlways(state, null, reject);
            }
        }
        return this;
    }

    public Promise<D, F, P> promise() {
        return this;
    }
}
