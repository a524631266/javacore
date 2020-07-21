package coderead.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用reactor模型进行tcp请求
 * telnet loaclhost 8888
 * 这个是最简单的模型
 */
public class HttpBossWorkerVersion1 {
    public static void main(String[] args) throws IOException {
        // 创建一个tcp
        // eventLoop是对selector的封装
        NioEventLoopGroup reactor = new NioEventLoopGroup(1);
        // NioServerSocketChannel 是 ServerSocketChannel的封装
        NioServerSocketChannel channel = new NioServerSocketChannel();

        // 一旦注册成功就会堵塞
        reactor.register(channel);
        // 在bind是异步的，这个bind事件是传送给reactor中的taskQueue的runnable对象
        ChannelFuture bind = channel.bind(new InetSocketAddress(8888));

        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                super.channelRead(ctx, msg);
                System.out.println(msg.getClass());
                System.out.println("建立链接");

                // 注册到selector中，但是selector被 eventloop封装
                NioSocketChannel sc = (NioSocketChannel) msg;
                reactor.register(sc);
                sc.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("获取到什么东西" + msg.getClass());
                        // 获取到的是ByteBuf 类似于 ByteBuffer，不过也是封装
                        // 可以看到封装的关系
                        // NioEventLoop 是 Selector 的封装
                        // NioServerSocketChannel 是 ServerSocketChannel的封装
                        // ByteBuf 是对 ByteBuffer的封装
                        System.out.println(msg.toString(Charset.defaultCharset()).trim());
                        if("close".equals(msg.toString(Charset.defaultCharset()).trim())){
                            ctx.close();
                        }

                    }
                });

            }
        });
        bind.addListener((ad)->{
            System.out.println("bind success" + ad);
        });
    }
}
