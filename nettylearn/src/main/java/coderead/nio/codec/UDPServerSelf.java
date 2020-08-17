package coderead.nio.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * 先去了解tcp/ip的粘包和拆包的问题
 */
public class UDPServerSelf {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture decode = bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast("beforedecode", new FilterByteBufHandler());
//                        ch.pipeline().addLast("decode",
//                                new LengthFieldBasedFrameDecoder(10,0 ,4));
                        ch.pipeline().addLast("decode", new FixedLengthFrameDecoder(6));
                        ch.pipeline().addLast("myhandler", new MyServerHandler());
                    }
                }).bind(8080);

        decode.sync().channel().closeFuture().await();
    }

    private static class MyServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
            if(msg instanceof DatagramPacket){
                ByteBuf data = ((DatagramPacket) msg).content();
                printContent(data);
            }else if(msg instanceof ByteBuf){
                printContent((ByteBuf) msg);
            }
            ctx.fireChannelRead(ctx);
        }

        private void printContent(ByteBuf data) {
            int i = data.readableBytes();
            byte[] bytes = new byte[i];
            data.readBytes(bytes);
            for (byte aByte : bytes) {
                System.out.println("data  :"+ aByte);
            }
        }
    }

    private static class FilterByteBufHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 如果只是简单传递
            if (msg instanceof DatagramPacket) {
                // 处理数据因为只要传递其中的内容，而不需要传递头，必须要确保释放
                ctx.fireChannelRead(((DatagramPacket) msg).content());
                ((DatagramPacket) msg).release();
                System.out.println("((DatagramPacket) msg).refCnt()： "+ ((DatagramPacket) msg).refCnt());
            } else {
                ctx.fireChannelRead(msg);
            }
        }
    }
}
