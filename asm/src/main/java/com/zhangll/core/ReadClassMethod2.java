package com.zhangll.core;

import java.io.IOException;
import java.io.InputStream;

public class ReadClassMethod2 {
    public static void main(String[] args) {
        readClassMethod2();
    }
    public static void readClassMethod2() throws IOException {
        InputStream resourceAsStream = App.class.getClassLoader().getResourceAsStream("com.zhangll.core.Interface2".replace(".", "/") + ".class");
        byte[] buffer = new byte[1024];
        resourceAsStream.read(buffer);
    }
}
