package com.zhangll.core.nio;

import scala.Char;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 *nc -vu 127.０．０．０　８８８８
 */
public class UDPChannelDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        DatagramChannel udp = DatagramChannel.open();
        udp.bind(new InetSocketAddress(8888));
//        SelectionKey.OP_READ | SelectionKey.OP_WRITE
        System.out.println(udp.validOps());
//        udp.setOption()
        ByteBuffer allocate = ByteBuffer.allocate(100);

        while (true){
            // 清空数据
            allocate.clear();
            udp.receive(allocate);

            allocate.flip();

            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer charBuffer = decoder.decode(allocate);
//            CharBuffer  charBuffer = decoder.decode(allocate.asReadOnlyBuffer());
            System.out.println(charBuffer);
        }

    }
}
