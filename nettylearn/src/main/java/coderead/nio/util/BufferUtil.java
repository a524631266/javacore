package coderead.nio.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class BufferUtil {
    public static final String byte2string(ByteBuffer byteBuffer){
        if (byteBuffer.position()!= 0){
            byteBuffer.flip();
        }
//        byteBuffer
//        byteBuffer.flip();
        Charset charset = Charset.forName("UTF-8");
//        char[] array = charset.decode(byteBuffer).array();
//        CharBuffer decode = charset.decode(byteBuffer);

        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = CharBuffer.allocate(byteBuffer.limit());
        try {
            charBuffer = decoder.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
//        System.out.println(decode);
        return charBuffer.toString();

    }

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.put((byte) 1);
        buffer.put((byte) 3);
        System.out.println(byte2string(buffer));
    }
}
