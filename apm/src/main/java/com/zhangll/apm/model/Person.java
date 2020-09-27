package com.zhangll.apm.model;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class Person {
    final int id;
    final String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
