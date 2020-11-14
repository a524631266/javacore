package com.zhangll.core.generateclass;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

public class InterfaceToClass extends ClassVisitor {


    public InterfaceToClass(int api, ClassVisitor classVisitor) {
        super(ASM4, classVisitor);
    }

    public static void main(String[] args) {

    }
}
