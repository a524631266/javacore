package com.zhangll.core.model;

import com.zhangll.core.view.MapInterfaceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ceil {
    private int row;
    private int column;
    private MapInterfaceImpl.CeilState state;
}
