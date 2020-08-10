package coderead.nio.codec;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TcpClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap
                .group(new NioEventLoopGroup(1)).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new MyProtocal());
                    }
                }).connect("192.168.1.86", 8080);

        Channel channel = connect.sync().channel();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("call to server:  ");
            String line = reader.readLine();
            // 写的是string类型的数据，所以在writeAndFlush
            channel.writeAndFlush(line);
        }
//        channel.closeFuture().sync();
    }

    /**
     * String类型代表一个编码格式，并不是只解码格式
     * 解码统一以bytebuf方式处理
     */
    public static class MyProtocal extends ByteToMessageCodec<String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {

        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        }
    }
}
