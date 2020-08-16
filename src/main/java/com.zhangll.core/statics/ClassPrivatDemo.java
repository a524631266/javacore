package com.zhangll.core.statics;

public abstract class ClassPrivatDemo {
    private static int a = 1;

    public void sayA(){
        System.out.println(a);
    }
    public static void main(String[] args) {
        a += 1;
        A a = new A();
        a.sayA();
        B b = new B();
        a.sayA();
    }
}
class A extends  ClassPrivatDemo{

}
class B extends  ClassPrivatDemo{

}
