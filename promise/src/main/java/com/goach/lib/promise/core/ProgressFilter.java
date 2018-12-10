package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:14.
 * Des: 对执行中的数据类型进行过滤
 */
public interface ProgressFilter<P,P_OUT> {
    public P_OUT filterProgress(final P result);
}
