package com.example.nd99u;

/**
 * @author DragonJiang
 * @Date 2016/8/11
 * @Time 18:45
 * @description
 */
public class MsgEvent {
    private String mMsg;

    public MsgEvent(String mMsg) {
        this.mMsg = mMsg;
    }

    public MsgEvent() {
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        this.mMsg = mMsg;
    }
}
