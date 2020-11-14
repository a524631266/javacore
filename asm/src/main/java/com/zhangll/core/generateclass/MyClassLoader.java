package com.zhangll.core.generateclass;

public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(name.endsWith("Comparable")){
            byte[] bytes = ClassWriterDemo.generateByte();

            return defineClass(name , bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }


    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> comparable = new MyClassLoader().findClass("com.zhangll.core.Comparable");

        System.out.println(comparable);
    }
}
