package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 发送消息
 */
public class RPCClient {
    public static AtomicLong idGenerator = new AtomicLong(0);

    public static Map<Long, ChannelPromise> resultPromiseMap = new HashMap<Long, ChannelPromise>();
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
        while (true){
            System.out.print("your name:");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.next();
            // 用户简单调用这个例子用于远程Rpc调用
            UserService userService = rpcClient.proxyService(UserService.class);
            String name = userService.getName(line);
            System.out.println("远程用户 name : " + name);
        }
    }
    public <T> T proxyService(Class<T> sourceClass ){
        Object o = Proxy.newProxyInstance(
                sourceClass.getClassLoader(), new Class[]{sourceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 正常的流程
                        // Object invoke = method.invoke(proxy, args);
                        // 需要远程调用
                        /**
                         * public interface UserService {
                         *     String getName(String name);
                         *     int getAge(int age);
                         * }
                         */
                        long id = idGenerator.getAndIncrement();
                        Transfer msg = new Transfer(id, false, true);
//                        Class requestClass = UserService.class;
                        String methodName = method.getName();
                        // 目前以string[]格式获取数，参数对象应该也是可序列化的对象
                        Request request = new Request(sourceClass.getName(), methodName, args);
                        msg.updateTarget(request);
                        ChannelPromise channelPromise = new DefaultChannelPromise(channel);

                        ChannelFuture channelFuture = channel.writeAndFlush(msg);
                        channelFuture.addListener(future -> {
                            System.out.println("callback" );
                            resultPromiseMap.put(id, channelPromise);
                        }).sync();
                        // 最多等待8秒钟
                        return channelPromise.get(8, TimeUnit.SECONDS);
                    }
        });
        return (T) o;
    }

    /**
     * 处理服务器返回的数据
     */
    private class ClientHandle extends SimpleChannelInboundHandler<Transfer> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Transfer msg) throws Exception {
            System.out.println("server msg" + msg);
            // 如果是相应
            if (msg.target instanceof Response) {
                Object result = ((Response) msg.target).getResult();
                ChannelPromise channelPromise = resultPromiseMap.get(msg.idCode);
                channelPromise.setSuccess((Void) result);
            }else{
                ctx.fireChannelRead(msg);
            }
        }
    }
}
