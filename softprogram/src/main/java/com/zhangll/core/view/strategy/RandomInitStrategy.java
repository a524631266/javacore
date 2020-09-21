package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;
import com.zhangll.core.view.MapInterfaceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInitStrategy extends InitStrategy {
    @Override
    boolean find(int row, int column) {
        Random random = new Random();
        return random.nextInt(2) % 2 == 0;
    }
}
