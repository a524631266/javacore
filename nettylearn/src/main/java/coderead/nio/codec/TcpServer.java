package coderead.nio.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * 简单的FixFrameLen
 */
public class TcpServer {
    @Test
    public void openServer() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup workers = new NioEventLoopGroup(8);

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bind = bootstrap.group(boss, workers)
                .channel(NioServerSocketChannel.class)
                // 初始化的
//                .handler(new ChannelInitializer<NioServerSocketChannel>() {
//                    @Override
//                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
//                        // 测试长度5 个字符，每5个字符就会向下导出并保存数据
//                        ch.pipeline().addLast("decoder", new FixedLengthFrameDecoder(5));
//                        ch.pipeline().addLast("myhandler", new MyClientHandler());
//                    }
//                })
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 测试长度5 个字符，每5个字符就会向下导出并保存数据
                        ch.pipeline().addLast("decoder", new FixedLengthFrameDecoder(5));
                        ch.pipeline().addLast("myhandler", new MyClientHandler());
                    }
                }).bind(8080).sync();
        bind.channel().closeFuture().sync();

    }

    private class MyClientHandler extends SimpleChannelInboundHandler<Object> {
        private int count = 0;
        // 默认是bytebuffer？
        // head出来的是bytebuffer？
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof ByteBuf) {
                // 粘包和半包的问题
                System.out.println(String.format("消息%s：%s",++count,((ByteBuf) msg).toString(Charset.defaultCharset())));
            }
        }
    }

}
