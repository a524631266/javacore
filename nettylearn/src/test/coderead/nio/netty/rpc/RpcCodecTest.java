package coderead.nio.netty.rpc;

import org.junit.Test;

import static org.junit.Assert.*;

public class RpcCodecTest {
    /**
     * 测试关键代码 序列化与反序列化
     */
    @Test
    public void serialize() {
        RpcCodec rpcCodec = new RpcCodec();
        byte[] serialize1 = rpcCodec.serialize(new Request("abc", "dasd", new String[]{"asdff"}));
        System.out.println(serialize1);

        Object des = rpcCodec.serialize(serialize1);

    }
}