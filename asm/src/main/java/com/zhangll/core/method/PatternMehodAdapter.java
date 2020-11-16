package com.zhangll.core.method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class PatternMehodAdapter extends MethodVisitor implements Opcodes {
    protected final static int SEEN_NOTHING = 0;
    // 使用有限自动机
    protected int state;

    public PatternMehodAdapter(MethodVisitor mv) {
        super(ASM5, mv);
    }
    protected abstract void visitInst();
}
