package coderead.nio.netty.rpc.serializer.javanative;

import coderead.nio.netty.rpc.serializer.Serializer;

import java.io.*;

public class JavaSerializer extends Serializer {
    @Override
    public byte[] serialize(Serializable target) {
        byte[] body = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(target);
            body = baos.toByteArray();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return body;
    }

    @Override
    public Object deSerialize(byte[] body) {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        Object o  = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(bais);
            o = inputStream.readObject();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return o;
    }
}
