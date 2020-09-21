package com.zhangll.core.view;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.ui.GrameFrame;
import com.zhangll.core.view.strategy.InitStrategy;
import com.zhangll.core.view.strategy.RandomInitStrategy;

import java.util.List;

public class MapInterfaceImpl implements MapInterface, InitStrategy {

    private final List<List<Ceil>> map ;
    private int maxRow; // 地图最大行
    private int maxColumn; // 地图最大列
    private int nRow = 0;
    private int nColumn = 0;
    private final InitStrategy initStrategy;
    GrameFrame grameFrame;


    public MapInterfaceImpl( int column, int row) {
        this(column, row, new RandomInitStrategy());
    }

    public MapInterfaceImpl( int column, int row, InitStrategy initStrategy) {
        this.initStrategy = initStrategy;
        this.map = initMap(column, row);
        this.maxColumn = column;
        this.maxRow = row;
        grameFrame = new GrameFrame(row, column);
    }



    @Override
    public List<List<Ceil>> initMap(int column, int row) {
        return initStrategy.initMap(column, row);
    }


    @Override
    public void update(int row, int column, CeilState state) {
        Ceil ceil = this.map.get(row).get(column);
        ceil.setState(state);
    }

    public static int[][] DIRECTIONS=new int[][]{
            {-1,-1},{-1,0},{-1,1},
            {0,-1},{0,1},
            {1,-1},{1,0},{1,1}
    };

    /**
     * 存活的节点
     * @param row
     * @param column
     * @return
     */
    @Override
    public int getNeighbourCount(int row, int column) {
        int count = 0;
//        System.out.println(String.format("row:%d, %d", row, column));
        for (int i = 0; i < DIRECTIONS.length; i++) {
            int[] direction = DIRECTIONS[i];
            int cRow = row + direction[0];
            int cColumn = column + direction[1];
            // 如果-1 => 10
            if((cRow)<0){
                cRow += maxRow;
            }else if(cRow >= maxRow){
                cRow -= maxRow;
            }
            if((cColumn)<0){
                cColumn += maxColumn;
            }else if(cColumn >= maxColumn){
                cColumn -= maxColumn;
            }
//            System.out.println(String.format("%d, %d", cRow, cColumn));
            Ceil ceil = map.get(cRow).get(cColumn);
            if(ceil.getState().equals(CeilState.LIVE)){
                count += 1;
            }
        }
        return count;
    }

    @Override
    public void reset() {
        this.nRow = 0;
        this.nColumn = 0;
    }


    @Override
    public void print() {
        int[][] lables = new int[maxRow][maxColumn];
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                Ceil ceil = map.get(i).get(j);
                lables[i][j] = ceil.getState().equals(CeilState.LIVE)?1:0;
            }
        }
        System.out.println(lables);
        System.out.println(map);
        grameFrame.refreshFrame(lables);
    }

    @Override
    public boolean hasNext() {
        return !(nRow == maxRow );
    }

    /**
     * 横向扫描
     * @return
     */
    @Override
    public Ceil next() {
        Ceil ceil = map.get(nRow).get(nColumn);
        if(nColumn >= (maxColumn-1)){
            nRow +=1;
            nColumn = 0;
        } else{
            nColumn +=1;
        }
        return ceil;
    }

    public enum CeilState {
        LIVE(0), DEAD(1);
        private int value;
        CeilState(int i) {
            this.value = i;
        }
        public int getValue() {
            return value;
        }

        public static CeilState getValue(int i) {
            return i==0?LIVE:DEAD;
        }
    }

}
