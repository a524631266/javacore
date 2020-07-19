package coderead.nio.echo;

import coderead.nio.util.ThreadUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HeartBeatEchoClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();


        channel.register(selector, SelectionKey.OP_CONNECT);

        boolean localhost = channel.connect(new InetSocketAddress("localhost", 9999));
        System.out.println("是否链接上?" + localhost);

        while (true) {
            int select = selector.select();
            System.out.println("elector.select()?");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                if(!next.isValid()){
                    continue;
                }
                // connect事件发生
                if(next.isConnectable()){
                    // 是否可链接状态
                    SocketChannel localChannel = (SocketChannel) next.channel();
                    // false
                    System.out.println(localChannel.isConnected());

                    // channa finish 之后 就代表已经链接上了
                    // 这一步非常重要,不写会死循环
                    localChannel.finishConnect();
                    // true
                    System.out.println(localChannel.isConnected());
//                    SocketChannel socketChannel = (SocketChannel)next.channel();
//                    socketChannel.configureBlocking(false);
                    // 切换为write 下句话意味这不停监听,就会不断轮询
//                    localChannel.register(next.selector(), SelectionKey.OP_WRITE);

                    // interestOps 表示给当前的key动态转换感兴趣的类型,就是给当前的文件句柄
                    //public SelectionKey nioInterestOps(int var1) {
                    //        if ((var1 & ~this.channel().validOps()) != 0) {
                    //            throw new IllegalArgumentException();
                    //        } else {
                    //            this.channel.translateAndSetInterestOps(var1, this);
                    //            this.interestOps = var1;
                    //            return this;
                    //        }
                    //    }
                    // 最终会调用这句化,对文件句柄重新设置值
                    // this.pollWrapper.setInterest(var3.getFDVal(), var2);
                    // 也会不停循环
                    next.interestOps(SelectionKey.OP_WRITE);
//                    socketChannel.register(next.selector(), SelectionKey.)
                }else if(next.isWritable()){
                    ByteBuffer allocate = ByteBuffer.allocate(64);
                    System.out.println("123123213");
                    SocketChannel localSocketChannel = (SocketChannel) next.channel();

                    localSocketChannel.write(ByteBuffer.wrap("heartbeat".getBytes()));

//                    next.interestOps(SelectionKey.)
                    ThreadUtil.sleep(2);
                    next.interestOps(SelectionKey.OP_READ);

                } else if(next.isReadable()){

                }


            }
        }

    }
}
