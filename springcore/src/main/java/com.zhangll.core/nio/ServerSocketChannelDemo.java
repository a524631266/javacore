package com.zhangll.core.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ServerSocketChannelDemo {
    @Test
    public void testChannel() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8889));
        serverChannel.configureBlocking(false);
        Selector selector = Selector.open();
        // 第一步 注册 channel
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            // 2. 监听事件 / 刷新key
            System.out.println("开始 监听selectKey");
            int selectkey = selector.select();
            System.out.println("数量: "+selectkey);
            // 3. 选择 准备就绪的keys用来监听相关的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                int i = selectionKey.interestOps();
                System.out.println("channel:" + selectionKey.channel().getClass().getName());
                System.out.println("intersoOps" + i);
                // 筛选出所有可被接受的channel，这里之前已经定义了sschannel是可被接受的
                if(selectionKey.isAcceptable()){
                    // 可以明确知道是serversorckechannel
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                    // 非堵塞获取一个接受的客户端连接
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
//                    accept.configureBlocking(true);
                    // 只监听读请求吗
                    accept.register(selectionKey.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE );
                }else if(selectionKey.isWritable()){
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 4. 处理请求 同步处理请求
//                    handleWritable(socketChannel);
                    // 5. 异步处理请求
                    handleWritable(socketChannel);

                }else if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
//                    handleRead(socketChannel);
                    // 当socket关闭之后是不能再次使用的！
//                    socketChannel.close();
                    handleRead(socketChannel);
                    // 异步处理
//                    asynHandleRead(socketChannel);
                }
                // 5.取消keys
                iterator.remove();
            }
            System.out.println("###### selectionKeys size?" + selectionKeys.size());
            System.out.println("#### ?" + selector.selectedKeys().size());
        }
    }

    private void asynHandleRead(SocketChannel socketChannel) {
        Thread asd = new Thread(() -> {
            sleep(10000);
            // 延时2秒，之后又读操作，所以会再次触发select？
            handleRead(socketChannel);
        }, "asd");
        asd.start();
    }

    private void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void handleRead(SocketChannel socketChannel) {

        // 约定最多100字节，多出部分不搞

        ByteBuffer buffer = ByteBuffer.allocate(100);
        try {
            System.out.println("handleRead");
            socketChannel.read(buffer);
            buffer.flip();
            // 转码
            Charset charset = Charset.forName("UTF-8");
            CharBuffer decode = charset.decode(buffer);
//            socketChannel.write(buffer);
            if (decode.toString().trim().equals("exit")){
                // 退出，并注销数据
                socketChannel.close();
                System.out.println("close");
//                socketChannel.socket().close();
            }
            System.out.println("读到信息：" + decode.toString());
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }



    private void handleWritable(SocketChannel socketChannel) {
// 约定最多100字节，多出部分不搞
        ByteBuffer buffer = ByteBuffer.allocate(100);
        try {
            // 当channel是堵塞状态下，read是一直堵塞的
            socketChannel.read(buffer);
            buffer.flip();
            // 转码
            Charset charset = Charset.forName("UTF-8");
            CharBuffer decode = charset.decode(buffer);
            System.out.println("写出信息：" + decode.toString());
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }



}
