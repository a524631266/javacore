package com.zhangll.core.generateclass;

import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.*;

public class ClassWriterDemo {
    public static void main(String[] args) {
        generateByte();
    }

    public static byte[] generateByte() {
        ClassWriter cw = new ClassWriter(0);

        // package com.zhangll.core;
        //public interface Comparable extends Interface2 {
        // signature为范型,本例没有范型因此为null
        cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "com/zhangll/core/Comparable",
                null,
                "java/lang/Object",
                new String[]{"com/zhangll/core/Interface2"}
        );

        // int LESS = -1;
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "LESS",
                "I",
                null,
                new Integer(-1)
        ).visitEnd();
        // int EQUAL = 0;
        cw.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL,
                "EQUAL",
                "I",
                null,
                new Integer(0)
        ).visitEnd();
        // int GREATER = 1;
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "GREATER",
                "I",
                null,
                new Integer(1)).visitEnd();
        // int compareTo(Object o);
        cw.visitMethod(ACC_PUBLIC+ ACC_ABSTRACT,
                "compareTo",
                "(Ljava/lang/Object;)I",
                null, null).visitEnd();

        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
//        System.out.println(bytes);
        return bytes;
    }
}
