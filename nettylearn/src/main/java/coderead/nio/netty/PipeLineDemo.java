package coderead.nio.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.junit.Test;

import java.io.IOException;

import java.net.InetSocketAddress;

public class PipeLineDemo {
    public NioEventLoopGroup group = new NioEventLoopGroup(1);



    @Test
    public void openServer() throws IOException {
        NioDatagramChannel channel = new NioDatagramChannel();
        group.register(channel);
        channel.pipeline().addLast(new SimpleChannelInboundHandler<Object>(){
            @Override
            public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("read "+ msg);
                if(msg instanceof  DatagramPacket){
                    DatagramPacket dp = (DatagramPacket) msg;
                    String s = dp.toString();
                    System.out.println("message :" + s);
                }
//                super.channelRead(ctx, msg);
            }
        }) ;
        channel.bind(new InetSocketAddress(8081));
        System.in.read();
    }

    @Test
    public void testPipeLineWrite() throws IOException {
        NioDatagramChannel channel = new NioDatagramChannel();
        group.register(channel);

        channel.bind(new InetSocketAddress(8082));

        channel.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer("asdf".getBytes()),
                    new InetSocketAddress("localhost",8081)
                ), new DefaultChannelPromise(channel){
            @Override
            public boolean tryFailure(Throwable cause) {
                return false;
            }
        });


        System.in.read();
    }

    /**
     * read write
     * @throws IOException
     */
    @Test
    public void testPipeLineReadWrite() throws IOException {
        NioDatagramChannel channel = new NioDatagramChannel();
        group.register(channel);
        channel.bind(new InetSocketAddress(8083));
        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("read mesage:::::::" + msg);
                ctx.fireChannelRead(msg);
            }
        });




        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

                System.out.println("write1: " + msg);
                ctx.write(
                        new DatagramPacket(Unpooled.wrappedBuffer(msg.toString().getBytes()),
                                new InetSocketAddress("localhost",8081)), new DefaultChannelPromise(ctx.channel()){
                    @Override
                    public boolean tryFailure(Throwable cause) {
                        return false;
                    }
                });
            }
        });

        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

                System.out.println("write2: " + msg);
                ctx.write("mmmmm" + msg, new DefaultChannelPromise(ctx.channel()){
                    @Override
                    public boolean tryFailure(Throwable cause) {
                        return false;
                    }
                });
            }
        });
        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("read message2:::::::" + msg);
                ctx.fireChannelRead(msg);
                ctx.write(msg);
            }
        });
//        channel.bind(new InetSocketAddress(8082));
        channel.pipeline().fireChannelRead("123123");
        // 此时会走handler
//        channel.pipeline().write("1232344444");
//        // 此时会从缓存区开始发送数据到绑定端口。
        System.out.println("flush start");
        channel.pipeline().flush();
//        channel.unsafe().flush();
//        channel.pipeline().flush();
//        channel.eventLoop().submit(new Runnable() {
//            @Override
//            public void run() {
//                channel.pipeline().write("1232344444");
//            }
//        });

        System.in.read();
    }

}
