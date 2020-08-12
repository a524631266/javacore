package com.zhangll.core.nio;

import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TestBuffer {

    public static void main(String[] args) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        System.out.println(byteBuffer.isDirect());
        IntBuffer allocate = IntBuffer.allocate(100);
        Boolean boo = true;
        System.out.println(boo);
//        allocate.put(boo);
        System.out.println(allocate.isDirect());

    }


    /**
     * 直接内存释放堆外内存，系统调用
     */
    @Test
    public void testClean(){
        DirectBuffer buffer = (DirectBuffer) ByteBuffer.allocateDirect(1);
        buffer.cleaner().clean();
    }
}
