package com.zhangll.core.annotaions.functionalinterface;

public class FunctionMethodClass {
    public void sayHello(MyFunction action){
        action.sayHello1();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        FunctionMethodClass functionMethodClass = new FunctionMethodClass();
        MyFunction action = new MyFunction() {
            @Override
            public void sayHello1() {
                System.out.println("hiiasdfdwi");
            }
        };
        functionMethodClass.sayHello(action);
    }
}
