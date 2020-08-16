package coderead.nio.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.Charset;
import java.util.List;

public class DaoyouProtocalOp extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 4字节 标志位
        out.writeInt(msg.MAGIC);
        // 4 字节内容长度
        out.writeInt(msg.getLength());
        // 1 字节表示不同操作 get put
        out.writeByte(msg.getOpType().getValue());
        out.writeBytes(msg.getContent().getBytes());
    }

    /**
     *  4 --- 4 --- 1 --- body
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicIndex = DaoyouProtocal.indexOf(in, Unpooled.copyInt(Message.MAGIC));
        if(magicIndex < 0 ){
            return;
        }
        // 如果后面的9个字节可读，那么提取前面的信息
        if (!in.isReadable( magicIndex +  9 )) {
            return;
        }
        int bodyLength = in.slice(magicIndex + 4, 4).readInt();
        if(!in.isReadable(magicIndex + 9 + bodyLength)){
            return;
        }
        byte opType = in.slice(magicIndex + 8, 1).readByte();
        in.skipBytes(magicIndex + 9);
        ByteBuf byteBuf = in.readSlice(bodyLength);

        String content = byteBuf.toString(Charset.defaultCharset());
        Message message = new Message(content, opType);
        out.add(message);
    }
}
