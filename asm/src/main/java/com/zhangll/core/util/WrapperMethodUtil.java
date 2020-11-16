package com.zhangll.core.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class WrapperMethodUtil {
    /**
     * 添加一个ClassWrite类
     * @param aClass
     * @param function
     * @throws IOException
     */
    public static byte[] wrapperMethod(Class<?> aClass, Function<ClassVisitor,ClassVisitor> function) throws IOException {
        InputStream sourceStream = aClass.getClassLoader().getResourceAsStream(AsmType.getInterName(aClass) + ".class");
        ClassReader cr = new ClassReader(sourceStream);
        // 不计算栈转帧
        ClassWriter cw = new ClassWriter(0);
        ClassVisitor newCw = function.apply(cw);
        cr.accept(newCw, 0);
        byte[] bytes = cw.toByteArray();
        return bytes;
    }
}
