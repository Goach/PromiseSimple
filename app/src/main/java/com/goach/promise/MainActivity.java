package com.goach.promise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.goach.lib.promise.core.DoneCallback;
import com.goach.lib.promise.core.DoneFilter;
import com.goach.lib.promise.core.FailCallback;
import com.goach.lib.promise.core.FailFilter;
import com.goach.lib.promise.core.ProgressCallback;
import com.goach.lib.promise.core.ProgressFilter;
import com.goach.lib.promise.ui.VUiKit;

import java.util.concurrent.Callable;
/**
 * author: Goach.zhong
 * Date: 2018/12/10 11:06.
 * Des:代码来源于https://github.com/jdeferred/jdeferred，本例子仅供学习
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testPromise(View view) {
        VUiKit.defer().when(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).then(new DoneFilter<Void, String>() {

            @Override
            public String filterDone(Void result) {
                return "result";
            }
        }, new FailFilter<Throwable, String>() {

            @Override
            public String filterFail(Throwable result) {
                return "fail";
            }
        }, new ProgressFilter<Void, String>() {

            @Override
            public String filterProgress(Void result) {
                return "progress";
            }
        }).done(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                Log.d("tag","finish============onDone"+result);
            }
        }).fail(new FailCallback<String>() {
            @Override
            public void onFail(String result) {
                Log.d("tag","finish============onFail"+result);
            }
        }).progress(new ProgressCallback<String>() {
            @Override
            public void onProgress(String progress) {
                Log.d("tag","finish============onProgress"+progress);
            }
        });
    }
}
