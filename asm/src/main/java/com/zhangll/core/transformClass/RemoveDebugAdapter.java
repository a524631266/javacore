package com.zhangll.core.transformClass;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.InputStream;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * 用于调试目的,可以忽略内部类/外部类/源文件名字,只需要继承一个空方法,不转发方法
 */
public class RemoveDebugAdapter extends ClassVisitor {
    public RemoveDebugAdapter(int api, ClassVisitor classVisitor) {
        super(ASM4, classVisitor);
    }

    @Override
    public void visitSource(String source, String debug) {

    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {

    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {

    }

}
