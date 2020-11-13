package com.zhangll.core.annotaions.functionalinterface;

import com.google.common.base.Function;
import com.rabbitmq.client.UnblockedCallback;
import org.apache.spark.api.java.function.Function3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

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

    public void printString2(Integer integer){
        String.valueOf(integer);
    }

    public String printString3(){
        return String.valueOf(12);
    }

    public String printString2pa(String a, Integer b){
        return String.valueOf(12);
    }

    public void printString2pa2(String a, Integer b){
        System.out.println("1123");
    }

    public void sayHello(MyFunction action){
        action.sayHello1();
    }
    public boolean predic(String a){
        return true;
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
        Function<Integer, String> a = FunctionMethodClass::a;

        // 第一个是类本身,第二个是类参数类(可以多个序列,从前到后)，第三个是返回类，如果没有类，那么为空
        BiFunction<FunctionMethodClass, Integer, String> printString = FunctionMethodClass::printString;
        BiConsumer<FunctionMethodClass, Integer> printString2 = FunctionMethodClass::printString2;
        Function<FunctionMethodClass, String> printString3 = FunctionMethodClass::printString3;
        Function3<FunctionMethodClass, String, Integer, String> printString2pa = FunctionMethodClass::printString2pa;

        BiFunction<FunctionMethodClass, String, Boolean> predic = FunctionMethodClass::predic;


    }

    private static String a(Integer integer) {
        return "ads";
    }
}
