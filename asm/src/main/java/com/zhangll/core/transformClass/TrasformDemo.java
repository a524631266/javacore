package com.zhangll.core.transformClass;

import com.zhangll.core.generateclass.ClassWriterDemo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM4;

public class TrasformDemo {
    public static void main(String[] args) {
//        simpleTransform();
        vistorTransform();

    }

    private static void vistorTransform() {
        byte[] bytes = ClassWriterDemo.generateByte();
        ClassWriter cw = new ClassWriter(0);
        // 定义一个适配器,用来转发事件到cw中,cw为source源
        ClassVisitor visitor = new ClassVisitor(ASM4, cw){};

        new ClassReader(bytes).accept(visitor, 0);

        byte[] bytes1 = cw.toByteArray();

        boolean equal = true;
        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] != bytes1[i]){
                equal = false;
            }
        }
        System.out.println(equal);
    }

    public static void simpleTransform() {
        byte[] bytes = ClassWriterDemo.generateByte();

        ClassWriter cw = new ClassWriter(0);
        ClassReader cr = new ClassReader(bytes);

        cr.accept(cw, 0);
        // bytes1 是 bytes的一个内存copy
        byte[] bytes1 = cw.toByteArray();
    }
}
