package coderead.nio.netty.redis;

import com.sun.org.apache.bcel.internal.classfile.CodeException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.redis.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 抽象类 RedisMessage
 * 1. 简单返回值 SimpleStringRedisMessage ErrorRedisMessage IntegerRedisMessage
 *
 *
 */
public class RedisClient {

    private Channel channel;

    public void openConnection(String host, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        channel = bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new RedisDecoder());
                        // bulk = $7(header) zhangll(bulk content)

                        // 顺序不能错乱了，否则无法实现
                        // 合并两个 BulkStringHeaderRedisMessage + DefaultLastBulkStringRedisContent == FullBulkStringRedisMessage
                        ch.pipeline().addLast("bulk-aggregator", new RedisBulkStringAggregator());

                        // FullBulkStringRedisMessage + FullBulkStringRedisMessage =ArrayRedisMessage[children=3]
                        ch.pipeline().addLast("array-aggragator", new RedisArrayAggregator());



                        ch.pipeline().addLast("encoder", new RedisEncoder());

                        ch.pipeline().addLast("dispatcher", new MyRedisHandler());
                    }
                }).connect(host, port).sync().channel();
//        channel.closeFuture();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        RedisClient redisClient = new RedisClient();
        redisClient.openConnection("localhost", 6379);


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            System.out.print("> ");
//            Scanner reader = new Scanner(System.in);
            String cmd = reader.readLine();
            redisClient.channel.writeAndFlush(cmd);
        }

    }


    private static class MyRedisHandler extends ChannelDuplexHandler {
        private static String split_char = "\\s+";
        /**
         * 业务需求为 输入字符串 生成不同 的redisMessage
         */
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (!(msg instanceof String)) {
                ctx.write(msg);
                return;
            }
            String cmd = (String) msg;
            // 空格分割
            String[] tokens = cmd.split(split_char);

            List<RedisMessage> collect = Arrays.stream(tokens).map((token) -> {
                // bulkstring类似于就是参数信息，用于表示set get name 等等的信息
                return new FullBulkStringRedisMessage(Unpooled.wrappedBuffer(token.getBytes()));
            }).collect(Collectors.toList());
            ctx.write(new ArrayRedisMessage(collect), promise);

        }


        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("msg:"+ msg);

            // 严格意义上来看要打印的数据
            printAggregatedResponse(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }

        private void printAggregatedResponse(Object msg) {

            if(msg instanceof SimpleStringRedisMessage){
                System.out.println(String.format("simpleString: %s", ((SimpleStringRedisMessage) msg).content()));
            }else if(msg instanceof ErrorRedisMessage){
                System.out.println(String.format("error: %s", ((ErrorRedisMessage) msg).content()));
            }else if(msg instanceof IntegerRedisMessage){

                System.out.println(String.format("integer: %s",
                        ((IntegerRedisMessage) msg).value()));
            }else if(msg instanceof FullBulkStringRedisMessage){
                System.out.println(String.format("FullBulkS: %s",
                ((FullBulkStringRedisMessage) msg).content().toString(Charset.defaultCharset())));
            } else if (msg instanceof ArrayRedisMessage){
                Iterator<RedisMessage> iterator = ((ArrayRedisMessage) msg).children().iterator();
                int count = 0;
                while (iterator.hasNext()){
                    RedisMessage next = iterator.next();
                    if(next instanceof FullBulkStringRedisMessage){
                        ByteBuf content = ((FullBulkStringRedisMessage) next).content();
                        String result = content.toString(Charset.defaultCharset());
                        System.out.println(String.format("child:%d, %s", ++count, result));
                    }
                }
            }else{
                throw  new CodecException("unknown message: " + msg);
            }
        }
    }

}
