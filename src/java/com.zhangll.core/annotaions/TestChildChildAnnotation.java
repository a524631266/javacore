package com.zhangll.core.annotaions;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestChildAnonotation
public @interface TestChildChildAnnotation {
}
