package coderead.nio.netty.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.Serializable;
import java.util.List;

/**
 * 0------2-------3--------4----------12---------16
 *   Magic  opcode  status    id          length
 *
 *
 * opcode (1个字节)
 * 0--------1--------2--------3-------------------8
 *   request twoway    event   serialization_type
 *
 */
public class RpcCodec extends ByteToMessageCodec<Transfer> {
    private static int MAGIC = 0xdada;
    private static int HEAD_LENGTH = 16;
    @Override
    protected void encode(ChannelHandlerContext ctx, Transfer msg, ByteBuf out) throws Exception {
        byte[] header = new byte[HEAD_LENGTH];
//        Bytes.bytes
//        Bytes
//        msg.
        byte[] body = deserialize(msg.target);

        out.writeBytes(header);
        out.writeBytes(body);
    }

    private byte[] deserialize(Serializable target) {
        return new byte[0];
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

    }
}
