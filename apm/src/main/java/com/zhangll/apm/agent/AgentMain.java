package com.zhangll.apm.agent;

import com.zhangll.apm.UserService;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.*;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class AgentMain {
    public static void premain3(String args, Instrumentation instrumentation){

    }

    /**
     * 1. 仍然会打印出日志记录
     * Hiiiiiii
     * 1601038762364
     * hello
     * @param args
     * @param instrumentation
     * @throws Exception
     */
    public static void premain3(String args, Instrumentation instrumentation) throws Exception {

//        UserService userService = new UserService();

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                className = className.replace("/",".");
//                System.out.println("className:" + className);
                if (className.equals("com.zhangll.apm.UserService")) {
                    System.out.println("instrument:" + className);
                    byte[] bytes = null;
                    try {
                        ClassPool pool = ClassPool.getDefault();
                        CtClass ctClass = pool.get(className);
                        CtMethod newMethod = CtNewMethod.make("public void ss(){\n" +
                                "            System.out.println(\"new method12323\");\n" +
                                "        }", ctClass);
                        ctClass.addMethod(newMethod);
                        bytes = ctClass.toBytecode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bytes;

                }
                return null;
            }
        },true);

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod sayHi = ctClass.getDeclaredMethod("sayHi");
        sayHi.insertAfter("System.out.println(\"hhhh:\"+System.currentTimeMillis());");
        // 2. 添加新方法  无法添加成功
//        CtMethod newMethod = CtNewMethod.make("public void ss(){\n" +
//                "            System.out.println(\"new method\");\n" +
//                "        }", ctClass);
//        ctClass.addMethod(newMethod);
        // 3. java.lang.RuntimeException: com.zhangll.apm.UserService class is frozen
        // 因为　ctClass.toBytecode()方式传入
//        instrumentation.redefineClasses(new ClassDefinition(UserService.class, ctClass.toBytecode()));
        UserService userService = new UserService();
        userService.sayHi();
        Method ss = userService.getClass().getDeclaredMethod("ss", null);
        ss.invoke(userService);

    }

    /**
     *
     * @param args
     * @param instrumentation 指令码
     * @throws UnmodifiableClassException
     * @throws IOException
     * @throws CannotCompileException
     * @throws ClassNotFoundException
     * @throws NotFoundException
     */
    public static void premain2(String args, Instrumentation instrumentation) throws UnmodifiableClassException, IOException, CannotCompileException, ClassNotFoundException, NotFoundException {

        // 0. 可以发现String没有 在 transformer中打印出来
        // 1.这个是主动加载的类,
        // instrumentation 不会拦截
        UserService.class.getName();

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                /**
                 *
                 */
                System.out.println("ClassName:" + className);
                return null;
                // 如果返回null 则为原先的class装载
            }
        },true);
//        instrumentation.
        // 2. 当之前已经装载过了,那么可以进行再次注入
        // a) 在addTransformer中添加true
        // b) <Can-Retransform-Classes>true</Can-Retransform-Classes>
        instrumentation.retransformClasses(UserService.class);
        System.out.println("hell" + args);

        new UserService().sayHello();
        // 3.跳过类装载
        // <Can-Redefine-Classes>true</Can-Redefine-Classes>
        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();
        CtClass ctClass = pool.get("com.zhangll.apm.UserService");
        CtMethod sayHello = ctClass.getDeclaredMethod("sayHello");
        sayHello.insertAfter("System.out.println(\"hello my dear\");");
        System.out.println("**********3**********");
        instrumentation.redefineClasses(new ClassDefinition(UserService.class, ctClass.toBytecode()));
        System.out.println("*********4***********");


    }

}
