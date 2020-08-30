package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.service.UserService;
import coderead.nio.netty.rpc.service.UserServiceImpl;
import jdk.internal.org.objectweb.asm.Type;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class RpcServerTest {

    @Test
    public void registerBeans() {
        String methodName = "getAge";
        RpcServer rpcServer = new RpcServer();
        rpcServer.registerBeans(UserService.class, new UserServiceImpl());
        for (Method method : UserService.class.getMethods()) {

            String methodDescriptor = Type.getMethodDescriptor(method);
            String key = UserService.class.getName() + methodDescriptor;
            RpcServer.ServerBean serverBean = rpcServer.beanMap.get(key);
            if (!method.getName().equals(methodName)) {
                continue;
            }
            for (int i = 0; i < 10; i++) {
                Object invoke = serverBean.invoke(new String[]{"haha"});
                System.out.println("invoke:" + invoke);
            }
        }
    }
}