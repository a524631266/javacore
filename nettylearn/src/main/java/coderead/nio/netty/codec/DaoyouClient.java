package coderead.nio.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaoyouClient {

    private Channel channel;
    private Bootstrap bootstrap;

    public void start() throws InterruptedException {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
//                ch.pipeline().addLast(new DaoyouProtocal());
                ch.pipeline().addLast(new DaoyouProtocalOp());
            }
        });
        channel = bootstrap.connect("localhost", 8080).sync().channel();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DaoyouClient daoyouClient = new DaoyouClient();
        daoyouClient.start();

//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        // 多线程

        int threadNum = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        while (true){
            System.out.print("your message :");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            System.out.print("your type [0 , 1 ,2 ]:");
            Scanner scanner2 = new Scanner(System.in);
            int b = scanner2.nextInt();
//            String line = bufferedReader.readLine();
            for (int i = 0; i < threadNum; i++) {

//                daoyouClient.channel.writeAndFlush(line);
                daoyouClient.channel.writeAndFlush(new Message(line, (byte) b));
            }
        }
    }
}
