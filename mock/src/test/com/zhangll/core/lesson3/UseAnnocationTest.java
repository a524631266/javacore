package com.zhangll.core.lesson3;

import com.zhangll.core.common.Account;
import com.zhangll.core.common.UserDao;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UseAnnocationTest {

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

//    @Mock
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private UserDao userDao;

    @Test
    public void testMock() {
        Account account = userDao.findAccount("x", "x");
        System.out.println(account);
    }
}
