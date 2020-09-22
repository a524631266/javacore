package com.zhangll.core.annotaions;

import org.junit.Test;

import javax.xml.bind.SchemaOutputResolver;
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

    @Test
    public void doSomeThing(){
        // 判断当前是否
        boolean annotationPresent = TestChildChildAnnotation.class.isAnnotationPresent(TestAnonotaion.class);
        boolean annotationPresent2 = TestChildAnonotation.class.isAnnotationPresent(TestAnonotaion.class);
        // false 为错误值
        System.out.println(annotationPresent);
        // true 为正常值，也就是会判断
        System.out.println(annotationPresent2);
    }
}
