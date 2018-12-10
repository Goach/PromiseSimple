package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:14.
 * Des: 对失败的数据类型进行过滤
 */
public interface FailFilter<F,F_OUT> {
    public F_OUT filterFail(final F result);
}
