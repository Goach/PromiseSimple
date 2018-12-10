package com.goach.lib.promise.core.multiple;

import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:40.
 * Des:
 */
public class OneReject {
    private final int index;
    private final Promise promise;
    private final Object reject;

    public OneReject(int index, Promise promise, Object reject) {
        super();
        this.index = index;
        this.promise = promise;
        this.reject = reject;
    }

    public int getIndex() {
        return index;
    }

    public Promise getPromise() {
        return promise;
    }

    public Object getReject() {
        return reject;
    }

    @Override
    public String toString() {
        return "OneReject [index=" + index + ", promise=" + promise
                + ", reject=" + reject + "]";
    }
}
