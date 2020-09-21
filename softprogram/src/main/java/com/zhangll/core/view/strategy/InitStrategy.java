package com.zhangll.core.view.strategy;

import com.zhangll.core.model.Ceil;

import java.util.List;

public interface InitStrategy {
    List<List<Ceil>> initMap(int column, int row);
}
