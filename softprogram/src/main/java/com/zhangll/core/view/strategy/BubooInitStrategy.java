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

        return (row==0 && column==0)
                || (row==1 && column==1)
                || (row==1 && column==2)
                || (row==2 && column==0)
                || (row==2 && column==1);
    }
}
