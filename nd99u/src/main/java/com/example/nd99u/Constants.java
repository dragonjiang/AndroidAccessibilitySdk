package com.example.nd99u;

public class Constants {
    public interface ACTION {
        String LAUNCH_MAIN_ACTIVITY = "com.demo.dragonjiang.dingsigninplugin.action.launch_main_activity";
        String START_FOREGROUND = "com.demo.dragonjiang.dingsigninplugin.action.start_foreground";
        String STOP_FOREGROUND = "com.demo.dragonjiang.dingsigninplugin.action.stop_foreground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 102;
    }

    public interface SP_KEY {
        String LAUNCH_TIME = "LAUNCH_TIME";
        String RESULT = "RESULT";
        String LAST_RESULT = "LAST_RESULT";
        String NAME = "NAME";
    }

    public static final String APP_PKG = "com.nd.app.factory.imapp0";
    public static final long MAX_PROCESSING_TIME = 10 * 60 * 1000;

    public interface RUN_RESULT{
        int NONE = 0;
        int FAILURE = 1;
        int SUCCESS = 2;
    }

}
