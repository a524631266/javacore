package com.zhangll.apm.agent;

import javassist.*;
import javassist.bytecode.Descriptor;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.*;

public class AgentC3P0 {
    public static void premain(String args, Instrumentation instrumentation){
        instrumentation.addTransformer(new ClassFileTransformer() {
            @SneakyThrows
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {

                className = Descriptor.toJavaName(className);

                // 拦截所有方法
//                System.out.println(className);
                if("com.mchange.v2.c3p0.ComboPooledDataSource".equals(className)
//                        || "com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource".equals(className)
                ){

                    ClassPool pool = new ClassPool();
                    pool.appendSystemPath();
                    CtClass ctClass = pool.get(className);
//                    CtMethod[] methods = ctClass.getDeclaredMethods();
//                    CtMethod connectionMethod = ctClass.getDeclaredMethod("getConnection");
//                    CtMethod sayHello = ctClass.getDeclaredMethod("sayHello");
                    CtMethod[] methods = ctClass.getMethods();
                    try {
                        Set<CtClass> C = new HashSet<>();
                        for (CtMethod method : methods) {

//                            agentMethod(ctClass, method);
                            String name = method.getName();
                            if("getConnection".equals(name)){
                                countAndDurationConnection(ctClass, method);
                                C.add( method.getDeclaringClass());
                            }
                        }
                        C.forEach(ctClass1 -> {
                            try{
                                ctClass1.toClass();
                            }catch (CannotCompileException e){
                                e.printStackTrace();
                            }
                        });
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
    private static void countAndDurationConnection(CtClass ctClass, CtMethod oldMethod)throws Exception{
//        if(oldMethod.getDeclaringClass().isFrozen()){
//            oldMethod.getDeclaringClass().defrost();
//        }
        CtMethod newMethod = CtNewMethod.copy(oldMethod, oldMethod.getDeclaringClass(), null);
        String oleMethodName = oldMethod.getName();
        oldMethod.setName(oleMethodName+"$agent");
        oldMethod.getDeclaringClass().addMethod(newMethod);
        //select * from admin
        newMethod.setBody("{    " +
                "        long start = System.currentTimeMillis();\n" +
                "        Object result = " + oleMethodName + "$agent($$);\n" +
                "        long duration = System.currentTimeMillis() - start;\n" +
                "        String countProperty = \"c3p0Source$agent$connect_num\";\n" +
                "        java.util.Properties properties = System.getProperties();\n" +
                "        Integer count = ($w) properties.getProperty(countProperty, \"0\");\n" +
                "        System.out.println(\"发起多少次请求:\" +count);\n" +
                "        Integer next = count + 1;\n" +
                "        properties.put(countProperty,next);\n" +
                "        return ($r) result;\n" +
                "        }");
//        long start = System.currentTimeMillis();
//        Object result = oleMethodName$agent($$);
//        long duration = System.currentTimeMillis() - start;
//
//        String countProperty = "c3p0Source$agent$connect_num";
//        Properties properties = System.getProperties();
//        Integer count = ($w)properties.getProperty(countProperty, 0);
//        properties.put(countProperty,count + 1);

//        oldMethod.setName(oleMethodName+"$agent");
//        newMethod.setName(oleMethodName);

//        ctClass.addMethod(oldMethod);


    }

    public static void main(String[] args) {
        Object ad = System.getProperties().getOrDefault("ad", 0);
        System.getProperties().put("ads", 0);
        System.out.println(ad);
    }

}
