package com.zhangll.core.jpaproxy;

import com.google.common.collect.Sets;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProxyInterface {
    private static LocalVariableTableParameterNameDiscoverer paramDiscover = new  LocalVariableTableParameterNameDiscoverer();
    public static void main(String[] args) {
        Object o = Proxy.newProxyInstance(InterfacePrototype.class.getClassLoader(), new Class[]{InterfacePrototype.class},
                new MyHandler());
        if(o instanceof InterfacePrototype){
//            Person zhangll = ((InterfacePrototype) o).generate(10, "zhangll");
            Person zhangll = ((InterfacePrototype) o).generate("zhangll", 10);
            System.out.println(zhangll);
        }
    }

    /**
     * 通过代理对象触发interface中的方法，类似于mybatis中的代理方法
     */
    private static class MyHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (!isObjectMethod(method)) {
                int modifiers = method.getModifiers();
                Class<?> returnType = method.getReturnType();
                Class<?>[] parameterTypes = method.getParameterTypes();
                // 1. 如何获取参数的方法名
                String[] parameterNames = paramDiscover.getParameterNames(method);
                System.out.println("parameterNames: " + parameterNames);
                String name = method.getName();
                // 抽象方法/接口方法无法触发的，问题是如何判断是接口或者抽象方法
//                method.invoke(null, args);
                // 由于 paramDiscover无法正确识别接口（非一般类）的方法参数名称，因此需要默认顺序
                Constructor<?>[] constructors = returnType.getConstructors();
                for (Constructor<?> constructor : constructors) {
                    if( constructor.getParameterCount() == args.length){

                        Class<?>[] parameterTypes1 = constructor.getParameterTypes();
                        Set<Class<?>> collect = Stream.of(parameterTypes1).collect(Collectors.toSet());
                        for(int i = 0;i < args.length; i++){
                            collect.remove(args[i].getClass());
                        }
                        // 这里有个bug 在动态代理生成的args中会把int类型转化为Integer
//                        if(collect.isEmpty()
//                                ){
//                            return constructor.newInstance(args);
//                        }
                        return constructor.newInstance(args);
                    }
                }
                return null;
            }
            return null;
        }

        /**
         * 判断是否为Object的方法。
         *
         * @param method
         * @return
         */
        private boolean isObjectMethod(Method method) {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass == Object.class) {
                return true;
            }
            return false;
        }
    }
}
