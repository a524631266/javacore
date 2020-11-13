package com.zhangll.core.lesson3;

import com.zhangll.core.common.Account;
import com.zhangll.core.common.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.runners.*;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UseRunnerTest {


    @Test
    public void testMock(){

        UserDao userDao = mock(UserDao.class);
        // 并不会抛异常,因为mock对象会覆盖方法
        Account account = userDao.findAccount("x", "x");
        // 默认返回null
        assertThat(account, equalTo(null));

    }

    @Test
    public void testStub(){

        UserDao userDao = mock(UserDao.class, RETURNS_SMART_NULLS);
        // 并不会抛异常,因为mock对象会覆盖方法
        Account account = userDao.findAccount("x", "x");
        // smart nulls 默认返回一个字符串
        System.out.println(account);
        assertThat(account, equalTo(null));

    }

}
