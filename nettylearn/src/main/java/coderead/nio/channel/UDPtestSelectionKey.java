package coderead.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * nc -vu 127.0.0.0 8888
 */
public class UDPtestSelectionKey {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel=ServerSocketChannel.open();
        // 绑定端口
        channel.bind(new InetSocketAddress(8080));
        channel.configureBlocking(false);
        Selector selector = Selector.open();

        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            System.out.println("死循环？？？");
            while (iterator.hasNext()){
                SelectionKey readyKey = iterator.next();
                iterator.remove();
                if (!readyKey.isValid()) {
                    continue;
                }
                if(readyKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)readyKey.channel();
                    // 非堵塞获取一个接受的客户端连接
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
//                    accept.configureBlocking(true);
                    // 只监听读请求吗
                    accept.register(readyKey.selector(), SelectionKey.OP_READ);
                }else if(readyKey.isReadable()){
                    System.out.println("死循环吗");
                    SocketChannel sc = (SocketChannel) readyKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(63);
//                    sc.read(buffer);
                    // 如果只涉及到write的话也是会死循环的
                    sc.write(ByteBuffer.wrap("hel".getBytes()));
                }

            }
        }
    }
}
