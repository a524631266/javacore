package com.zhangll.apm.collector;

import com.zhangll.apm.UserService;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerCollectorTest {

    @Test
    public void buildClass() throws NotFoundException, CannotCompileException {

        HttpServerCollector httpServerCollector =new HttpServerCollector(null);
        CtClass ctClass = httpServerCollector.buildClass(HttpServerCollector.class.getClassLoader(),
                "com.zhangll.apm.UserService");
        ctClass.toClass();

        UserService userService = new UserService();
        userService.sayHi();
        userService.sayHello();

    }
}