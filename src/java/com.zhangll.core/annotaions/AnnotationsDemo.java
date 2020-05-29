package com.zhangll.core.annotaions;

import java.lang.annotation.Annotation;

@TestAnonotaion
class TestAnnotation{

}
public class AnnotationsDemo {
    public static void main(String[] args) {
        Annotation[] annotations = TestAnnotation.class.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            String name = aClass.getName();
        }
    }

    public static void doSomeThing(String[] args){

    }
}
