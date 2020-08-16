package coderead.nio.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import scala.Int;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Scanner;

/**
 * 实验结果表明，在udp上一个包只能传递一次，不会多个包分发
 */
public class UDPClient {
    NioEventLoopGroup group;
    Bootstrap bootstrap;
    @Test
    public void test() throws InterruptedException, IOException {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);

//        NioDatagramChannel udpChannel = new NioDatagramChannel();
        ChannelFuture bind = bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.MAX_MESSAGES_PER_READ, 10)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    class ReceiveHandler extends ChannelInboundHandlerAdapter {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println("ok,server has receive");
                        }
                    }

                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        System.out.println("ini");
                        ch.pipeline().addLast(new ReceiveHandler());
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("active");
                        ctx.channel().pipeline().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(generateString(3000).getBytes()),
                                new InetSocketAddress("localhost", 8080)));

                    }
                }).bind(new InetSocketAddress(8081)).sync();

        Scanner data = new Scanner(System.in);
        while (true){
            System.out.print("输入你要方法送的长度:");
            Integer readline = data.nextInt();
            bind.channel().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(generateString(Integer.valueOf(readline)).getBytes()),
                    new InetSocketAddress("192.168.1.86", 8080)),new DefaultChannelPromise(bind.sync().channel()){
                @Override
                public boolean tryFailure(Throwable cause) {
                    return false;
                }
            });
        }
//        bind.sync().channel().wait();
    }
    public static String generateString(int length){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //
            char[] chars = Character.toChars(RandomUtils.nextInt(26) + 65);
            sb.append(String.valueOf(chars));
        }
        System.out.println(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        generateString(10);
    }
}
