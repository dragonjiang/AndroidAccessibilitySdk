package com.demo.dragonjiang.accessilibility_sdk.shell_support;

/**
 * @author DragonJiang
 * @Date 2016/7/24
 * @Time 17:50
 * @description
 */

public interface IShellCmdCallback {

    /**
     * 输入错误提示
     *
     * @param errorMsg 提示信息
     */
    void OnInputError(String errorMsg);

    /**
     * 处理结束返回
     *
     * @param result 将处理结果转成字符串返回
     */
    void OnComplete(String result);
}
