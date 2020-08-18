package coderead.nio.netty.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.*;
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
        byte[] body = serialize(msg.target);

        out.writeBytes(header);
        out.writeBytes(body);
    }

    /**
     * java二进制序列化
     * @param target
     * @return
     */
    protected byte[] serialize(Serializable target) {
        byte[] body = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(target);
            body = baos.toByteArray();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return body;
    }

    protected Object serialize(byte[] body) {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        Object o  = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(bais);
            o = inputStream.readObject();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

    }
}
