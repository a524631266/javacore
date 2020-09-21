package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInitStrategy implements InitStrategy {
    @Override
    public List<List<Ceil>> initMap(int column, int row) {
        ArrayList<List<Ceil>> lists = new ArrayList<List<Ceil>>() {};
        for (int i = 0; i < row; i++) {
            ArrayList<Ceil> inner = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                Random random = new Random();
                if(random.nextInt(2) % 2 == 0){
                    inner.add(new Ceil(i,j, MapInterfaceImpl.CeilState.DEAD));
                }else {
                    inner.add(new Ceil(i,j, MapInterfaceImpl.CeilState.LIVE) );
                }
            }
            lists.add(inner);
        }
        return lists;
    }
}
