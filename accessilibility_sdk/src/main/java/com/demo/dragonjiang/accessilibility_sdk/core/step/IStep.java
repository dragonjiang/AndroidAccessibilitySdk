package com.demo.dragonjiang.accessilibility_sdk.core.step;

/**
 * @author DragonJiang
 * @Date 2016/7/31
 * @Time 10:36
 * @description
 */
public interface IStep {

    /**
     * 处理这个步骤关注的事件
     */
    void dealStep();

    /**
     * 重置状态
     */
    void reset();
}
