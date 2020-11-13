/**
 * how to mock by Mockito
 * 1. use runner
 *  @RunWith(MockitoJUnitRunner.class)
 * 2. use annotation方式
 *      @Before
 *     public void init(){
 *         MockitoAnnotations.initMocks(this);
 *     }
 *
 *     @Mock 默认方式
 *     @Mock(answer = Answers.RETURNS_SMART_NULLS)
 *  3. rule方式
 *      @Rule
 *     public MockitoRule rule = MockitoJUnit.rule();
 *
 *     @Mock
 *     public UserDao userDao;
 */
package com.zhangll.core.lesson3;