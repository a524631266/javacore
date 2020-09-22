package com.zhangll.core.bug;

/**
 * https://zhuanlan.zhihu.com/p/88555159
 * jit 编译优化/常量传播替换数据值，消除bug
 *  -Xint 关闭jit优化 jit有c1，c2，c3模式，可以作为参考资料好好看看
 * -XX:-TieredCompilation 关闭C1编译器
 * -XX:TieredStopAtLevel=3 停留在C阶段
 */
public class Hello {

    public void test(){
        int i = 8 ;
        while ((i -= 3) > 0);
        System.out.println("i  = " + i);
    }

    public static void main(String[] args) {
        Hello hello = new Hello();
        for (int i = 0; i < 50_000; i++) {
            hello.test();
        }
    }
}
