package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 发送消息
 */
public class RPCClient {
    public static AtomicLong idGenerator = new AtomicLong(0);
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

    public static void main(String[] args) throws InterruptedException, NoSuchMethodException {
        RPCClient rpcClient = new RPCClient();
        rpcClient.connect("localhost", 8080);
        while (true){
            Scanner scanner = new Scanner(System.in);
            Transfer msg = new Transfer(idGenerator.getAndIncrement(), false, true);
            Class requestClass = UserService.class;
            /**
             * public interface UserService {
             *     String getName(String name);
             *     int getAge(int age);
             * }
             */
//            Method requestMethod = requestClass.getMethod("getName");
            String line = scanner.nextLine();
            String[] parameters =  new String[]{line};
            // 目前以string[]格式获取数，参数对象应该也是可序列化的对象
            Request request = new Request(requestClass.getName(),"getName",parameters);
            msg.updateTarget(request);
            rpcClient.channel.writeAndFlush(msg);

            scanner.close();
        }
    }

    /**
     * 处理服务器返回的数据
     */
    private class ClientHandle extends SimpleChannelInboundHandler<Transfer> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Transfer msg) throws Exception {
            System.out.println("server msg" + msg);
        }
    }
}
