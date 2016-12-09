package com.dl7.taglayout;

import android.app.Application;

import com.dl7.taglayout.utils.ToastUtils;

/**
 * Created by long on 2016/12/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
