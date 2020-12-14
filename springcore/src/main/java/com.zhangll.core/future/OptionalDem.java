package com.zhangll.core.future;

import scala.Option;

import java.util.Optional;

/**
 * Optional是java8 新增的一个项目
 *  利用optional机制可以有效放置空指针，或者在下游使用会有一个警示作用！！表示这个返回值可能
 *  为null，因此
 *
 *  哪里有用到optional
 */
public class OptionalDem {
    public static void main(String[] args) {
        String data = "bada";
        // of不能为null ，否则为空，使用ofNullable就可以了！！
        Optional<Object> o = Optional.of(1);
        System.out.println(o.isPresent());

        Optional<Integer> integer = Optional.ofNullable(null);
        // 如果存在，则触发这个方法！没有的话旧，直接报错！
        integer.ifPresent(c ->{
            System.out.println("ifPresent" + c);
        });

        // 是否存在
        System.out.println(integer.isPresent());

        Optional<String> data1 = Optional.ofNullable(null);
        if(data1.isPresent()){
            String s = data1.get();
            System.out.println(s);
        } else {
            System.out.println("empty list");
        }


        Option<Integer> apply = Option.apply(10);
        System.out.println(apply.get());


    }
}
