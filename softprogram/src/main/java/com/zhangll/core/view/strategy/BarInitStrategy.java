package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BarInitStrategy implements InitStrategy {
    /**
     * 每五格 (1,2),(2,2)(3,2) 为活着的
     * @param column
     * @param row
     * @return
     */
    @Override
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

    private boolean find(int row, int column){
        return (row % 5 == 1 || row % 5 == 2 || row % 5 == 3) && (column % 5 == 2);
    }
}
