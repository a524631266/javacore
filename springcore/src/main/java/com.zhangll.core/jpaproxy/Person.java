package com.zhangll.core.jpaproxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
//@AllArgsConstructor
public class Person {
    private int age = 1;
    private String name;
    public Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
