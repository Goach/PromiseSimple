package com.goach.lib.promise.core.multiple;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:40.
 * Des:
 */
public class MasterProgress {
    private final int done;
    private final int fail;
    private final int total;

    public MasterProgress(int done, int fail, int total) {
        super();
        this.done = done;
        this.fail = fail;
        this.total = total;
    }

    public int getDone() {
        return done;
    }

    public int getFail() {
        return fail;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "MasterProgress [done=" + done + ", fail=" + fail
                + ", total=" + total + "]";
    }
}
