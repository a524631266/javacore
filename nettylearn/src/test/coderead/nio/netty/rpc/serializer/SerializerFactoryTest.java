package coderead.nio.netty.rpc.serializer;

import coderead.nio.netty.rpc.Request;
import coderead.nio.netty.rpc.Transfer;
import org.junit.Test;

import static org.junit.Assert.*;

public class SerializerFactoryTest {
    /**
     * 测试关键代码 序列化与反序列化
     */
    @Test
    public void getSerializer() {
        Serializer serializer = SerializerFactory.getSerializer(Transfer.SerializerType.JAVA);
        Request request = new Request( "abc", "dasd", new String[]{"asdff"});
        byte[] serialize = serializer.serialize(request);

        Object o = serializer.deSerialize(serialize);
        assert o instanceof Request;
        System.out.println(o);
    }
}