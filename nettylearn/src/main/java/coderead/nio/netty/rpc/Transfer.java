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
    Serializable target;
    // 状态码 256种状态码应该构了
    final byte status;
    // no magic
    // 是否是心跳机制传递的心跳包 event 1 为心跳 0 为非心跳
    final boolean isHeartbeat;
    // 请求： 1; 响应： 0
    final boolean isRequest;
    // 仅在 Req/Res 为1（请求）时才有用，标记是否期望从服务器返回值。如果需要来自服务器的返回值，则设置为1。
    boolean twoway = true;

    SerializerType serializerType = SerializerType.JAVA;

    public Transfer(byte status, boolean isHeartbeat, boolean isRequest) {
        this.status = status;
        this.isHeartbeat = isHeartbeat;
        this.isRequest = isRequest;
    }

    public enum SerializerType {
        JAVA,
        Protostuff,
        FastJson,
        Hessian
    }
    // 自定义一个长度
    abstract int getLength();
}
