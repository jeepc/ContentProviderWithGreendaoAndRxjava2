package com.jeepc.service;

import android.app.Application;

/**
 * Created by jeepc on 2018/1/3.
 */

public class App extends Application {
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaoManager.getInstance().init(this);
    }
    public static App get() {
        return instance;
    }
}
