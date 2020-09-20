package com.zhangll.core.timer;

/**
 * 调节时间速度,可以直接执行
 * 主要是这个模块更新地图
 */
public interface Timer extends Runnable{

    /**
     * 触发瓶率
     * @return
     */
    int fireFreqquence();
}
