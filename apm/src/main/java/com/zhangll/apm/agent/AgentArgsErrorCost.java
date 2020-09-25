package com.zhangll.apm.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.Descriptor;
import lombok.SneakyThrows;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentArgsErrorCost {
    public static void premain(String args, Instrumentation instrumentation){
        instrumentation.addTransformer(new ClassFileTransformer() {
            @SneakyThrows
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

                className = Descriptor.toJavaName(className);

                // 拦截所有方法

                if("com.zhangll.apm.UserService".equals(className)){

                    ClassPool pool = ClassPool.getDefault();
                    CtClass ctClass = pool.get(className);
                    CtMethod[] methods = ctClass.getDeclaredMethods();

//                    CtMethod sayHello = ctClass.getDeclaredMethod("sayHello");
                    try {
                        for (CtMethod method : methods) {

                            agentMethod(ctClass, method);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    byte[] bytes = ctClass.toBytecode();
                    return bytes;
                }

                return null;
            }
        });
    }


    private static void agentMethod(CtClass ctClass, CtMethod oldMethod) throws Exception{
        // 1. 返回类型要声明，否则报错
        CtClass returnType = oldMethod.getReturnType();
        CtMethod agentMethod = CtNewMethod.copy(oldMethod, ctClass, null);
        agentMethod.setName(oldMethod.getName() + "$agent");
        ctClass.addMethod(agentMethod);
        // 3. 捕获异常　以及　输出执行时间按
//        agentMethod.setBody("{" +
//                "        long start = System.currentTimeMillis();\n" +
//                "        Object result = null;" +
//                "        try {\n" +
//                "           if ($type.toString().equals(\"void\")){\n" +
//                "               " + oldMethod.getName() + "$agent($$);\n" +
//                "               return ;\n" +
//                "           } else {\n" +
//                "               result = " + oldMethod.getName() + "$agent($$);\n" +
//                "           }" +
//                "        }catch (Exception e){\n" +
//                "            System.out.println(\"3. error: \"+e.getMessage());\n" +
//                "           if ($type.toString().equals(\"void\")){\n" +
//                "            return ;\n" +
//                "           }" +
//                "        }" +
//                "        System.out.println(\"1. cost time:\" + (System.currentTimeMillis() - start));\n" +
//                "        return ($r)result;" +
//                "}");
        if(!returnType.getName().equals("void")){
            agentMethod.setBody("{" +
                    "        long start = System.currentTimeMillis();\n" +
                    "        Object result = null;" +
                    "        try {\n" +
                    "           result = " + oldMethod.getName() + "$agent($$);\n" +
                    "        }catch (Exception e){\n" +
                    "            System.out.println(\"3. error: \"+e.getMessage());\n" +
                    "        }" +
                    "        System.out.println(\"1. cost time:\" + (System.currentTimeMillis() - start));\n" +
                    "        return ($r)result;" +
                    "}");
        } else{
            agentMethod.setBody("{" +
                    "        long start = System.currentTimeMillis();\n" +
                    "        try {\n" +
                    "        " + oldMethod.getName() + "$agent($$);\n" +
                    "        }catch (Exception e){\n" +
                    "            System.out.println(\"3. error: \"+e.getMessage());\n" +
                    "        }" +
                    "        System.out.println(\"1. cost time:\" + (System.currentTimeMillis() - start));\n" +
                    "}");
        }

        // 2.输出参数列表
        agentMethod.insertBefore("for (int i = 0; i < $args.length; i++) {\n" +
                "            Object o = $args[i];\n" +
                "            System.out.println(\"2. args[\" + i + \"]: type:\" + o.getClass().getName() + \"; value: \" + o);\n" +
                "        }");


        agentMethod.insertBefore("System.out.println(\"package: \" + $class);");
        agentMethod.insertBefore("System.out.println(\"$type: \" + ($type.toString().equals(\"void\")));");

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
    }


}
