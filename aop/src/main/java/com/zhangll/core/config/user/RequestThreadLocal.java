package com.zhangll.core.config.user;

import java.util.Optional;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/27
 */
public class RequestThreadLocal {
    private static ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    public static void set(RequestContext requestContext) {
        threadLocal.set(requestContext);
    }

    public static RequestContext get() {
        return threadLocal.get();
    }

    public static RequestContext safeGet() {
        return Optional.ofNullable(threadLocal.get()).orElseGet(RequestTrace::buildLite);
    }

    public static void clear() {
        threadLocal.remove();
    }
}
