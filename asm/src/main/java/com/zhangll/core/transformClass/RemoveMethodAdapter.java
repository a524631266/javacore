package com.zhangll.core.transformClass;

import com.zhangll.core.generateclass.MyClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * 删除特定方法
 */
public class RemoveMethodAdapter extends ClassVisitor {
    // 忽略的名字
    private final String mName;
    // 以及描述符
    private final String mDesc;

    public RemoveMethodAdapter(
            ClassVisitor cv, String mName, String mDesc) {
        super(ASM4, cv);
        this.mName = mName;
        this.mDesc = mDesc;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if(name.equals(mName) && mDesc.equals(descriptor)){
            return null;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InputStream targetClass = Person.class.getClassLoader().getResourceAsStream("com/zhangll/core/transformClass/Person.class");

        ClassReader cr = new ClassReader(targetClass);
        ClassWriter cw = new ClassWriter(0);
        // public void sayHello(String name){
        cr.accept(getSayHello(cw), 0);

        byte[] bytes = cw.toByteArray();

        Class<Person> aClass = (Class<Person>) new MySimpleClassLoader(bytes).findClass("com.zhangll.core.transformClass.Person");

        // invoke method , 如果没有就直接报错 java.lang.NoSuchMethodException
        Method sayHello = aClass.getDeclaredMethod("sayHello", String.class);

        // 通过asm获取到的class 不能触发
        Person obj = new Person();
        sayHello.invoke(obj, "hhi");
    }

    public static RemoveMethodAdapter getSayHello(ClassWriter cw) {
        return new RemoveMethodAdapter( cw,"sayHello","(Ljava/lang/String;)V");
    }
}
