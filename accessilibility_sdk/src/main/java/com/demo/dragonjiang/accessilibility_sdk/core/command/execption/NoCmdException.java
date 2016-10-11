package com.demo.dragonjiang.accessilibility_sdk.core.command.execption;

/**
 * @author DragonJiang
 * @Date 2016/8/4
 * @Time 16:08
 * @description
 */
public class NoCmdException extends Exception {

    public NoCmdException() {
        super("command is empty!");
    }
}
