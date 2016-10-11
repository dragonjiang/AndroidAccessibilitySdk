package com.demo.dragonjiang.accessilibility_sdk.core.command.execption;

/**
 * @author DragonJiang
 * @Date 2016/8/4
 * @Time 16:08
 * @description
 */
public class CmdCancelException extends Exception {

    public CmdCancelException() {
        super("command canceled!");
    }
}
