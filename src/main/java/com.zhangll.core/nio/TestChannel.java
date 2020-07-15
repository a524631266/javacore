package com.zhangll.core.nio;

import com.codahale.metrics.Timer;
import com.sun.nio.file.ExtendedOpenOption;
import com.zhangll.core.spark.metircs.MetricsDemo_5_Timer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public class TestChannel {
    private long time = 0;
    @Before
    public void init(){
        time = System.currentTimeMillis();
    }

    @After
    public void stop(){
        System.out.println("cost time: " + (System.currentTimeMillis() - time));

    }


    @Test
    public void testNoShareFileChannel1() throws IOException, InterruptedException {

        FileChannel open = FileChannel.open(Paths.get("data/file"),ExtendedOpenOption.NOSHARE_READ);
        TimeUnit.SECONDS.sleep(10);
    }
    @Test
    public void testNoShareFileChannel2() throws IOException, InterruptedException {

        FileChannel open = FileChannel.open(Paths.get("data/file"),ExtendedOpenOption.NOSHARE_READ);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 较为完整的文件复制
     * @throws IOException
     */
    @Test
    public void testFileCopyChannel() throws IOException {
        FileChannel source = FileChannel.open(Paths.get("data/file"),ExtendedOpenOption.NOSHARE_READ);
        FileChannel target = FileChannel.open(Paths.get("data/output"),StandardOpenOption.WRITE);

        ByteBuffer allocate = ByteBuffer.allocate(5);
        int count = 0;
        while(count < source.size()) {
            source.read(allocate,count);
            // 按下暂停键，重置postion以及limit和mark
            allocate.flip();
            count += allocate.limit();

            target.write(allocate);
            allocate.clear();
        }

        source.close();
        target.close();
    }

    /**
     *
     * 直接内存copy copy
     * @throws IOException
     */
    @Test
    public void testFileCopyChannel2() throws IOException {

        FileChannel source = new FileInputStream("data/file").getChannel();
        FileChannel target = new FileOutputStream("data/output2").getChannel();

        source.transferTo(0, source.size(), target);
        source.close();
        target.close();
    }


    @Test
    public void testCostTime() throws IOException, InterruptedException {
        MetricsDemo_5_Timer.startReport();
        for (int i = 0; i < 1000; i++) {
            Timer.Context time = MetricsDemo_5_Timer.responses.time();
            testFileCopyChannel();
            time.stop();
        }
        TimeUnit.SECONDS.sleep(5);

//        com.zhangll.core.spark.metircs.MetricsDemo_5_Timer.responses
//                count = 1000
//        mean rate = 122.75 calls/second
//        1-minute rate = 200.00 calls/second
//        5-minute rate = 200.00 calls/second
//        15-minute rate = 200.00 calls/second
//        min = 0.56 milliseconds
//                max = 161.24 milliseconds
//                mean = 3.83 milliseconds
//                stddev = 9.45 milliseconds
//                median = 2.08 milliseconds
//        75% <= 2.88 milliseconds
//        95% <= 8.72 milliseconds
//        98% <= 22.95 milliseconds
//        99% <= 55.05 milliseconds
//        99.9% <= 90.57 milliseconds
    }
    @Test
    public void testCostTime2() throws IOException, InterruptedException {
        MetricsDemo_5_Timer.startReport();
        for (int i = 0; i < 1000; i++) {
            Timer.Context time = MetricsDemo_5_Timer.responses.time();
            testFileCopyChannel2();
            time.stop();
        }
        TimeUnit.SECONDS.sleep(5);

//        com.zhangll.core.spark.metircs.MetricsDemo_5_Timer.responses
//                count = 1000
//        mean rate = 109.79 calls/second
//        1-minute rate = 200.00 calls/second
//        5-minute rate = 200.00 calls/second
//        15-minute rate = 200.00 calls/second
//        min = 0.86 milliseconds
//                max = 169.57 milliseconds
//                mean = 4.56 milliseconds
//                stddev = 9.03 milliseconds
//                median = 3.27 milliseconds
//        75% <= 4.23 milliseconds
//        95% <= 8.83 milliseconds
//        98% <= 13.92 milliseconds
//        99% <= 37.31 milliseconds
//        99.9% <= 134.77 milliseconds

    }

    @Test
    public void testSocketChannel() throws IOException {
        ServerSocketChannel open = ServerSocketChannel.open();

        SocketChannel open1 = SocketChannel.open();

    }

}
