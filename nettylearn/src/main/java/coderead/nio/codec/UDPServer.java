package coderead.nio.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import java.net.InetSocketAddress;

/**
 * udp 使用bootstrap加载
 * nc 192.168.1.68 8080 < /文件
 */
public class UDPServer {
    NioEventLoopGroup group;
    Bootstrap bootstrap;


    private void handlerReader(ChannelHandlerContext ctx, Object msg) {
        // 如何处理拆包和粘包的问题
        if (msg instanceof DatagramPacket) {
//            System.out.println(msg.toString());
            ((DatagramPacket) msg).retain();
            ByteBuf content = ((DatagramPacket) msg).content();


            System.out.println("start");
            int count = 0;
            while (content.isReadable()) {
                count += 1;
                byte b = content.readByte();
                System.out.println(b);
            }
            System.out.println("end count" + count);
            InetSocketAddress sender = ((DatagramPacket) msg).sender();

            ctx.channel().writeAndFlush(
                    new DatagramPacket(Unpooled.wrappedBuffer("has receive".getBytes()),
                            sender)
                    );
        }
        ctx.fireChannelRead(msg);

    }

    @Test
    public void test() throws InterruptedException, IOException {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);

//        NioDatagramChannel udpChannel = new NioDatagramChannel();
        ChannelFuture bind = bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.MAX_MESSAGES_PER_READ, 10)
                // 默认接收2048 2k的字节，要是想扩大就需要自定义一个固定长度的分配器
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
//                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .handler(new SimpleChannelInboundHandler<Object>() {
                    /**
                     * 在这里不需要传播， ctx.fireChannelRead(msg)
                     * 因为上层完成的channelRead来传播fireChannelRead
                     * 否则会发现引发io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1。
                     * 可以手动试试
                     *
                     * @param ctx
                     * @param msg
                     * @throws Exception
                     */
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                        handlerReader(ctx, msg);
                    }

                }).bind(new InetSocketAddress(8080));

        ChannelFuture channelFuture = bind.addListener((ch)->{
            System.out.println("bind success" + ch.get());
        });
        System.in.read();
//        bind.sync().channel().wait();
    }
}
