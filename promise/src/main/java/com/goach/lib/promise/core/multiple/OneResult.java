package com.goach.lib.promise.core.multiple;

import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:39.
 * Des:
 */
public class OneResult {
    private final int index;
    private final Promise promise;
    private final Object result;

    public OneResult(int index, Promise promise, Object result) {
        super();
        this.index = index;
        this.promise = promise;
        this.result = result;
    }
    public int getIndex() {
        return index;
    }
    public Promise getPromise() {
        return promise;
    }
    public Object getResult() {
        return result;
    }
    @Override
    public String toString() {
        return "OneResult [index=" + index + ", promise=" + promise
                + ", result=" + result + "]";
    }
}
