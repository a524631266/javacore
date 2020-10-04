package com.zhangll.core.quickstart;

import javax.servlet.http.HttpServletRequest;

public class UserLoginController {

    public final UserDao userDao;

    public UserLoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    public String login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Account account = userDao.findAccount(username, password);

            if(account!=null){
                return "/index";
            }else{
                return "/login";
            }
        }catch (UnsupportedOperationException e){

            return "/505";
        }

    }

}
