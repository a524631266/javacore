package com.zhangll.core.method;

import com.zhangll.core.transformClass.MySimpleClassLoader;
import com.zhangll.core.util.TraceClassVisitorDemo;
import com.zhangll.core.util.WrapperMethodUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

class TestSayHello{

    public void sayHello() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

//    public void sayHello2() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
//    }
}

class TestSayHello2{

    public void sayHello() throws InterruptedException {
        long start = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);
        long cost = System.currentTimeMillis() - start;
        System.out.println(cost);
    }

    public void sayHello2() throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println("asdf");
        TimeUnit.SECONDS.sleep(100);
        long cost = System.currentTimeMillis() - start;
        System.out.println("hahaha");
        System.out.println(cost);
    }

//    public void sayHello2() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(1);
//    }
}

public class ClassVisitorForMethod extends ClassVisitor implements Opcodes {
    public ClassVisitorForMethod(ClassVisitor classVisitor) {
        super(ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("visitMethod name :" + name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if(mv!=null) {
            mv = new Sleep3MethodVisitor(mv);
        }
        return mv;
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
//        WrapperMethodUtil.wrapperMethod(TestSayHello.class, ClassVisitorForMethod::new);
        byte[] bytes = WrapperMethodUtil.wrapperMethod(TestSayHello2.class, ClassVisitorForMethod::new);
        Class<?> aClass = new MySimpleClassLoader(bytes).findClass(TestSayHello2.class.getName());
        TraceClassVisitorDemo.showCode(aClass);
        TraceClassVisitorDemo.storeClass(bytes, aClass);
        // 为转化之后的3秒
        new TestSayHello2().sayHello();
    }
}
