package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:00.
 * Des: 失败请求接口
 */
public interface FailCallback<F> {
    public void onFail(final F result);
}
