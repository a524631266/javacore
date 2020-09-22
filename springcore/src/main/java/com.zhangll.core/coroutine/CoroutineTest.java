package com.zhangll.core.coroutine;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * https://zhuanlan.zhihu.com/p/27590299
 * 协程的使用。一般应用于io密集型的场景。这个无话可说，因为在计算密集型的任务
 * 中并没有多大优势
 */
public class CoroutineTest {
    public static void main(String[] args) {
        FiberTask hello = new FiberTask("hello");

        FiberTask world = new FiberTask("world");

        hello.start();
        world.start();
        try {
            hello.join();
            world.join();
        }catch ( ExecutionException e ){
            e.printStackTrace();
        }catch ( InterruptedException e ){
            e.printStackTrace();
        }
    }

    static class FiberTask extends Fiber<Integer>{
        private String msg;
        public FiberTask(String msg){
            this.msg = msg;
        }

        @Override
        protected Integer run() throws SuspendExecution, InterruptedException {
            for (int i = 0; i < 5; i++) {
                System.out.println(msg);
                // 使用Park等待,传递时间 为等待相应的时间
                Fiber.park(1000, TimeUnit.MILLISECONDS);
            }

            // 使用uppark恢复
//            this.unpark();
            return 0;
        }
    }
}
