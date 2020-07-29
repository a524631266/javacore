package coderead.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.IOException;

public class BytBufTest {
    /**
     * readindex
     * writeindex
     *
     */
    @Test
    public void RwTest(){
        ByteBuf buffer = Unpooled.buffer(5, 100);

        buffer = Unpooled.wrappedBuffer(new byte[]{6,6,6,6,6}).duplicate();

        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        byte a = buffer.readByte();
        byte b = buffer.readByte();
        buffer.discardSomeReadBytes();
//        buffer.discardReadBytes(); // 回收的时候，直接使用clear，只是重置了read/write指针
//        byte c = buffer.readByte(); // index异常
        buffer.writeByte((byte) 3);
        buffer.writeByte((byte) 4);

        buffer.writeByte((byte) 5);

        buffer.writeByte((byte) 6); // 自动扩容为 64
        buffer.writeByte((byte) 7);

        System.out.println(a + b);
    }


    @Test
    public void RwTest2() throws IOException {
        ByteBuf buffer = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5});
        buffer.readByte();
//        buffer.readByte();
        ByteBuf duplicate = buffer.duplicate();
        ByteBuf slice = buffer.slice();
//        ByteBuf readSlice = buffer.readSlice(4);
        ByteBuf copy = buffer.copy();
        System.in.read();

    }

    @Test
    public void ReclaimRelease() throws IOException {
        ByteBuf buffer = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5});
        //
        buffer.retain(); // ref + 1
        buffer.release(); // ref -1
        buffer.readByte(); // 可以正常回收
        buffer.clear(); // windex: 0 ,rindex: 0
    }
}
