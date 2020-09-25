package com.zhangll.apm;

import javassist.*;
import org.junit.Test;

public class JavaAssitSyntaxTest {
    /**
     *no such field: start
     */
    @Test
    public void insertTimeWrong() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod sayHi = ctClass.getDeclaredMethod("sayHi");

        sayHi.insertBefore("long start = System.currentTimeMillis();");
        sayHi.insertAfter("System.out.println(System.currentTimeMillis() - start);");

        ctClass.toClass();


        new UserService().sayHi();
    }
    /**
     * Hiiiiiii
     * cost time:1
     */
    @Test
    public void insertTimeRight() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod oldMethod = ctClass.getDeclaredMethod("sayHi");
        CtMethod agentMethod = CtNewMethod.copy(oldMethod, ctClass, null);
        agentMethod.setName(oldMethod.getName() + "$abc");
        ctClass.addMethod(agentMethod);
        agentMethod.setBody("{long start = System.currentTimeMillis();\n" +
                        oldMethod.getName() +"$abc($$);\n" +
                "        System.out.println(\"cost time:\"+(System.currentTimeMillis() - start));}");
        agentMethod.setName(oldMethod.getName());
        oldMethod.setName(oldMethod.getName() + "$abc");

        ctClass.toClass();
        new UserService().sayHi();
    }

}
