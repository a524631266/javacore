package coderead.nio.buffer;
/**
 * @Copyright 源码阅读网 http://coderead.cn
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

/**
 * @author 鲁班大叔
 * @date 2020/7/109:40 PM
 */
public class IntBufferTest {

    @Test
    public void testRead(){
        ByteBuf buffer = Unpooled.wrappedBuffer("hello".getBytes());
        for (int i = 0; i < buffer.capacity(); i ++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }
    @Test
    public void test1(){
        IntBuffer allocate = IntBuffer.allocate(6);// position=0、limit=6、capacity=6
        allocate.put(1);
        allocate.put(2); // position=2、limit=6、capacity=6
        allocate.flip(); // position=0、limit=2、capacity=6
        allocate.get();  // position=1、limit=2、capacity=6
        allocate.get();  //  position=2、limit=2、capacity=6
//        allocate.put(3); //  超出limit 限制 ==》 BufferOverflowException
        //allocate.get(); // 超出limit 限制 ==》 BufferUnderflowException

        // Buffer 没有删除数据这个说法
        // 循环利用空间
        // 回到缓冲的初始状态
        allocate.clear(); //// position=0、limit=6、capacity=6
        for (int i = 0; i < 6; i++) {
            allocate.put(i+1);
        }
        allocate.flip();
        allocate.get();
        allocate.get();
        allocate.mark();
        int i = allocate.get()+100;
        int i1 = allocate.get()+100;
        allocate.reset();
        allocate.put(i);
        allocate.put(i1);
        System.out.println(Arrays.toString(allocate.array()));
    }


    @Test
    public void testReadWrite() throws CharacterCodingException {
        // 一个字符一个字节 6个字节
//        ByteBuf buffer = Unpooled.wrappedBuffer("helloo".getBytes());
        ByteBuf buffer = Unpooled.wrappedBuffer("我是天才".getBytes());
        System.out.println(buffer.capacity());
        while (buffer.isReadable()){
            // readChar 读两个字节，神奇
//            char c = buffer.readChar();
            // readInt 读四个字节，正常
//            int c = buffer.readInt();
            // readBoolean 1个字节
//            boolean c = buffer.readBoolean();
             //  readFloat 读四个字节，正常
//            float c = buffer.readFloat();
            //  readLong 读8个字节，正常
//            long c = buffer.readLong();
            // readShort 读两个字节，正常
//            short c = buffer.readShort();
            //  readDouble 读8个字节，正常
//            double c = buffer.readDouble();
            // readMedium 三个字节
//            int c = buffer.readMedium();

            // 如何转换呢？
            ByteBuf byteBuf = buffer.readSlice(3);
//            final CharsetEncoder encoder = CharsetUtil.encoder(CharsetUtil.UTF_8);
            char c = byteBuf.readChar();
//            ByteBuffer encode = encoder.encode(c1);
            System.out.println(c);
        }
    }


}
