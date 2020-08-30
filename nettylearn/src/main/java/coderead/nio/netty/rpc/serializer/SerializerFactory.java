package coderead.nio.netty.rpc.serializer;

import coderead.nio.netty.rpc.Transfer;
import coderead.nio.netty.rpc.serializer.javanative.JavaSerializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {
    public static Map<Transfer.SerializerType, Serializer> serializerMap = new HashMap<Transfer.SerializerType, Serializer>();
    static {
        serializerMap.put(Transfer.SerializerType.JAVA, new JavaSerializer());
    }

    public static Serializer getSerializer(Transfer.SerializerType type) {
        return serializerMap.get(type);
    }

    public static Serializer getSerializer(int sertype) {
        return getSerializer(Transfer.SerializerType.getType(sertype));
    }
}
