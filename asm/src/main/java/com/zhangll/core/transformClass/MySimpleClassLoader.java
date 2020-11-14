package com.zhangll.core.transformClass;

public class MySimpleClassLoader extends ClassLoader {
    private final byte[] classBytes ;

    public MySimpleClassLoader(byte[] classBytes) {
        this.classBytes = classBytes;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (classBytes!=null){
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.findClass(name);
    }

}
