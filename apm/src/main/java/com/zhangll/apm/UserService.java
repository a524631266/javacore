package com.zhangll.apm;

import com.zhangll.apm.model.Person;

import java.util.concurrent.TimeUnit;

public class UserService {
    public void sayHello(){
        System.out.println("hello");
    }

    public void sayHi(){
        System.out.println("Hiiiiiii");
    }

    public Person getPerson(int id){
        int a = 1/0;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Person(id, "abd");
    }
    public void getPerson(){
        System.out.println("12323233");
    }
}
