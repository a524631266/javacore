package com.zhangll.core.jpaproxy;

import com.sun.xml.internal.bind.api.ClassResolver;
import com.zhangll.core.jpaproxy.interf.Base;
import com.zhangll.core.jpaproxy.interf.Child;
import com.zhangll.core.jpaproxy.interf.ProxyObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

class MainProxy {
    // 模拟扫描一个类，获取所有实现类
    //
    private Set<Class<?>> set = new HashSet();

    public Object proxyClass(Class<?> tClass){
        // 模拟生成一个代理
        // 通过类所处的加载器，这里可能会不存在吗？暂时不写
        // tClass 是获取这个接口
        // 利用接口获取实现类
        Class<?> transClass = findOneClass(tClass);

        Object object = Proxy.newProxyInstance(tClass.getClassLoader(), transClass.getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        method.invoke(transClass.newInstance(), args);
                        return null;
                    }
                });
        return object;
    }

    private Class<?> findOneClass(Class<?> tClass) {
        for (Class storeObjectClass : set) {
            boolean assignableFrom = tClass.isAssignableFrom(storeObjectClass);

            if (assignableFrom){
                return storeObjectClass;
            }
            Class<?>[] interfaces = tClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                Class<?> oneClass = findOneClass(anInterface);
                if(oneClass!=null) {
                    return oneClass;
                }
            }
        }
        return null;
    }

    public void setSet(Class<?> transClass) {
        this.set.add(transClass);
    }

    public static void main(String[] args) throws ClassNotFoundException {

        MainProxy baseMainProxy = new MainProxy();
        // 设置扫描所有可用的类
//       1. 这里直接使用路径 假设通过spring获取到了扫描的地址
        String path = "com.zhangll.core.jpaproxy.interf.ProxyObject";
        Class<?> aClass = Class.forName(path);
        baseMainProxy.setSet(aClass);
        //2. 根据interface获取数据
        Base base = (Base) baseMainProxy.proxyClass(Child.class);
        base.findById();
    }
}
