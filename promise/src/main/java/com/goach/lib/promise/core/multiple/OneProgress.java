package com.goach.lib.promise.core.multiple;

import com.goach.lib.promise.core.Promise;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:49.
 * Des:
 */
public class OneProgress extends MasterProgress {
    private final int index;

    private final Promise promise;
    private final Object progress;

    public OneProgress(int done, int fail, int total, int index, Promise promise, Object progress) {
        super(done, fail, total);
        this.index = index;
        this.promise = promise;
        this.progress = progress;
    }

    public int getIndex() {
        return index;
    }

    public Promise getPromise() {
        return promise;
    }

    public Object getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return "OneProgress [index=" + index + ", promise=" + promise
                + ", progress=" + progress + ", getDone()=" + getDone()
                + ", getFail()=" + getFail() + ", getTotal()=" + getTotal()
                + "]";
    }
}

