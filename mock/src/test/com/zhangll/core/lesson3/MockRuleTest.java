package com.zhangll.core.lesson3;


import com.zhangll.core.common.Account;
import com.zhangll.core.common.UserDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.MockitoJUnit;

/**
 * 主要是集成多种runners 的方式
 *
 */
public class MockRuleTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public UserDao userDao;
    @Test
    public void testMock(){
//        UserDao mock = Mockito.mock(UserDao.class);

        Account account = userDao.findAccount("x", "x");
        System.out.println(account);
    }

}
