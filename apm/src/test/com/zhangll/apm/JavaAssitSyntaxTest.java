package com.zhangll.apm;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.junit.Test;

public class JavaAssitSyntaxTest {
    /**
     *
     */
    @Test
    public void insertTime() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod sayHi = ctClass.getDeclaredMethod("sayHi");

        sayHi.insertBefore("long start = System.currentTimeMillis();");
        sayHi.insertAfter("System.out.println(\"System.currentTimeMillis() - start\");");

        new UserService().sayHi();
    }
}
