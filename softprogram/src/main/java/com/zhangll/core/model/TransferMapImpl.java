package com.zhangll.core.model;

import com.zhangll.core.view.MapInterface;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.Iterator;

public class TransferMapImpl extends TransferMap {
    /**
     * 逻辑规则
     * 1. 如果只是一个细胞 周围有超过四个细胞是活的,或者只有1歌 或细胞那么就会死亡
     * 2. 如果 周围有3个细胞活着,那么就继续是活细胞
     * 3. 如果 周围2个细胞,那么就是当前细胞状态不变
     * @param map
     */
    @Override
    public void updateState(MapInterface map) {
        // 转换逻辑 旁边有几个
        while(map.hasNext()){
            MapInterfaceImpl.Ceil next = map.next();
            int a = map.getNeighbourCount(next.getRow(), next.getColumn());
            System.out.println("a:" + a);
            switch (a){
                case 0:
                    continue;
                case 2:
                    continue;
                case 3:
                    System.out.println("set live");
                    next.setState(MapInterfaceImpl.CeilState.LIVE);
                default:
                    System.out.println("set dea");
                    next.setState(MapInterfaceImpl.CeilState.DEAD);
            }
        }
        map.reset();
    }
}
