package com.yf.chainfiredemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import org.androidannotations.annotations.EApplication;

/**
 * @Author: LinFord
 * @Project: LinFord_Application
 * @Create at: 16:08-星期五-五月-2019
 * @Email: glf@yf-space.com
 * @Copyright(c): 深圳市圆方时代电子有限公司
 * @Technique point:
 * @Description:
 * @Modify:
 */
@EApplication
public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Utils.init(this);
        FileMonitorService_.intent(this).start();
    }

    @Override public void onTerminate() {
        super.onTerminate();
        FileMonitorService_.intent(this).stop();
    }
}
