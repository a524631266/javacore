package coderead.nio.echo;

import coderead.nio.util.BufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * telnet localhost 9999 测试
 */
public class HeartBeatEchoServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 堵塞当中
            int select = selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            System.out.println("poll?");
            while (iterator.hasNext()){
                SelectionKey nextKey = iterator.next();

                iterator.remove();
                System.out.println("new selector");
                if(nextKey.isValid()){
                    // 建立完链接,tcp四次握手已经完全
                    if(nextKey.isAcceptable()){
                        ServerSocketChannel socketChannel = (ServerSocketChannel) nextKey.channel();
                        SocketChannel accept = socketChannel.accept();
                        accept.configureBlocking(false);
                        // 一旦建立链接就里面触发写的状态!!这句话如何理解?
                        accept.register(nextKey.selector(), SelectionKey.OP_READ);

                    }else  if (nextKey.isReadable()){
                        // 处理读入的数据
                        // 目的是什么呢?心跳机制的重要目的是保证服务可达
                        // 并返回协议要求的格式 ,
                        // 我们的心跳机制是返回给客户端一个当前的时候
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        // heartbeat 共 64字节

                        SocketChannel channel = (SocketChannel)nextKey.channel();

                        // 读取数据并显示
                        channel.read(buffer);
                        // 必须复位.默认情况下 limit 与 capacity 是一致的,除非flip成功!!
                        buffer.flip();
                        System.out.println(buffer.limit());
                        // 如果有保留并且ascii码为4 EOF  ********
                        if(buffer.hasRemaining() && buffer.get(0) == 4){
                            channel.close();
                            System.out.println("关闭管道"+ channel);
                            break;
                        }

                        buffer.flip();
                        System.out.println("server receive message : " + BufferUtil.byte2string(buffer));

                        // 会写响应时间
                        buffer.clear();
                        buffer.put(String.valueOf(System.currentTimeMillis()).getBytes());
//                        buffer.rewind();
                        buffer.flip();
                        channel.write(buffer);
                    }


                }else{
                    // 这一步是做什么?
                    continue;
                }

            }

        }




    }
}
