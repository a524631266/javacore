package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.serializer.SerializerFactory;
import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.Serializable;
import java.util.List;

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
    private static ByteBuf MAGIC_BUFFER = Unpooled.wrappedBuffer(Bytes.short2bytes(MAGIC));
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

    protected void doEncode(Transfer msg, ByteBuf out) {
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
        Bytes.long2bytes(msg.getIdCode(),header, 4);
        // 序列化
        byte[] body = msg.getSerializeBody();
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
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        Transfer transfer = doDecode(in);
        if(transfer != null){
            out.add(transfer);
        }

    }

    protected Transfer doDecode(ByteBuf in) {
        // 1. 魔数开头
        int i = ByteBufUtil.indexOf(MAGIC_BUFFER, in);
        // 魔数
        if(i < 0){
            return null;
        }
        // 头文件是否可读headLength 切记i位置
        if (!in.isReadable(i + HEAD_LENGTH)) {
            return null;
        }
        // bug1: readBytes会移动index
//        in.readBytes()
        // bug2: getBytes会从头开始读取，不管isreadable
//        in.getByte()
        ByteBuf lengthBuf = in.slice( i + 12, 4);
        int body_Length = lengthBuf.readInt();
        // 全身是否可读 head+body
        if (!in.isReadable(i + HEAD_LENGTH + body_Length)) {
            return null;
        }
        // opcode
        ByteBuf opcodeBuf = in.slice(i+ 2, 1);
        byte opcode = opcodeBuf.getByte(0);
        boolean isRequest = (opcode & REQUEST_FLAG )!= 0;
        boolean isTwoWay = (opcode  & TWOWAY_FLAG)!= 0;
        boolean isHeartbeat = (opcode  & EVENT_FLAG)!= 0;
        int Sertype = (opcode & 0b00011111);
        // idCode 当前channel中的唯一idCode编码
        long idCode = in.slice(i + 4, 8).readLong();
        // body
        byte[] body = new byte[body_Length];
        in.slice(i + HEAD_LENGTH, body_Length).getBytes(0, body);
        // 获取数
        Object o = SerializerFactory.getSerializer(Sertype).deSerialize(body);
        // 反汇编
        if(o instanceof Serializable){
            Transfer transfer = new Transfer(idCode, Sertype, isTwoWay, isHeartbeat, isRequest);
            transfer.updateTarget((Serializable) o);
            return transfer;
        } else {
            return null;
        }
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
