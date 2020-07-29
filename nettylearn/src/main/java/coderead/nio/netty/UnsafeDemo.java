package coderead.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;
import org.junit.Test;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class UnsafeDemo {
    public NioEventLoopGroup group = new NioEventLoopGroup(1);
    /**
     * 测试unsafe中异步的关系
     * 总体来说 只有注册时安全的，其他都是不安全的
     * 其他操作（bind，connect，write，read。。。）不安全是因为
     * unsafe会通过异步重新回调channel中相同方法，并通过异步方式
     * 检测assertEventLoop,来判断是否是
     *
     *
     *
     */
    @Test
    public void testRegisterError() throws IOException {
        DatagramChannel channel = new NioDatagramChannel();
//        group.register(channel);
        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                System.out.println("注册成功");
            }
        });
        // 注册是安全的！！！
        channel.unsafe().register(group.next(), channel.newPromise());
//        channel.unsafe().bind(new InetSocketAddress(8080), channel.newPromise());
        System.in.read();
    }

    /**
     * 如果注册太慢，就无法bind端口，因为unsafe是
     * @throws IOException
     */
    @Test
    public void testBindError() throws IOException {


        DatagramChannel channel = new NioDatagramChannel();
        // 会报java.lang.IllegalStateException: channel not registered to an event loop
//        group.register(channel);
        channel.unsafe().register(group.next(), channel.newPromise());
        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                System.out.println("注册成功");
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("激活成功");
            }
        });
//        java.lang.AssertionError
//        at io.netty.channel.AbstractChannel$AbstractUnsafe.assertEventLoop(AbstractChannel.java:383)
        channel.unsafe().bind(new InetSocketAddress(8080), channel.newPromise());
        System.in.read();
    }

    /**
     * 此时的bind是
     * @throws IOException
     */
    @Test
    public void testBindNormal() throws IOException, InterruptedException {
        DatagramChannel channel = new NioDatagramChannel();
        // 会报java.lang.IllegalStateException: channel not registered to an event loop
//        group.register(channel);
        channel.unsafe().register(group.next(), channel.newPromise());
//        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//            @Override
//            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//                System.out.println("注册成功");
//            }
//
//            @Override
//            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                System.out.println("激活成功");
//            }
//        });
        channel.pipeline().addLast(new LoggingHandler(){
            @Override
            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                System.out.println("注册成功");
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("激活成功");
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                System.out.println("激活不成功");
            }
        });
        channel.eventLoop().submit(new Runnable() {
            @Override
            public void run() {
                // 此时可以注册成功
                System.out.println("开始绑定");
                channel.unsafe().bind(new InetSocketAddress(8080), channel.newPromise());
            }
        });

//        TimeUnit.SECONDS.sleep(10);
        System.in.read();
    }

    /**
     * 利用unsafe异步处理bind write操作
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        DatagramChannel channel = new NioDatagramChannel();
        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
                System.out.println("!11111111111111");
//                super.exceptionCaught(ctx, cause);
            }
        });
        group.register(channel);
        // 不能绑定同一个
        channel.eventLoop().submit(new Runnable() {
            @Override
            public void run() {
                channel.unsafe().bind(new InetSocketAddress(8081), new DefaultChannelPromise(channel){
                    @Override
                    public boolean tryFailure(Throwable cause) {
                        return false;
                    }
                });
            }
        });
        channel.eventLoop().submit(new Runnable() {
            @Override
            public void run() {
                // 打印错误日志。 为什么不在pipeline中处理？
//                protected final void safeSetFailure(ChannelPromise promise, Throwable cause) {
                // 这里的!promise.tryFailure(cause)，表示在作io操作的时候传递了一个Promise
                // 如果这个promise触发了tryFailure，那么久会发出这个信息
//                    if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
//                        logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
//                    }
//                }
                // 方法1
//                channel.unsafe().write("1232", new DefaultChannelPromise(channel) {
//                    @Override
//                    public boolean tryFailure(Throwable cause) {
////                        cause.printStackTrace();
////                        return super.tryFailure(cause);
//                        return false;
//                    }
//                });
                channel.unsafe().write(new DatagramPacket(Unpooled.wrappedBuffer("123".getBytes()),
                            new InetSocketAddress("localhost",8080)
                ), new DefaultChannelPromise(channel) {
                    @Override
                    public boolean tryFailure(Throwable cause) {
                        cause.printStackTrace();
//                        return super.tryFailure(cause);
                        return false;
                    }
                });
                channel.unsafe().flush();
            }
        });
//        channel.unsafe().write("1232", new DefaultChannelPromise(channel) {
//            @Override
//            public boolean tryFailure(Throwable cause) {
//                cause.printStackTrace();
//                return super.tryFailure(cause);
////                return false;
//            }
//        });
        System.in.read();
    }

    /**
     * 开启一个UDP服务读的服务
     * @throws IOException
     */
    @Test
    public void openServer() throws IOException {
        DatagramChannel channel = new NioDatagramChannel();
        group.register(channel);
//        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////                super.channelRead(ctx, msg);
//                System.out.println("msg:" + msg);
//            }
//        });
        channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                System.out.println(msg);
            }
        });
        channel.unsafe().bind(new InetSocketAddress(8080), channel.newPromise());
        System.in.read();

    }

}
