package com.zhangll.core.util;

import com.zhangll.core.Interface2;
import com.zhangll.core.Interface2Impl;
import com.zhangll.core.transformClass.Person;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 先查看
 */
public class TraceClassVisitorDemo {
    private static PrintWriter printWriter = new PrintWriter(System.out);
    private static TraceClassVisitor tcv = new TraceClassVisitor(printWriter);
    public static void main(String[] args) throws IOException {

//        InputStream targetClass = Person.class.getClassLoader().getResourceAsStream("com/zhangll/core/transformClass/Person.class");
//        ClassReader cr = new ClassReader(targetClass);
//        cr.accept(tcv, 0);

//        showCode(Person.class);
        showCode(Interface2.class);
        showCode(Interface2Impl.class);

    }

    public static void showCode(Class<?>  cClass) throws IOException {
        String path = String.format("%s.class", AsmType.getInterName(cClass));
        InputStream resourceAsStream = cClass.getClassLoader().getResourceAsStream(path);
        ClassReader classReader = new ClassReader(resourceAsStream);
        classReader.accept(tcv, 0);
    }


    public static void showCode(byte[] bytes) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(tcv, 0);
    }
}
