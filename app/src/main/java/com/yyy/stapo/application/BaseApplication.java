package com.yyy.stapo.application;

import android.app.Application;

import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    private OkHttpClient okHttpClient;
    private static BaseApplication baseApplication;

    @Override
    public final void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(getApplicationContext());
        baseApplication = this;
    }

    /**
     * 返回应用程序实例
     */
    public static BaseApplication getInstance() {
        return baseApplication;
    }


    public OkHttpClient getClient() {
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient();
        return okHttpClient;
    }

    ;
}
