package com.demo.dragonjiang.accessilibility_sdk.shell_support;


/**
 * result of command
 * <p>
 * <p>
 * {@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in linux shell
 * <p>
 * {@link CommandResult#successMsg} means success message of command result
 * <p>
 * {@link CommandResult#errorMsg} means error message of command result
 */
public class CommandResult {

    /**
     * result of command
     **/
    public int result;
    /**
     * success message of command result
     **/
    public String successMsg;
    /**
     * error message of command result
     **/
    public String errorMsg;


    public CommandResult(int result) {
        this.result = result;
    }


    public CommandResult(int result, String successMsg, String errorMsg) {
        this.result = result;
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
    }
}