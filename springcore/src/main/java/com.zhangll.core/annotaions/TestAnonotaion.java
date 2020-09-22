package com.zhangll.core.annotaions;

import java.lang.annotation.*;

/**
 * RUNTIME 运行时可以获取的注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnonotaion {
}
