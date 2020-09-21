package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class InitStrategy {
    public List<List<Ceil>> initMap(int column, int row) {
        List<List<Ceil>> lists = new ArrayList<List<Ceil>>();
        for (int i = 0; i < row; i++) {
            ArrayList<Ceil> inner = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                if(!find(i, j)){
                    inner.add(new Ceil(i,j, MapInterfaceImpl.CeilState.DEAD));
                }else {
                    inner.add(new Ceil(i,j, MapInterfaceImpl.CeilState.LIVE) );
                }
            }
            lists.add(inner);
        }
        return lists;
    }

    abstract boolean find(int row, int column);
}
