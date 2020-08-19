package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.serializer.Serializer;
import coderead.nio.netty.rpc.serializer.SerializerFactory;

import java.io.Serializable;

public abstract class IdCodeSerializable<T> implements Serializable {
    abstract T getIdCode();

    byte[] getSerializeBody(Transfer.SerializerType type) {
        //
        byte[] result = null;
        Serializer serializer = SerializerFactory.getSerializer(type);
        return serializer.serialize(this);
    };

}
