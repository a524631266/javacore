package com.zhangll.core.view.strategy;

public class BarInitStrategy extends InitStrategy {
    /**
     * 每五格 (1,2),(2,2)(3,2) 为活着的
     * @param column
     * @param row
     * @return
     */
    protected boolean find(int row, int column){
        return (row % 5 == 1 || row % 5 == 2 || row % 5 == 3) && (column % 5 == 2);
    }
}
