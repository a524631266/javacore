package coderead.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用reactor模型进行tcp请求
 * telnet loaclhost 8888
 * 这个是最简单的模型
 */
public class HttpBossWorkerVersion3_Bootstrap {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        // 更加简单。已经帮你完成了注册的功能
        ServerBootstrap bootstrap = serverBootstrap.group(bossGroup, workerGroup)
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
                        // 解码,利用byteBuffer读取数据
                        System.out.println("初始化管道");
                        ch.pipeline().addLast("decode", new HttpRequestDecoder());

                        ch.pipeline().addLast("servlet", new MyServlet2());
                        // 必须要addFirst ， 理由是我们的业务写是在MyServlet2中的，写的话handler是从尾部向头部扫描handler的，所以需要加载servlet之前，用addFirst比较粗暴
                        ch.pipeline().addFirst("encode", new HttpResponseEncoder());
                    }
                });
        ChannelFuture bind = bootstrap.bind(9999);
        bind.addListener(future -> {
            System.out.println("注册成功" + future.get());
        });
        bind.sync().addListener(future -> {
            System.out.println("注册成功2" + future.get());
        });

    }

    private static class MyServlet extends SimpleChannelInboundHandler {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

            HttpRequest request = (HttpRequest) msg;
            System.out.println("当前客户端url： "+request.getUri());

            // 处理返回响应;
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
            httpResponse.content().writeBytes("hello ddd".getBytes());
            ChannelFuture channelFuture = ctx.writeAndFlush(httpResponse);
//            关闭方法1
//            channelFuture.addListener(future -> {
//                ctx.close();
//            });
            // 关闭方法2
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
    private static class MyServlet2 extends SimpleChannelInboundHandler {
        /**
         * HttpRequest -> HttpContent[...HttpContent...,LastHttpContent]
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 读写文件的
            if(msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                System.out.println("当前客户端url： "+request.getUri());
            }else if(msg instanceof HttpContent){
                // 开始接收流
                ByteBuf content = ((HttpContent) msg).content();
                System.out.println("start receive data: " + content.toString(Charset.defaultCharset()));
                // 目前输出数据到当前流中
                OutputStream out = new FileOutputStream("data/test.mp4",true
                );
                // ByteBuf有一个可以通过javaio接口直接读出数据
                content.readBytes(out, content.readableBytes());
                out.close();
            }
            // 当获取数据之后进行事件处理
            if(msg instanceof LastHttpContent){
                // 结束接收流
                // 处理返回响应;
                System.out.println("last context finished");
                FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
                httpResponse.content().writeBytes("success".getBytes());
                // 这里并不会立马触发写的操作。而是一个future操作，在其中是有触发selectionKey.OP_WRITE操作的
                ChannelFuture channelFuture = ctx.writeAndFlush(httpResponse);
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
