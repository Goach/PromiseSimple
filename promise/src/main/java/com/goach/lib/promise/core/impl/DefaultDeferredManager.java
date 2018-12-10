package com.goach.lib.promise.core.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:50.
 * Des:
 */
public class DefaultDeferredManager extends AbstractDeferredManager {

    public static final boolean DEFAULT_AUTO_SUBMIT = true;

    private final ExecutorService executorService;
    private boolean autoSubmit = DEFAULT_AUTO_SUBMIT;


    public DefaultDeferredManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    public DefaultDeferredManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    protected void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    @Override
    protected void submit(Callable callable) {
        executorService.submit(callable);
    }

    @Override
    public boolean isAutoSubmit() {
        return autoSubmit;
    }

    public void setAutoSubmit(boolean autoSubmit) {
        this.autoSubmit = autoSubmit;
    }

}

