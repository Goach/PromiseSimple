package com.goach.lib.promise.core;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 13:58.
 * Des: 完成任务的回调
 */
public interface DoneCallback<D> {
    public void onDone(final D result);
}
