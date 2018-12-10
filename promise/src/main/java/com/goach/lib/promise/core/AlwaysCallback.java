package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:31.
 * Des:
 * @param <D> 执行成功的数据类型
 * @param <R> 执行失败的数据类型
 */
public interface AlwaysCallback<D,R> {
    public void onAlways(final Promise.State state,final D resolved,final R rejected);
}
