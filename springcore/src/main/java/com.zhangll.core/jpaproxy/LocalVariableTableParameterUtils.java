package com.zhangll.core.jpaproxy;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

class P{
    void sayHello(int id, String name){
        System.out.println(id);
    }
}
public class LocalVariableTableParameterUtils {
    private static LocalVariableTableParameterNameDiscoverer paramDiscover = new  LocalVariableTableParameterNameDiscoverer();

    public static void main(String[] args) throws NoSuchMethodException {
        // 局部变量表可以通过paramDiscover获取到
        Method sayHello = P.class.getDeclaredMethod("sayHello", int.class, String.class);
        String[] parameterNames = paramDiscover.getParameterNames(sayHello);
        System.out.println(parameterNames);
        // parameterNames = {String[2]@985}
        // 0 = "id"
        // 1 = "name"
        // 无法通过接口方法获取参数名称
        Method generate = InterfacePrototype.class.getDeclaredMethod("generate", int.class, String.class);
        String[] parameterNames2 = paramDiscover.getParameterNames(generate);
        System.out.println(parameterNames2);
    }
}
