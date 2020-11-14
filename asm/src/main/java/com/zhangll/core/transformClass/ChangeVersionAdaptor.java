package com.zhangll.core.transformClass;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_5;

/**
 * 修改v1_5
 */
public class ChangeVersionAdaptor extends ClassVisitor {

    public ChangeVersionAdaptor(int api, ClassVisitor classVisitor) {
        super(ASM4, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(V1_5, access, name, signature, superName, interfaces);
    }
}
