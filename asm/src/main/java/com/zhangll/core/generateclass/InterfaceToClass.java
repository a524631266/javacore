package com.zhangll.core.generateclass;

import com.zhangll.core.Interface2;
import com.zhangll.core.transformClass.MySimpleClassLoader;
import com.zhangll.core.transformClass.Person;
import com.zhangll.core.util.AsmType;
import com.zhangll.core.util.TraceClassVisitorDemo;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;

import static org.objectweb.asm.Opcodes.*;

public class InterfaceToClass extends ClassVisitor {

    class InnerFieldVisitor extends FieldVisitor{

        public InnerFieldVisitor( FieldVisitor fieldVisitor) {
            super(ASM4, fieldVisitor);
        }

    }

    class ExtendsMethodAdapter extends MethodVisitor {

        private final Type[] argumentTypes;
        private String[] argumentNames;
        private int cur = 0;
        public ExtendsMethodAdapter(MethodVisitor mv, Type[] argumentTypes) {
            super(ASM5, mv);
            this.argumentTypes = argumentTypes;
            this.argumentNames = new String[argumentTypes.length];
        }

        @Override
        public void visitInsn(int opcode) {
            System.out.println("in code:" + opcode);
            super.visitInsn(opcode);
        }

        /**
         * 获取参数和名称
         *
         * @param name
         * @param access
         */
        @Override
        public void visitParameter(String name, int access) {
            System.out.println("in method:" + name);
            argumentNames[cur++] = name;
            super.visitParameter(name, access);
        }

        /**
         * 方法注解
         *
         * @param parameter
         * @param descriptor
         * @param visible
         * @return
         */
        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
            System.out.println("paramter:" + parameter);
            System.out.println("descriptor:" + descriptor);
            return super.visitParameterAnnotation(parameter, descriptor, visible);
        }

        @Override
        public void visitAttribute(Attribute attribute) {
            System.out.println("attribute.type: " + attribute.type);
            super.visitAttribute(attribute);
        }

        @Override
        public void visitEnd() {

            super.visitEnd();
        }
    }


    private final Class<?> inte;

    /**
     * @param classVisitor
     * @param inte         接口类
     */
    public InterfaceToClass(ClassVisitor classVisitor, Class<?> inte) {
        super(ASM4, classVisitor);
        this.inte = inte;
    }

//    @Override
//    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        System.out.println("superName:" + superName);
//        System.out.println(name);
//        super.visit(version, ACC_PUBLIC, AsmType.getInterName(inte) + "Impl", signature, name, interfaces);
//    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("method:  name:" + name);
        System.out.println("method:  descriptor:" + descriptor);

        Type[] argumentTypes = Type.getArgumentTypes(descriptor);
        System.out.println("method:  signature:" + signature);
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);

        if (mv != null) {
            mv = new ExtendsMethodAdapter(mv, argumentTypes);
        }
        return mv;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Class<Interface2> aClass = Interface2.class;
        InputStream targetClass = aClass.getClassLoader().getResourceAsStream(AsmType.getInterName(aClass) + ".class");
        ClassReader cr = new ClassReader(targetClass);
        ClassWriter cw = new ClassWriter(0);
        InterfaceToClass icw = new InterfaceToClass(cw, aClass);
        cr.accept(icw, 0);
        byte[] bytes = cw.toByteArray();
        TraceClassVisitorDemo.showCode(bytes);
//        Class<?> aClass1 = new MySimpleClassLoader(bytes).findClass(aClass.getName());
//        System.out.println(aClass1);
    }
}
