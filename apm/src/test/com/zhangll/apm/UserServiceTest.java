package com.zhangll.apm;

import com.zhangll.apm.model.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
    @Test
    public void test(){
        //
        UserService userService = new UserService();
        userService.sayHello();
    }

    public static void main(String[] args) {
        //
        UserService userService = new UserService();
        userService.sayHello();
        Person person = userService.getPerson(1);
    }

}