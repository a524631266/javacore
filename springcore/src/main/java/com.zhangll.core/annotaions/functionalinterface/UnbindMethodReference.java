package com.zhangll.core.annotaions.functionalinterface;


class X {
    String f(){return "X::f";}
}
class Y {
    String s(){return "Hello";}
}

class ZZ{
    String aa(Integer count){return  "count :" + count;};
}

interface TransformX {
    String transform(X x);
}
interface MakeString {
    String make();
}

interface Count {
    // 相当于this方法，普通方法中的策略
    String count(ZZ zz, Integer count);
}

public class UnbindMethodReference {
    public static void main(String[] args) {
        // java 自动转换函数方法
        // 未绑定的方法引用，隐含着一个this对象需要传递
//        MakeString s = X::f; // error
        TransformX f = X::f;
//        TransformX s = Y::s;
        X x = new X();
        System.out.println(f.transform(x));
//        System.out.println(f2.transform(x));
        X x2 = new X();
        System.out.println(x2.f());


        Count count = ZZ::aa;
        ZZ zz = new ZZ();
        System.out.println(count.count(zz, 123));
    }
}
