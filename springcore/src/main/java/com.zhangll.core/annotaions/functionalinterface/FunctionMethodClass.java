package com.zhangll.core.annotaions.functionalinterface;

import java.util.function.BiConsumer;

public class FunctionMethodClass<K,V> {
    private V value;

    public FunctionMethodClass() {
        value = null;
    }

    public FunctionMethodClass(V value) {
        this.value = value;
    }

    public String printString(Integer integer){
        return String.valueOf(integer);
    }

    public void sayHello(MyFunction action){
        action.sayHello1();
    }

    public K testKV(KeyValueFunction<K, V> kv) throws Exception {
        K key = kv.getKey(value);
        System.out.println("key: " + key);
        return key;
    }

    public static void say(){
        System.out.println("inner function");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        FunctionMethodClass functionMethodClass = new FunctionMethodClass();
        // 方法1
        MyFunction action = new MyFunction() {
            @Override
            public void sayHello1() {
                System.out.println("hidi");
            }
        };
        functionMethodClass.sayHello(action);

        // 方法2 lamda算法
        functionMethodClass.sayHello(()->{
            System.out.println("lamda");
        });

        // 方法3 静态方法
        functionMethodClass.sayHello(FunctionMethodClass::say);

        FunctionMethodClass<String, Integer> object = new FunctionMethodClass<>(1);
        KeyValueFunction<String, Integer> keyValueFunction = new KeyValueFunction<String, Integer>() {

            @Override
            public String getKey(Integer value) throws Exception {
                System.out.println("Value:" + value);
                return "asd";
            }
        };
        object.testKV(keyValueFunction);
        object.testKV(FunctionMethodClass::a);

    }

    private static String a(Integer integer) {
        return "ads";
    }
}
