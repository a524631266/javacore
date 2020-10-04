package com.zhangll.core.lesson3;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DeepMockTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public Lession03Service lession03Service;

//    @Mock
//    public Lesson03 lesson03;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void test(){
//        Lesson03 lesson0 = new Lesson03();
//        when(lession03Service.get()).thenReturn(lesson03);
//        Lesson03 lesson031 = lession03Service.get();
//        lesson03.foo();
//
//        assertThat(lesson031 , equalTo(lesson03));
//    }

    @Test
    public void testDeepMock(){
        lession03Service.get().foo();
    }
}
