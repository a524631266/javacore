package coderead.nio.netty.rpc.serializer.protobuf;

import coderead.nio.netty.rpc.serializer.Serializer;

import java.io.Serializable;

public class PtotobufSerializer extends Serializer {
    @Override
    public byte[] serialize(Serializable target) {

        return new byte[0];
    }

    @Override
    public Object deSerialize(byte[] body) {
        return null;
    }
}
