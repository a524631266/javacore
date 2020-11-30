package com.zhangll.aop.demo.demo1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/aop/http/alive")
    public String value(){
        return "一切正常";
    }
    @AuthChecker
    @RequestMapping("/aop/http/user_info")
    public String callSomeInterface() {
        return "调用了 user_info 接口.";
    }

    @AuthCheckerHandlerParameter
    @RequestMapping("/aop/http/user_info2")
    public String login(@RequestParam("name") String name, @RequestParam("pass") String pass){
        if(name.equals("abc")){
            throw  new IllegalArgumentException("name error");
        }
        return "登陆成功";
    }
}
