package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:14.
 * Des: 对完成的数据类型进行过滤
 */
public interface DoneFilter<D,D_OUT> {
    public D_OUT filterDone(final D result);
}
