package coderead.nio.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import scala.Int;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

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
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        System.out.println("start init ");
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("active");
//                        ctx.channel()
                        ctx.channel().pipeline().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(generateString(3000).getBytes()),
                                new InetSocketAddress("localhost", 8080)));

                    }
                }).bind(new InetSocketAddress(8081));

        bind.sync().channel().writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(generateString(3000*10 - 1).getBytes()),
                new InetSocketAddress("192.168.1.86", 8080)),new DefaultChannelPromise(bind.sync().channel()){
            @Override
            public boolean tryFailure(Throwable cause) {
                return false;
            }
        });

        System.in.read();
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
