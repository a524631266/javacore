package com.zhangll.core.util;

import com.zhangll.core.transformClass.Person;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class TraceClassVisitorDemo {
    public static void main(String[] args) throws IOException {
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor tcv = new TraceClassVisitor(printWriter);

        InputStream targetClass = Person.class.getClassLoader().getResourceAsStream("com/zhangll/core/transformClass/Person.class");

        ClassReader cr = new ClassReader(targetClass);
        cr.accept(tcv, 0);
    }
}
