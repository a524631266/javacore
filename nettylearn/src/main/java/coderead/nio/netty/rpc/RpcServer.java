package coderead.nio.netty.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class RpcServer {
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
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Transfer msg) throws Exception {
            System.out.println("server receive client" + msg);
            ctx.fireChannelRead(msg);
        }
    }
}
