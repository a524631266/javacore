package coderead.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用reactor模型进行tcp请求
 * telnet loaclhost 8888
 * 这个是最简单的模型
 */
public class HttpBossWorkerVersion3_Bootstrap {
    public static void main(String[] args) throws IOException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        // 更加简单。已经帮你完成了注册的功能
        ChannelFuture bind = serverBootstrap.group(bossGroup, workerGroup)
                // 第一种方法直接读取数据
//                .childHandler(new SimpleChannelInboundHandler<Object>() {
//                    @Override
//                    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//                        System.out.println(msg);
//                    }
//                })
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 解码
                        ch.pipeline().addLast("decode", new HttpRequestDecoder());

                        ch.pipeline().addLast("servlet", new MyServlet());

//                ch.pipeline().addLast("encode", new HttpRequestEncoder());
                    }
                }).bind(9999);
        bind.addListener(future -> {
            System.out.println("注册成功");
        });

    }

    private static class MyServlet extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("当前客户端url： "+request.getUri());
        }
    }
}
