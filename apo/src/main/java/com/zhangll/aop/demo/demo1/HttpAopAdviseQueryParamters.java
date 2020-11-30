package com.zhangll.aop.demo.demo1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * http://localhost:8080/aop/http/user_info2?name=zhangll&pass=123456
 * 1. around  2- Before 3- AfterReturning 4- After
 * 2. error 路线 1. around 2. before 3. deAfterThrowing 4. doAfter
 * 默认情况下，Component实例是单例 kind == method-execution==》joinPoint.getTarget目标为所执行的方法 class(非实例)
 */
@Component
@Aspect
public class HttpAopAdviseQueryParamters {
    private Logger logger = LoggerFactory.getLogger(getClass());


    // 定义一个 Pointcut, 使用 切点表达式函数 来描述对哪些 Join point 使用 advise.
    @Pointcut("@annotation(com.zhangll.aop.demo.demo1.AuthCheckerHandlerParameter)")
    public void pointcut() {
    }

    // 定义 advise
    @Around("pointcut()")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getArgs());
        // joinPoint 的类型有哪些？
        String kind = joinPoint.getKind();Object target = joinPoint.getTarget();
        System.out.println("around!!!");
        return joinPoint.proceed();
    }

    @Before("pointcut()")
    public void beforeInvoke(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        System.out.println("doBefore");
//        return joinPoint.proceed();
    }

    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        System.out.println("doAfter");
    }

    /**
     *
     * @param joinPoint
     * @param retValu1  方法调用返回的结果，可以通过处理结果来应用程序
     */
    @AfterReturning(value = "pointcut()", returning = "retValu1")
    public void doAfterReturning(JoinPoint joinPoint, Object retValu1){
        Object[] args = joinPoint.getArgs();
        System.out.println(args);
        System.out.println("doAfterReturning");
    }

    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void deAfterThrowing(JoinPoint joinPoint, Exception exception){
        System.out.println("deAfterThrowing");
        System.out.println(exception.getMessage());
    }

}
