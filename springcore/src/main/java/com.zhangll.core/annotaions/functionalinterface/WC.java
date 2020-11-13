package com.zhangll.core.annotaions.functionalinterface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WC {
    public String word;
    public String name;
    public int count;
}
