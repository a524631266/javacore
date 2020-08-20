package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.service.UserService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class RpcServer {
    public ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * 以map 存储bean对象
     * 设计的标准是
     * 1.Map中存有指定对象的bean
     * 2. 保证同步
     * @param port
     * @throws InterruptedException
     */
    public static ConcurrentMap<String, Object> beanMap = new ConcurrentHashMap<>();

    public void openServer(int port) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup workers = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bind = bootstrap.group(boss, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast("codec", new RpcCodec());
                        ch.pipeline().addLast("serverHandler", new ServerHandler());
                    }
                }).bind( port);
        bind.addListener(future -> {
            if(future.cause() != null){
                System.out.println("error");
            }else{
                System.out.println("bind success");
            }
        });
        bind.sync().channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        RpcServer rpcServer = new RpcServer();
        rpcServer.openServer(8080);
    }

    private class ServerHandler extends SimpleChannelInboundHandler<Transfer> {
        /**
         * 重要做用是返回数据
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Transfer msg) throws Exception {
            System.out.println("server receive client" + msg);
            Transfer result = new Transfer(msg.idCode, msg.isHeartbeat, false);
//            ctx.fireChannelRead(msg);
            if(msg.target instanceof Request && !msg.isHeartbeat){
                executor.submit(()->{
                    Channel channel = ctx.channel();
                    String token = ((Request) msg.target).getToken();
                    Object aClass = beanMap.get(((Request) msg.target).getToken());

//                    o.getClass().getMethod(msg.target.)
//                    Response response = new Response();
//                    result.updateTarget(response);
                    channel.writeAndFlush(result);
                });
            } else {
//                Response response = new Response();
//                result.updateTarget(response);
                ctx.channel().writeAndFlush(result);
            }
        }
    }

    static void loadClass(){
        beanMap.put(UserService.class.getName(), UserService.class);
    }


}
