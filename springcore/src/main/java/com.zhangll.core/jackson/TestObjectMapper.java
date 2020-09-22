package com.zhangll.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
// NON_NULL表示步不序列化为null的键值对
@JsonInclude(JsonInclude.Include.NON_NULL)
// order表示字符串的秩序
@JsonPropertyOrder({"age", "name"})
class User  {
    private final String name;
    private final int age;

    public User() {
        this("default", 0);
    }

    User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    //必须设置getName否者无法获取属性
    public String getName() {
        return name;
    }
    //必须设置getName否者无法获取属性
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

/**
 * 测试objectMapper是否是线程安全的
 */
public class TestObjectMapper {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        CountDownLatch countDownLatch = new CountDownLatch(100);

        ExecutorService executorService =
                newFixedThreadPool(100);
        for (int i = 1; i <= 100 ; i++) {
            int finalI = i;
            executorService.submit(()->{
                System.out.println(Thread.currentThread().getName() + ":::" + finalI);
                long random = Math.round(Math.random() * 100);
                User zhangll = null;
                if(random> finalI){
                    zhangll = new User("zhangll" + finalI, finalI);
                }else{
                    zhangll = new User(null, finalI);
                }
                try {
                    String s = mapper.writeValueAsString(zhangll);
                    Integer threadID = Integer.valueOf(Thread.currentThread().getName().split("-")[3]);
                    System.out.println(s);
                    User user = mapper.readValue(s, User.class);
                    boolean equals = threadID.equals(user.getAge());
                    if(equals){
                        countDownLatch.countDown();
                    }
                    // 当存在countDown100次会出现一个0值，所以线程安全的
                    if(countDownLatch.getCount() == 0){
                        System.out.println( Thread.currentThread().getName() + ":count::" + countDownLatch.getCount());
                    }
                } catch ( JsonProcessingException e ) {
                    e.printStackTrace();
                }
            });
        }

    }
}
