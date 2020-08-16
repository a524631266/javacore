package com.zhangll.core.concurrent.reference;

import org.junit.Test;
import sun.misc.Queue;

import java.lang.ref.*;

/**
 * gc root tracing 算法，显示单条路径选择最小的那截路径作为最终对象的强/弱划分
 * 多条路径中，选择最强的作为强弱标记。并行取最长
 * https;//blog.csdn.net/firebolt100/article/details/82660333
 * https://zhuanlan.zhihu.com/p/29522201 finalizefng方法
 * https;//blog.csdn.net/qq541005640/article/details/81987435 检测jvm堆内内存与堆外内存使用
 * http://gee.cs.oswego.edu/dl/papers/aqs.pdf g关于synchronizer框架论文
 */
public class FiveReferenceDemo {
    @Test
    public void strongReference(){
        // strong Reference
        // frd 所指向的地址是强类型的
        FiveReferenceDemo frd = new FiveReferenceDemo();
    }
    private String status = "Hi I am active";

    /**
     * -Xms10m -Xmx10m -XX:+PrintGCDetails
     * @throws InterruptedException
     */
    @Test
    public void softReference() throws InterruptedException {
        SoftReference<FiveReferenceDemo> soft = new SoftReference<>(new FiveReferenceDemo());
        System.gc();
        Thread.sleep(1 * 1000);
        FiveReferenceDemo fiveReferenceDemo = soft.get();
        System.out.println("Soft reference :: " + soft.get());
        System.out.println("Soft reference :: " + soft.get());
        assert  fiveReferenceDemo != null;
        while (soft.get() != null) {
//            System.gc();
            Thread.sleep(1 * 1000);
            byte[] b = new byte[1024 * 1024 * 5];
            System.out.println("Soft reference :: " + soft.get());
        }
    }


    @Test
    public void weakReference() throws InterruptedException {
        WeakReference<FiveReferenceDemo> soft = new WeakReference<FiveReferenceDemo>(new FiveReferenceDemo());
        System.gc();
        Thread.sleep(1 * 1000);
        FiveReferenceDemo fiveReferenceDemo = soft.get();
        System.out.println("Soft reference :: " + soft.get());
        System.out.println("Soft reference :: " + soft.get());
        assert  fiveReferenceDemo == null;

    }

    /**
     * 虚引用，通知作用
     * 这里模拟两个Phantom类型的reference对象
     * @throws InterruptedException
     */
    @Test
    public void phantomReference() throws InterruptedException {
        ReferenceQueue<FiveReferenceDemo> queue = new ReferenceQueue();
        PhantomReference<FiveReferenceDemo> reference = new PhantomReference<>(new FiveReferenceDemo(), queue);
        PhantomReference<FiveReferenceDemo> reference2 = new PhantomReference<>(new FiveReferenceDemo(), queue);
        System.gc();
        //
//        Thread.sleep(100);
        // 不管gc与否，phantomReference.get总是返回null，这个是虚引用的精髓。
        FiveReferenceDemo fiveReferenceDemo = reference.get();
        FiveReferenceDemo fiveReferenceDemo2 = reference2.get();
        assert fiveReferenceDemo == null;
        assert fiveReferenceDemo2 == null;
        Reference<FiveReferenceDemo> poll = (Reference<FiveReferenceDemo>) queue.poll();
        System.out.println("first: "+poll);

        Reference<FiveReferenceDemo> poll2 = (Reference<FiveReferenceDemo>) queue.poll();
        System.out.println("second: " + poll2);
        FiveReferenceDemo fiveReferenceDemo1 = poll.get();
        System.out.println(fiveReferenceDemo1);

    }

    /**
     * 查看 堵塞的其中一种实现 wait notify
     * @throws InterruptedException
     */
    @Test
    public void queue() throws InterruptedException {
        Queue<Object> objectQueue = new Queue<>();
        objectQueue.enqueue(new Object());

        Object a = null;
        while ((a = objectQueue.dequeue())!= null){
            System.out.println("1111" + a);
        }
    }
}
