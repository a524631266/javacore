package com.zhangll.core.annotaions.functionalinterface;
@FunctionalInterface
interface Functional{
    String goodbye(String arg);
}
interface FunctionNoAnn{
    String goodbye(String arg);
}

public class FunctionAnnotation {
    public String goodbye2(String arg){
        return "Goodb" + arg;
    }

    public static void main(String[] args) {
        FunctionAnnotation fa = new FunctionAnnotation();
        Functional f = fa::goodbye2;
        FunctionNoAnn f2 = fa::goodbye2;
        System.out.println(f.goodbye("12323"));

        System.out.println(f2.goodbye("2334"));


        Functional f1 = a -> {return  "123";};
        FunctionNoAnn f11 = a -> "3434";
        f1.goodbye("hhhh: ");
        f11.goodbye("hhhhhh:");
    }
}
