package com.goach.lib.promise.android;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.goach.lib.promise.core.AlwaysCallback;
import com.goach.lib.promise.core.DoneCallback;
import com.goach.lib.promise.core.FailCallback;
import com.goach.lib.promise.core.ProgressCallback;
import com.goach.lib.promise.core.Promise;
import com.goach.lib.promise.core.impl.Deferred;
import com.goach.lib.promise.core.impl.DeferredObject;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 14:09.
 * Des:
 */
public class AndroidDeferredObject<D, F, P> extends DeferredObject<D, F, P> {
    private static final InternalHandler sHandler = new InternalHandler();

    private static final int MESSAGE_POST_DONE = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    private static final int MESSAGE_POST_FAIL = 0x3;
    private static final int MESSAGE_POST_ALWAYS = 0x4;

    private final AndroidExecutionScope defaultAndroidExecutionScope;

    public AndroidDeferredObject(Promise<D, F, P> promise) {
        this(promise, AndroidExecutionScope.UI);
    }

    public AndroidDeferredObject(Promise<D, F, P> promise,
                                 AndroidExecutionScope defaultAndroidExecutionScope) {
        this.defaultAndroidExecutionScope = defaultAndroidExecutionScope;
        promise.done(new DoneCallback<D>() {
            @Override
            public void onDone(D result) {
                AndroidDeferredObject.this.resolve(result);
            }
        }).progress(new ProgressCallback<P>() {
            @Override
            public void onProgress(P progress) {
                AndroidDeferredObject.this.notify(progress);
            }
        }).fail(new FailCallback<F>() {
            @Override
            public void onFail(F result) {
                AndroidDeferredObject.this.reject(result);
            }
        });
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void handleMessage(Message msg) {
            CallbackMessage<?, ?, ?, ?> result = (CallbackMessage<?, ?, ?, ?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_DONE:
                    ((DoneCallback) result.callback).onDone(result.resolved);
                    break;
                case MESSAGE_POST_PROGRESS:
                    ((ProgressCallback) result.callback)
                            .onProgress(result.progress);
                    break;
                case MESSAGE_POST_FAIL:
                    ((FailCallback) result.callback).onFail(result.rejected);
                    break;
                case MESSAGE_POST_ALWAYS:
                    ((AlwaysCallback) result.callback).onAlways(result.state,
                            result.resolved, result.rejected);
                    break;
            }
        }
    }

    protected void triggerDone(DoneCallback<D> callback, D resolved) {
        if (determineAndroidExecutionScope(callback) == AndroidExecutionScope.UI) {
            executeInUiThread(MESSAGE_POST_DONE, callback, State.RESOLVED,
                    resolved, null, null);
        } else {
            super.triggerDone(callback, resolved);
        }
    }

    protected void triggerFail(FailCallback<F> callback, F rejected) {
        if (determineAndroidExecutionScope(callback) == AndroidExecutionScope.UI) {
            executeInUiThread(MESSAGE_POST_FAIL, callback, State.REJECTED,
                    null, rejected, null);
        } else {
            super.triggerFail(callback, rejected);
        }
    }

    protected void triggerProgress(ProgressCallback<P> callback, P progress) {
        if (determineAndroidExecutionScope(callback) == AndroidExecutionScope.UI) {
            executeInUiThread(MESSAGE_POST_PROGRESS, callback, State.PENDING,
                    null, null, progress);
        } else {
            super.triggerProgress(callback, progress);
        }
    }

    protected void triggerAlways(AlwaysCallback<D, F> callback, State state,
                                 D resolve, F reject) {
        if (determineAndroidExecutionScope(callback) == AndroidExecutionScope.UI) {
            executeInUiThread(MESSAGE_POST_ALWAYS, callback, state, resolve,
                    reject, null);
        } else {
            super.triggerAlways(callback, state, resolve, reject);
        }
    }

    protected <Callback> void executeInUiThread(int what, Callback callback,
                                                State state, D resolve, F reject, P progress) {
        Message message = sHandler.obtainMessage(what,
                new CallbackMessage<Callback, D, F, P>(this, callback, state,
                        resolve, reject, progress));
        message.sendToTarget();
    }

    protected AndroidExecutionScope determineAndroidExecutionScope(Object callback) {
        AndroidExecutionScope scope = null;
        if (callback instanceof AndroidExecutionScopeable) {
            scope = ((AndroidExecutionScopeable) callback).getExecutionScope();
        }
        return scope == null ? defaultAndroidExecutionScope : scope;
    }

    @SuppressWarnings("rawtypes")
    private static class CallbackMessage<Callback, D, F, P> {
        final Deferred deferred;
        final Callback callback;
        final D resolved;
        final F rejected;
        final P progress;
        final State state;

        CallbackMessage(Deferred deferred, Callback callback, State state,
                        D resolved, F rejected, P progress) {
            this.deferred = deferred;
            this.callback = callback;
            this.state = state;
            this.resolved = resolved;
            this.rejected = rejected;
            this.progress = progress;
        }
    }

}
