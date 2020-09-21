package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.ArrayList;
import java.util.List;

public class BubooInitStrategy extends InitStrategy {
    /**
     * 每五格 (1,2),(2,2)(3,2) 为活着的
     * @param column
     * @param row
     * @return
     */
    protected boolean find(int row, int column){

        return (row % 10==0 && column % 10==0)
                || (row % 10==1 && column % 10==1)
                || (row % 10==1 && column % 10==2)
                || (row % 10==2 && column % 10==0)
                || (row % 10==2 && column % 10==1);
    }
}
