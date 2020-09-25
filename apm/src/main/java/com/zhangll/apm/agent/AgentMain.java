package com.zhangll.apm.agent;

import com.zhangll.apm.UserService;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class AgentMain {
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
    public static void premain(String args, Instrumentation instrumentation) throws UnmodifiableClassException, IOException, CannotCompileException, ClassNotFoundException, NotFoundException {

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
