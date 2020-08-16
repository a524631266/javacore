package coderead.nio.netty.codec;
enum OpType{
    GET(0),
    PUT(1),
    POST(2);
    byte value;
    OpType(int i) {
        this.value = (byte) i;
    }
    public byte getValue() {
        return value;
    }

    public static OpType getInstance(int order) {
        return OpType.values()[order];
//        return GET;
    }
}

/**
 * 定义格式
 */
public class Message {
    public static final int MAGIC = 0xDaDa;
    private final String content;
    // 操作类型 1 ==
    private final OpType opType;

    public Message(String content, OpType opType) {
        this.content = content;
        this.opType = opType;
    }

    public Message(String content, byte opType) {
        this(content, OpType.getInstance(opType));
    }

    int getLength(){
        return content.length();
    }

    public String getContent() {
        return content;
    }

    public OpType getOpType() {
        return opType;
    }

    public static void main(String[] args) {
        byte a =(byte) 1;

        int b = a;

        OpType instance = OpType.getInstance(a);

    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", opType=" + opType +
                '}';
    }
}
