package com.zhangll.core.annotaions.functionalinterface;

import lombok.ToString;

@ToString
class Dog{
    String name;
    int age = -1;
    Dog(){
        name = "has no args";
    }

    public Dog(String name) {
        this.name = name;
    }

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

interface MakeNoArgs{
    Dog make();
}

interface MakOneArgs{
    Dog make(String name);
}

interface MakeTwoArgs{
    Dog make(String name, int age);
}

/**
 * Dog(name=has no args, age=-1)
 * Dog(name=one args, age=-1)
 * Dog(name=two args, age=20)
 */
public class ConstructorReference {
    public static void main(String[] args) {
        // 一个无参数实例的构建
        MakeNoArgs mna = Dog::new;
        Dog make = mna.make();
        System.out.println(make);

        // 构建一个拥有执行Dog对象的实例
        MakOneArgs mno = Dog::new;
        System.out.println(mno.make("one args"));


        MakeTwoArgs mnt = Dog::new;
        System.out.println(mnt.make("two args", 20));
    }
}
