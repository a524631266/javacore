package coderead.nio.netty.websocket;

import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * http
 *
 *
 */
public class DanmuServer {
    private ByteBuf indexPage;
    // channelGroup会自动删除已经关闭的websocket链接
    private ChannelGroup channelGroup;
    private Thread schedule = new Thread(()->{
        while (true){
            try {
                TimeUnit.SECONDS.sleep(1);
                if(channelGroup!=null){
                    channelGroup.writeAndFlush(wrapMessage("hello"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    public void openServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup workers = new NioEventLoopGroup(8);
        Channel channel = bootstrap.group(boss,workers )
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // http请求的解码器
                        ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                        // 合并head & body
                        ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));


                        ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                        // 自己 的http协议，用来拦截是否要做websoket还是返回结果
                        ch.pipeline().addLast("my-http-handler", new MyHttpHandler());
                        // websorket 协议处理器
                        /**
                         * must /ws
                         * @see WebSocketServerProtocolHandshakeHandler#isNotWebSocketPath(FullHttpRequest)
                         * @throws InterruptedException
                         */
                        ch.pipeline().addLast("wc-encoder",
                                new WebSocketServerProtocolHandler("/ws"));
                        ch.pipeline().addLast("my-wc-handler", new MyWcHandler());

                    }
                }).bind(8080).sync().channel();
        // 设置 channelGroup

        channelGroup = new DefaultChannelGroup(new NioEventLoopGroup(1).next());
        channel.closeFuture().sync();

    }

    private class MyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        /**
         * channelRead0 是异步处理的，所以如果在该方法中传递消息给下个handler的时候需要retain
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            if (msg.uri().equals("/")) {
                String uri = msg.uri();
                System.out.println("uri2:" + uri);
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                // 设置内容类型和长度
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, indexPage.capacity());
                // 获取数据
                response.content().writeBytes(indexPage.duplicate());

                ctx.writeAndFlush(response);
                // 192.168.1.76/ws
            } else if (msg.uri().contains("/ws")) {
                // 如果仅仅只是传递数据那么
                ctx.fireChannelRead(msg.retain());
            }else{
                String uri = msg.uri();
                System.out.println("uri:" + uri);

            }
        }
    }


    private void initIndexPage() throws IOException {
        // file:/home/zhangll/github/javacore/nettylearn/target/scala-2.11/classes/
        URL location = DanmuServer.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(location);
        String classPath = location.getPath().replace("file:/", "");

        RandomAccessFile randomAccessFile = new RandomAccessFile(classPath + "Index.html", "r");

        ByteBuffer buffer = ByteBuffer.allocateDirect((int) randomAccessFile.length());
        randomAccessFile.getChannel().read(buffer);
        buffer.flip(); // 必须flip
        indexPage = Unpooled.wrappedBuffer(buffer);

    }
    private class MyWcHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
            System.out.println(msg);
            if(msg instanceof TextWebSocketFrame){
                String text = ((TextWebSocketFrame) msg).text();
                // 在建立ws的时候，只要建立这个就行
                if(text.equals("add")){
                    channelGroup.add(ctx.channel());
                    System.out.println("insert " + ctx.channel());
                }else{
                    channelGroup.writeAndFlush(msg.retain());
//                    channelGroup.writeAndFlush(text);
                }
            }

        }

        /**
         * 最好不要在这里加channelGroup.add(ctx.channel());
         * 因为http是短时链接，发起一次http，就会创建一个channel
         * 而 发起请求的时候不仅有原请求，还有http://localhost:8080/favicon.ico请求
         * @param ctx
         * @throws Exception
         */
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channel" + ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channel incative");
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("close" + ctx.channel());
//            if(channelGroup.contains(ctx.channel())){
//            channelGroup.remove(ctx.channel());
//            WebSocketFrame wsf = new TextWebSocketFrame("someone close");
//            channelGroup.writeAndFlush(wsf);
//            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        DanmuServer danmuServer = new DanmuServer();

        danmuServer.initIndexPage();
        danmuServer.cycle();
        danmuServer.openServer();
    }

    private void cycle() {
        schedule.setDaemon(true);
        schedule.start();
    }

    private WebSocketFrame wrapMessage(String msg){
        return new TextWebSocketFrame(msg);
    }
}
