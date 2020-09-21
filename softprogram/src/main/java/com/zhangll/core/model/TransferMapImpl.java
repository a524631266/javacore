package com.zhangll.core.model;

import com.zhangll.core.view.MapInterface;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.*;

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
        Map<MapInterfaceImpl.CeilState, List<Ceil>> result =
                new HashMap<MapInterfaceImpl.CeilState, List<Ceil>>(){
                    {
                        put(MapInterfaceImpl.CeilState.LIVE, new ArrayList<>());
                        put(MapInterfaceImpl.CeilState.DEAD, new ArrayList<>());
                    }
                };
        // 转换逻辑 旁边有几个
        while(map.hasNext()){
            Ceil next = map.next();
            int a = map.getNeighbourCount(next.getRow(), next.getColumn());
            //System.out.println("a:" + a);
            switch (a){
                case 2:
                    continue;
                case 3:
//                    System.out.println("set live");
                    result.get(MapInterfaceImpl.CeilState.LIVE).add(next);
//                    next.setState(MapInterfaceImpl.CeilState.LIVE);
                default:
//                    System.out.println("set dea");
                    result.get(MapInterfaceImpl.CeilState.DEAD).add(next);
//                    next.setState(MapInterfaceImpl.CeilState.DEAD);
            }
        }
        // System.out.println("result: " + result);
        map.reset();
        Set<Map.Entry<MapInterfaceImpl.CeilState, List<Ceil>>> entries = result.entrySet();
        for (Map.Entry<MapInterfaceImpl.CeilState, List<Ceil>> entry : entries) {
            MapInterfaceImpl.CeilState key = entry.getKey();
            List<Ceil> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                value.get(i).setState(key);
            }
        }
//        System.out.println("to result: " + result);

    }
}
