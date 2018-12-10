package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:21.
 * Des: 输入的类型为P，输出的类型为P_OUT
 */
public interface ProgressPipe<P,D_OUT,F_OUT,P_OUT> {
    public Promise<D_OUT,F_OUT,P_OUT> pipeProgress(final P result);
}
