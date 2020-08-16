package coderead.nio.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.Charset;
import java.util.List;
// 用于编码类型
public class DaoyouProtocal extends ByteToMessageCodec<String> {
    // 标识码　ｃａｆｅ　ｂａｂｅ
    static int MAGIC = 0xDADA;
    static ByteBuf MAGIC_BUFF = Unpooled.copyInt(MAGIC);
    /**
     * 编码数据
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes();
        out.writeInt(MAGIC);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        1.查找
//        int magicIndex = ByteBufUtil.indexOf(in, 0, in.readableBytes(), (byte) MAGIC);
        int magicIndex = indexOf(in, MAGIC_BUFF);
        if(magicIndex < 0) {
            return; // 返回累加 见 callDecode 如果 in是可读的，一直遍历，直到不可读
            // 不可读有两种方式1. 当前in读取完全2. 或者out中有有数据
        }
        if (!in.isReadable(magicIndex + 8)) {
            return; // 返回累加
        }
        int bodyLength = in.slice(magicIndex + 4, 4).readInt();
        if (!in.isReadable( magicIndex + 8 + bodyLength)){
            return; // 返回累加
        }
        in.skipBytes(magicIndex + 8);
        // 读取片段,并把readindex往后移动
        // 当出现拆包问题，由于我们return一个空值，所以codec会累加之前的结果，一般会使用cumulation缓存上次的结果
        // cumulation也是byteBuf的一个实现类，其接口以及所有参数同普通的byteBuf一致
        // 此时的in 为Com
        ByteBuf buf = in.readSlice(bodyLength);
        String message = buf.toString(Charset.defaultCharset());
        out.add(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }


    public static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i ++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex ++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                    break;
                } else {
                    haystackIndex ++;
                    if (haystackIndex == haystack.writerIndex() &&
                            needleIndex != needle.capacity() - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.capacity()) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }
}
