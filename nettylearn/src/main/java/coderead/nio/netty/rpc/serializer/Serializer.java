package coderead.nio.netty.rpc.serializer;

import java.io.Serializable;

public abstract class Serializer {
    /**
     * 序列化
     * @param target
     * @return
     */
    public abstract byte[] serialize(Serializable target);

    /**
     * 反序列化为指定对象
     * @param body
     * @return
     */
    public abstract Object deSerialize(byte[] body);
}
