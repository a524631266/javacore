package coderead.nio.netty.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import com.alibaba.dubbo.common.io.Bytes;


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
    private static short MAGIC = 0xdad;
    private static int HEAD_LENGTH = 16;
//    // 卸载client端
//    private static AtomicLong requestId = new AtomicLong(0);
    private static byte REQUEST_FLAG = (byte)0b1000_0000;
    private static byte TWOWAY_FLAG = (byte)0b0100_0000;
    private static byte EVENT_FLAG = (byte)0b0010_0000;

    @Override
    protected void encode(ChannelHandlerContext ctx, Transfer msg, ByteBuf out) throws Exception {
        if(msg instanceof Transfer){
            doEncode(msg, out);
        } else{
            throw new IllegalArgumentException("msg error");
        }

    }

    private void doEncode(Transfer msg, ByteBuf out) {
        byte[] header = new byte[HEAD_LENGTH];
        Bytes.short2bytes(MAGIC, header);
        // 设置opcode
        if (msg.isRequest) {
            header[2] |= REQUEST_FLAG;
            header[3] = msg.status;
        }
        if(msg.twoway){
            header[2] |= TWOWAY_FLAG;
        }
        if(msg.isHeartbeat){
            header[2] |= EVENT_FLAG;
        }
        header[2] |= msg.serializerType.value;
        // 设置id
        Bytes.long2bytes(msg.target.getIdCode(),header, 4);
        // 序列化
        byte[] body = msg.target.getSerializeBody(msg.serializerType);
        // 设置length
        Bytes.int2bytes(body.length, header,12);
        out.writeBytes(header);
        out.writeBytes(body);
    }

//    /**
//     * java二进制序列化
//     * @param target
//     * @return
//     */
//    protected byte[] serialize(Serializable target) {
//        byte[] body = null;
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(target);
//            body = baos.toByteArray();
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        }
//        return body;
//    }
//
//    protected Object deSerialize(byte[] body) {
//        ByteArrayInputStream bais = new ByteArrayInputStream(body);
//        Object o  = null;
//        try {
//            ObjectInputStream inputStream = new ObjectInputStream(bais);
//            o = inputStream.readObject();
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        } catch ( ClassNotFoundException e ) {
//            e.printStackTrace();
//        }
//        return o;
//    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

    }

    public static void main(String[] args) {
        byte a = 0;
//        a |= 0b1000_0000;
        a |= (byte) 0x80;
        System.out.println(a);

        byte[] header = new byte[HEAD_LENGTH];
        Bytes.int2bytes(0x1010,header,12);
        System.out.println(header);
    }
}
