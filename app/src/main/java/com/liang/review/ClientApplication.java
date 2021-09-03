package com.liang.review;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Author:bernie-mac
 * Data:2021/7/29 18:04
 * Description: PACKAGE_NAME
 */
public class ClientApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        ClientApplication application = (ClientApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
