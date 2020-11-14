package com.zhangll.core;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * running for that invoke the method
 */
public class App {
    public static void main(String[] args) throws IOException {
        ClassPrinter cp = new ClassPrinter();
        ClassReader classReader = new ClassReader("com.zhangll.core.Interface2");
        classReader.accept(cp, 0);
    }


}
