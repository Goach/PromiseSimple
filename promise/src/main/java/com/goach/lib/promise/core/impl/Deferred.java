package com.goach.lib.promise.core.impl;

import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:44.
 * Des:
 */
public interface Deferred<D, F, P> extends Promise<D, F, P> {
    Deferred<D, F, P> resolve(final D resolve);
    Deferred<D, F, P> reject(final F reject);
    Deferred<D, F, P> notify(final P progress);
    Promise<D, F, P> promise();
}
