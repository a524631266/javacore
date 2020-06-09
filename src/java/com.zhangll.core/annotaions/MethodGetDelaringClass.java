package com.zhangll.core.annotaions;

import java.lang.reflect.Method;

class DD extends Object{
    public void sayhell2(){
        System.out.println("Object");
    }
}

interface Father{
    void sayFather();
}

public class MethodGetDelaringClass {

    static class Inner implements Father{
        // fanshe
        public Inner(){}

        public void sayhell(){
            System.out.println("eee");
        }

        @Override
        public void sayFather() {
            sayhell();
        }
    }
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        Method sayhell = Inner.class.getMethod("sayhell");
        // 1.获取DeclaringClass
        // 内部类，无法反射生成动态类，因为类是内部的，所以没有init方法，无法在外部实例化
        // 内部静态类可以使用反射进行调用
        Class<?> declaringClass = sayhell.getDeclaringClass();

        boolean equals = Object.class.equals(declaringClass);
        System.out.println("eau:" + equals);
        Object o = Inner.class.newInstance();
        System.out.println(declaringClass.getName());

        // 2. 查找是不是Object方法
        Method sayhell2 = DD.class.getMethod("hashCode");
        boolean equals2 = Object.class.equals(sayhell2.getDeclaringClass());
        System.out.println("eau:" + equals2);


        // 3.获取方法的modifiers，修饰符
        int modifiers = sayhell.getModifiers();
        System.out.println(modifiers);
    }
}
