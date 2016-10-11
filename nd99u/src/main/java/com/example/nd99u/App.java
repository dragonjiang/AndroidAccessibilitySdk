package com.example.nd99u;

import android.app.Application;

import com.demo.dragonjiang.accessilibility_sdk.utils.ContextUtils;
import com.orhanobut.logger.Logger;


/**
 * @author DragonJiang
 * @Date 2016/7/24
 * @Time 17:50
 * @description
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.setAppContext(getApplicationContext());
        Logger.init().hideThreadInfo().methodCount(0);
    }
}
