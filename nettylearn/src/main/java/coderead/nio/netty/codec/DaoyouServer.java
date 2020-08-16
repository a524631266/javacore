package coderead.nio.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DaoyouServer {
    private ServerBootstrap bootstrap;

    @Before
    public void init(){
        bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioServerSocketChannel.class);
    }

    @Test
    public void test(){
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
//                ch.pipeline().addLast(new DaoyouProtocal());
                ch.pipeline().addLast(new TrackHandler());
                ch.pipeline().addLast(new DaoyouProtocalOp());
                ch.pipeline().addLast(new TrackMessageHandler());
            }
        });
    }
    @After
    public void start() throws InterruptedException {
        System.out.println("start");
        bootstrap.bind(8080).sync().channel().closeFuture().sync();
    }

    private class TrackHandler extends SimpleChannelInboundHandler<String> {
        // 并不存在并发问题，因为是在同一个channel中同一个线程逐步进行的。
        private int count = 0;
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println(count++ + "::" + msg);
        }
    }

    private class TrackMessageHandler extends SimpleChannelInboundHandler<Message> {
        private int count = 0;
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
            System.out.println(count++ + "::" + msg);
        }
    }
}
