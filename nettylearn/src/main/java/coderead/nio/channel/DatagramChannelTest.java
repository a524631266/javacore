package coderead.nio.channel;
/**
 * @Copyright 源码阅读网 http://coderead.cn
 */


import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP 管道通信
 * @author 鲁班大叔
 * @date 2020/7/1112:02 PM
 */
public class DatagramChannelTest {

    @Test
    public void test1() throws IOException {
        DatagramChannel channel=DatagramChannel.open();
        // 绑定端口
        channel.bind(new InetSocketAddress(8080));
        ByteBuffer buffer=ByteBuffer.allocate(8192);

        while (true){
            buffer.clear(); //  清空还原
            channel.receive(buffer); // 阻塞
            buffer.flip();
            byte[] bytes=new byte[buffer.remaining()];
            buffer.get(bytes);
            System.out.println(new String(bytes));
        }
    }
}
