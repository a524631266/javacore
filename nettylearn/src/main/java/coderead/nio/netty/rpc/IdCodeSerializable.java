package coderead.nio.netty.rpc;

import coderead.nio.netty.rpc.serializer.Serializer;
import coderead.nio.netty.rpc.serializer.SerializerFactory;

import java.io.Serializable;

public abstract class IdCodeSerializable<T> implements Serializable {
    // 可序列化对象
    Serializable target;
    protected Transfer.SerializerType serializerType = Transfer.SerializerType.JAVA;
    abstract Long getIdCode();

    /**
     * 设计 序列化对象
     * @return
     */
    byte[] getSerializeBody() {
        //
        byte[] result = null;
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        return serializer.serialize(target);
    };
    public void updateTarget(Serializable o) {
        this.target = o;
    }
    /**
     * 返回序列化
     * @return
     */
    Object deSerialize(byte[] body) {
        //
        byte[] result = null;
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        return serializer.deSerialize(body);
    };

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

        public static SerializerType getType(int type){
            switch (type){
                case 1:
                    return JAVA;
                case 2:
                    return PROTOBUF;
                case 3:
                    return PROTOBUF;
                case 4:
                    return HESSIAN;
                default:
                    return JAVA;
            }
        }
    }

    @Override
    public String toString() {
        return "IdCodeSerializable{" +
                "target=" + target +
                ", serializerType=" + serializerType +
                '}';
    }
}
