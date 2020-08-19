package coderead.nio.netty.rpc;

import java.io.Serializable;


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
public abstract class Transfer {
    // 1. 魔数要不要放在这里？魔数根据不同协议会有不同的标志。
    // 由于这是我们自己自定义的，且一个编解码拥有唯一的魔数
//    final long transid;
//    // target的字节大小最大为4个字节的大小
//    final int length;
    // 可序列化对象
    IdCodeSerializable<Long> target;
    // 状态码 256种状态码应该,只有为response才会返回status
    // 状态时实时变化的，所以可以修改
    byte status = (byte) State.OK.value; //
    // no magic
    // 是否是心跳机制传递的心跳包 event 1 为心跳 0 为非心跳
    final boolean isHeartbeat;
    // 请求： 1; 响应： 0
    final boolean isRequest;
    // 仅在 Req/Res 为1（请求）时才有用，标记是否期望从服务器返回值。如果需要来自服务器的返回值，则设置为1。
    boolean twoway = true;

    SerializerType serializerType = SerializerType.JAVA;

    public Transfer(boolean isHeartbeat, boolean isRequest) {
        this.isHeartbeat = isHeartbeat;
        this.isRequest = isRequest;
    }

    public enum State {
        /**
         * 正常返回
         */
        OK(1),
        /**
         * 状态 错误
         */
        ERROR(2),
        /**
         * illegal
         */
        ILLEGAL(3);
        int value ;
        State(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }
    public enum SerializerType {
        /**
         * JAVA内置的序列化
         */
        JAVA(1),
        /**
         * google PROTOBUF
         */
        PROTOBUF(2),
        /**
         * fast json
         */
        FASTJSON(3),
        /**
         * hessian
         */
        HESSIAN(4);
        int value;
        SerializerType(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    // 自定义一个长度
    abstract int getLength();
}
