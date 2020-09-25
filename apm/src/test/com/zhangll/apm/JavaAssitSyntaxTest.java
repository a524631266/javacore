package com.zhangll.apm;

import com.zhangll.apm.model.Person;
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
        agentMethod.setName(oldMethod.getName() + "$agent");
        ctClass.addMethod(agentMethod);
        agentMethod.setBody("{long start = System.currentTimeMillis();\n" +
                        oldMethod.getName() +"$agent($$);\n" +
                "        System.out.println(\"cost time:\"+(System.currentTimeMillis() - start));}");
        agentMethod.setName(oldMethod.getName());
        oldMethod.setName(oldMethod.getName() + "$agent");

        ctClass.toClass();
        new UserService().sayHi();
    }

    /**
     * assist 获取返回值
     * 实现对某个服务方法的监听（时间，参数、异常）打印
     * @throws Exception
     */
    @Test
    public void getReturnObject() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod oldMethod = ctClass.getDeclaredMethod("getPerson");
        // 1. 返回类型要声明，否则报错
        CtClass returnType = oldMethod.getReturnType();
        CtMethod agentMethod = CtNewMethod.copy(oldMethod, ctClass, null);
        agentMethod.setName(oldMethod.getName() + "$agent");
        ctClass.addMethod(agentMethod);
        // 3. 捕获异常　以及　输出执行时间按
        agentMethod.setBody("{" +
                "        long start = System.currentTimeMillis();\n" +
                "        "+ returnType.getName() + " result = null;" +
                "        try {\n" +
                "        result = " + oldMethod.getName() + "$agent($$);\n" +
                "        }catch (Exception e){\n" +
                "            System.out.println(\"3. error: \"+e.getMessage());\n" +
                "        }" +
                "        System.out.println(\"1. cost time:\" + (System.currentTimeMillis() - start));\n" +
                "        return result;" +
                "}");

        // 2.输出参数列表
        agentMethod.insertBefore("for (int i = 0; i < $args.length; i++) {\n" +
                "            Object o = $args[i];\n" +
                "            System.out.println(\"2. args[\" + i + \"]: type:\" + o.getClass().getName() + \"; value: \" + o);\n" +
                "        }");


        agentMethod.insertBefore("System.out.println(\"package: \" + $class);");
        CtClass[] exceptionTypes = agentMethod.getExceptionTypes();
        System.out.println("exceptionTypes:" + exceptionTypes.length);
////        System.out.println($);
//        Object[] obj = $args;
//        int count = 0;
//        for (Object o : obj) {
//            String name = o.getClass().getName();
//            System.out.println(" args[" + count++ + "] name:" + name + ";value"+ o);
//        }
//        for (int i = 0; i < $args.length; i++) {
//            Object o = $args[i];
//            System.out.println("args[" + i + "]: type:" + o.getClass().getName() + "; value" + o);
//        }



//        long start = System.currentTimeMillis();
//        Person person = new UserService().getPerson(10);
//        System.out.println("cost time:" + (System.currentTimeMillis() - start));
//        return person;
        agentMethod.setName(oldMethod.getName());
        oldMethod.setName(oldMethod.getName() + "$agent");
        // 重新加载到类加载池中
        ctClass.toClass();


        Person person = new UserService().getPerson(10);
        System.out.println(person);
    }

}
