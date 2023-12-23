package com.wangxingxing.mydemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * author : 王星星
 * date : 2023/12/23 10:38
 * email : 1099420259@qq.com
 * description :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
