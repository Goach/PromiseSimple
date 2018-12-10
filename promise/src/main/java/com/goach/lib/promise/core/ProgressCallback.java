package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 14:10.
 * Des: 正在执行中的回调
 */
public interface ProgressCallback<P> {
    public void onProgress(final P progress);
}
