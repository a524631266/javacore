package coderead.nio.netty.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 发送消息
 */
public class RPCClient {

    private Channel channel = null;

    public void connect(String host, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup(1))
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast("codec", new RpcCodec());
                        ch.pipeline().addLast("request", new ClientHandle());
                    }
                }).connect(host, port);
        channel = connect.sync().channel();
    }

    public static void main(String[] args) throws InterruptedException {
        RPCClient rpcClient = new RPCClient();
        rpcClient.connect("localhost", 8080);
        rpcClient.channel.pipeline();
    }

    /**
     * 处理服务器返回的数据
     */
    private class ClientHandle extends SimpleChannelInboundHandler<Transfer> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Transfer msg) throws Exception {

        }
    }
}
