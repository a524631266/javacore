package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.service.UserService;
import coderead.nio.netty.rpc.service.UserServiceImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import jdk.internal.org.objectweb.asm.Type;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 发送消息
 */
public class RPCClient {
    public static AtomicLong idGenerator = new AtomicLong(0);
    /**
     * 以id作为返回结果的存储地点，
     */
    public static Map<Long, Promise<Response>> resultPromiseMap = new HashMap<Long,  Promise<Response>>();
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
        Scanner scanner = new Scanner(System.in);
        UserService userService = rpcClient.proxyService(UserService.class);
        while (true){
            System.out.print("your name:");
            String name = scanner.next();
//            String name1 = userService.name;
            // 用户简单调用这个例子用于远程Rpc调用
            int age = userService.getAge(name);
            try{
                String name2 = userService.getName(name);
            }catch ( NullPointerException np ){
                np.printStackTrace();
//            System.out.println("远程用户 name : " + name)
            };
        }
    }

    /**
     * 什么时候会触发method方法
     * 1. 调用方法的时候
     * 2. 在处理方法的时候，如果调用的是Object方法，那么就不要远程用
     * @param sourceClass
     * @param <T>
     * @return
     */
    public <T> T proxyService(Class<T> sourceClass) {
        Class<?>[] interfaces = null;
        if (sourceClass.isInterface()) {
            interfaces = new Class[]{sourceClass};
        } else {
            return null;
        }
        Object o = Proxy.newProxyInstance(
                sourceClass.getClassLoader(), interfaces,
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
                        // 正常的流程
                        // Object invoke = method.invoke(proxy, args);
                        // 需要远程调用
                        Response response = callRemoteMethod(method, args, sourceClass);
                        if (response != null) {
                            return response.getResult();
                        } else {
                            return null;
                        }
                    }
                });
        return (T) o;
    }

    private <T> Response callRemoteMethod(Method method, Object[] args, Class<T> sourceClass) {
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
        Class<?> returnType = method.getReturnType();
        Class<?> declaringClass = method.getDeclaringClass();
        Class<?>[] parameterTypes = method.getParameterTypes();
        String methodDescriptor = Type.getMethodDescriptor(method);
        Class<? extends Object[]> parameterClass = args.getClass();

        // 目前以string[]格式获取数，参数对象应该也是可序列化的对象
        Request request = new Request(sourceClass.getName(), methodDescriptor, args);
        // 设计并不好，需要updateTarget，会
        msg.updateTarget(request);
        DefaultPromise<Response> promise = new DefaultPromise(channel.eventLoop());
        ChannelFuture channelFuture = channel.writeAndFlush(msg);
        channelFuture.addListener(future -> {
            System.out.println("callback");
            resultPromiseMap.put(id, promise);
        });
        // 最多等待8秒钟 ,在这里不够优雅
        try {
            return promise.get(1, TimeUnit.SECONDS);
        } catch ( InterruptedException e ) {
            e.printStackTrace();
            return null;
        } catch ( ExecutionException e ) {
            e.printStackTrace();
            return null;
        } catch ( java.util.concurrent.TimeoutException e ) {
            e.printStackTrace();
            return null;
        }
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
                Promise<Response> promise = resultPromiseMap.get(msg.idCode);
                promise.setSuccess((Response) msg.target);
            }else{
                ctx.fireChannelRead(msg);
            }
        }
    }

}
