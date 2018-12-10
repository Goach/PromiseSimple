package com.goach.lib.promise.android;

import android.os.AsyncTask;
import android.util.Log;

import com.goach.lib.promise.core.Promise;
import com.goach.lib.promise.core.impl.DeferredObject;
import com.goach.lib.promise.core.DeferredManager.StartPolicy;
import java.util.concurrent.CancellationException;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 14:06.
 * Des:
 */
public abstract class DeferredAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    private static final String TAG = DeferredAsyncTask.class.getSimpleName();
    private final DeferredObject<Result, Throwable, Progress> deferred = new DeferredObject<Result, Throwable, Progress>();
    private final StartPolicy startPolicy;

    private Throwable throwable;

    public DeferredAsyncTask() {
        this.startPolicy = StartPolicy.DEFAULT;
    }

    public DeferredAsyncTask(StartPolicy startPolicy) {
        this.startPolicy = startPolicy;
    }

    @Override
    protected final void onCancelled() {
        deferred.reject(new CancellationException());
    }

    protected final void onCancelled(Result result) {
        deferred.reject(new CancellationException());
    };

    @Override
    protected final void onPostExecute(Result result) {
        if (throwable != null) {
            deferred.reject(throwable);
        } else {
            deferred.resolve(result);
        }
    }

    @Override
    protected final void onProgressUpdate(Progress ... values) {
        if (values == null || values.length == 0) {
            deferred.notify(null);
        } else if (values.length > 0) {
            Log.w(TAG,"There were multiple progress values.  Only the first one was used!");
            deferred.notify(values[0]);
        }
    }

    protected final Result doInBackground(Params ... params) {
        try {
            return doInBackgroundSafe(params);
        } catch (Throwable e) {
            throwable = e;
            return null;
        }
    };

    protected abstract Result doInBackgroundSafe(Params ... params) throws Exception;

    @SuppressWarnings("unchecked")
    protected final void notify(Progress progress) {
        publishProgress(progress);
    }

    public Promise<Result, Throwable, Progress> promise() {
        return deferred.promise();
    }

    public StartPolicy getStartPolicy() {
        return startPolicy;
    }
}

