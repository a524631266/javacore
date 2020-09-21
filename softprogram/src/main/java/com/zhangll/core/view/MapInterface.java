package com.zhangll.core.view;

import com.zhangll.core.model.Ceil;

import java.util.Iterator;

/**
 * 地图的功能有哪些
 * 管理地图初始化,获取,更新
 * 只暴露用户接口
 */
public interface MapInterface extends Iterator<Ceil> {

    /**
     * 转换游戏只暴露接口
     */
    void update(int row, int column, MapInterfaceImpl.CeilState state);

    int getNeighbourCount(int row, int column);

    /**
     * 重置棋盘的当前row和当前column
     */
    void reset();

    /**
     * 根据各种情况来打印数据
     */
    void print();
}
