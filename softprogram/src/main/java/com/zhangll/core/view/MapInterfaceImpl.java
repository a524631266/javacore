package com.zhangll.core.view;

import com.zhangll.core.model.TransferMap;
import com.zhangll.core.ui.GrameFrame;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MapInterfaceImpl implements MapInterface {

    private final List<List<Ceil>> map ;
    private int maxRow; // 地图最大行
    private int maxColumn; // 地图最大列
    private int nRow = 0;
    private int nColumn = 0;

    GrameFrame grameFrame;


    public MapInterfaceImpl( int column, int row) {
        this.map = initMap(column, row);

        this.maxColumn = column;
        this.maxRow = row;
        grameFrame = new GrameFrame(maxRow, maxColumn);
    }

    public List<List<Ceil>> initMap(int column, int row) {
        ArrayList<List<Ceil>> lists = new ArrayList<List<Ceil>>() {};
        for (int i = 0; i < row; i++) {
            ArrayList<Ceil> inner = new ArrayList<>();
            for (int j = 0; j < column; j++) {
                Random random = new Random();
                if(random.nextInt(2) % 2 == 0){
                    inner.add(new Ceil(i,j, CeilState.DEAD));
                }else {
                    inner.add(new Ceil(i,j, CeilState.LIVE) );
                }
            }
            lists.add(inner);
        }
        return lists;
    }


    @Override
    public void init() {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                int randomState = new Random(1).nextInt();
                CeilState value = CeilState.getValue(randomState);
                map.get(i).get(j).setState(value);
            }
        }
        print();
    }

    @Override
    public void update(int row, int column, CeilState state) {
        Ceil ceil = this.map.get(row).get(column);
        ceil.state = state;
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
        //TODO DIRECTIONS
        int count = 0;
        System.out.println(String.format("row:%d, %d", row, column));
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
            System.out.println(String.format("%d, %d", cRow, cColumn));
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
                lables[i][j] = ceil.state.equals(CeilState.LIVE)?1:0;
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

    @Data
    @AllArgsConstructor
    public class Ceil {
        private int row;
        private int column;
        private CeilState state;
    }
}
