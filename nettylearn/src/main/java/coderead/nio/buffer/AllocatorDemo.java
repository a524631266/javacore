package coderead.nio.buffer;

import coderead.nio.metric.TimeTest;
import io.netty.buffer.*;
import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AllocatorDemo extends TimeTest{
    public static Integer ITERTESTCOUNT = 10000;
    @Test
    public void testHeapBuffer(){
        // true 表示分配一个direct类型的buffer

        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(true);

        // 默认 256 大小
        for (int i = 0; i < ITERTESTCOUNT ; i++) {

            ByteBuf heapBuffer = allocator.heapBuffer(ITERTESTCOUNT);

            time.stop();
            time = responses.time();
        }

    }

    @Test
    public void testDirectBuffer(){
        // true 表示分配一个direct类型的buffer

        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        // 默认 256 大小
        for (int i = 0; i < ITERTESTCOUNT; i++) {

            ByteBuf directBuffer = allocator.directBuffer(ITERTESTCOUNT);

            time.stop();
            time = responses.time();
        }
    }

    @Test
    public void testPooledByteAllocator_Direct(){
        PooledByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator();
        for (int i = 0; i < ITERTESTCOUNT; i++) {
            ByteBuf byteBuf = pooledByteBufAllocator.directBuffer(ITERTESTCOUNT);

            time.stop();
            time = responses.time();
        }
    }
    @Test
    public void testPooledByteAllocator_heap(){
        PooledByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator();
        for (int i = 0; i < ITERTESTCOUNT; i++) {
            ByteBuf byteBuf = pooledByteBufAllocator.heapBuffer(ITERTESTCOUNT);
            time.stop();
            time = responses.time();
        }
    }

    @Test
    public void testUnPooledByteAllocator_Direct(){
        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        for (int i = 0; i < ITERTESTCOUNT; i++) {
            ByteBuf byteBuf = allocator.directBuffer(ITERTESTCOUNT);

            time.stop();
            time = responses.time();
        }
    }
    @Test
    public void testUnPooledByteAllocator_heap(){
        UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        for (int i = 0; i < ITERTESTCOUNT; i++) {
            ByteBuf byteBuf = allocator.heapBuffer(ITERTESTCOUNT);
            time.stop();
            time = responses.time();
        }
    }


    @Test
    public void testRight(){
        // 在java种 又以
        int normalizedCapacity = 4723;
//        int d = 513 >>> 1;
        normalizedCapacity --;
        normalizedCapacity |= normalizedCapacity >>>  1;
        normalizedCapacity |= normalizedCapacity >>>  2;
        normalizedCapacity |= normalizedCapacity >>>  4;
        normalizedCapacity |= normalizedCapacity >>>  8;
        normalizedCapacity |= normalizedCapacity >>> 16;
        normalizedCapacity ++;
        System.out.println(normalizedCapacity);
    }

    @Test
    public void testUnpooledUtil(){
        // 非池化
        ByteBuf byteBuf = Unpooled.wrappedBuffer("abc".getBytes());

    }

    /**
     * 出站与入站约定：https://netty.io/wiki/reference-counted-objects.html
     */
    @Test
    public void testReferent(){
        ByteBuf byteBuf = Unpooled.wrappedBuffer("abc".getBytes());
        assert  byteBuf.refCnt() == 1;

        // 不会增加ref
        ByteBuf order = byteBuf.order(ByteOrder.BIG_ENDIAN);
        // 不会增加ref ,ref重置为1
        ByteBuf copy = byteBuf.copy();
        ByteBuf readBytes = byteBuf.readBytes(3);



        byteBuf.retain();
        assert byteBuf.refCnt() == 2;
        boolean destroyed = byteBuf.release();
        assert !destroyed;
        assert byteBuf.refCnt() == 1;
        ByteBuf duplicate = byteBuf.duplicate();
        assert duplicate.refCnt() == 1;
        byteBuf.retain();
        assert duplicate.refCnt() == 2;
        duplicate.release();
        duplicate.release();

        assert copy.refCnt() == 1;
        assert readBytes.refCnt() == 1;

        byteBuf.clear();
    }

}
