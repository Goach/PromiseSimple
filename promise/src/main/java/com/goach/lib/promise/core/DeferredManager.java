package com.goach.lib.promise.core;

import com.goach.lib.promise.core.multiple.MasterProgress;
import com.goach.lib.promise.core.multiple.MultipleResults;
import com.goach.lib.promise.core.multiple.OneReject;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * author: Goach.zhong
 * Date: 2018/12/7 13:50.
 * Des:
 */
public interface DeferredManager {
    enum StartPolicy {
        DEFAULT,//自由选择是否启动
        AUTO,//自动启动
        MANAUL//手动启动
    }

   <D,F,P> Promise<D,F,P> when(Promise<D,F,P> promise);

    Promise<Void,Throwable,Void> when(Runnable runnable);
    <D> Promise<D,Throwable,Void> when(Callable<D> callable);
    <D> Promise<D,Throwable,Void> when(Future<D> callable);
    <P> Promise<Void,Throwable,P> when(DeferredRunnable<P> callable);
    <D, P> Promise<D, Throwable, P> when(
            DeferredCallable<D, P> callable);
    <D, P> Promise<D, Throwable, P> when(
            DeferredFutureTask<D, P> task);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            Promise... promises);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            Runnable... runnables);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            Callable<?>... callables);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            DeferredRunnable<?>... runnables);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            DeferredCallable<?, ?>... callables);
    Promise<MultipleResults, OneReject, MasterProgress> when(
            DeferredFutureTask<?, ?>... tasks);

    Promise<MultipleResults, OneReject, MasterProgress> when(
            Future<?> ... futures);



}
