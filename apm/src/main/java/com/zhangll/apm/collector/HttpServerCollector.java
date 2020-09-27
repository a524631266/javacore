package com.zhangll.apm.collector;

import com.zhangll.apm.model.PerformantModel;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class HttpServerCollector {


    /**
     * 主要根据主要的类来拦截方法
     *
     * @param classLoader
     * @param className
     */
    public CtClass buildClass(ClassLoader classLoader, String className) throws NotFoundException, CannotCompileException {

        ClassPool pool = new ClassPool();
        pool.insertClassPath(new ClassClassPath(HttpServerCollector.class));
        CtClass ctClass = pool.get(className);
        // 拦截的方法
        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

        for (CtMethod method : declaredMethods) {
            wrapper(ctClass, method);
        }
        return ctClass;
    }

    private void wrapper(CtClass ctClass, CtMethod method) throws CannotCompileException, NotFoundException {
        String originMethodName = method.getName();

        CtMethod agentMethod = CtNewMethod.copy(method, ctClass, null);
        method.setName(originMethodName + "$agent");
        CtClass returnType = method.getReturnType();
        String source = returnType.getName().equals("void")?VoidSource:GeneralSource;


        agentMethod.setBody(String.format(source,
                                        "Object o = com.zhangll.apm.collector.HttpServerCollector.start(\""+ originMethodName+ "\")",
                                        originMethodName,
                                        "o = com.zhangll.apm.collector.HttpServerCollector.error(o,e)",
                                        "com.zhangll.apm.collector.HttpServerCollector.end(o)"));

        ctClass.addMethod(agentMethod);
    }


    public static PerformantModel start(String methodName){
        PerformantModel performantModel = new PerformantModel();
        performantModel.setStartTime(System.currentTimeMillis());
        performantModel.setMethodName(methodName);
        return performantModel;
    }

    public static PerformantModel error(Object performantModel, Exception e){
        String message = e.getMessage();
        ((PerformantModel)performantModel).setError(message);
        return (PerformantModel )performantModel;
    }

    public static void end(Object performantModel){

        ((PerformantModel)performantModel).setCostTime(System.currentTimeMillis() - ((PerformantModel)performantModel).getStartTime());
        System.out.println(performantModel);
    }

//    {
//        %s;
//        Object result = null;
//        try {
//            result = %s$agent($$);
//        } catch (Exception e) {
//            %s;
//            throw e;
//        } finally {
//            %s;
//            return ($r)result;
//        }
//
//    }
//    {
//        %s;
//        try {
//            %s$agent($$);
//        } catch (Exception e) {
//            %s;
//            throw e;
//        } finally {
//            %s;
//        }
//
//    }
    private String VoidSource =
        "    {\n" +
        "        %s;\n" +
        "        try {\n" +
        "            %s$agent($$);\n" +
        "        } catch (Exception e) {\n" +
        "            %s;\n" +
        "            throw e;\n" +
        "        } finally {\n" +
        "            %s;\n" +
        "        }\n" +
        "    }";

    private String GeneralSource =
            "    {\n" +
            "        %s;\n" +
            "        Object result = null;\n" +
            "        try {\n" +
            "            result = %s$agent($$);\n" +
            "        } catch (Exception e) {\n" +
            "            %s;\n" +
            "            throw e;\n" +
            "        } finally {\n" +
            "            %s;\n" +
            "            return ($r)result;\n" +
            "        }\n" +
            "    }";


}
