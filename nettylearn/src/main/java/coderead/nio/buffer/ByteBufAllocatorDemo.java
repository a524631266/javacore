package coderead.nio.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

/**
 * 关于allocator相关的内容
 */
public class ByteBufAllocatorDemo {

    @Test
    public void testAllocateHeapBuffer(){
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(100);
        writeAndRead(byteBuf);

        splitline();
        // 清空
        byteBuf.clear();

//        // number2
//        writeAndRead(byteBuf);

        // 区域一块区域
        for (int i = 0; i < 100; i++) {
            byteBuf.writeByte(i);
        }

        byteBuf.readByte();
        byteBuf.readByte();


//        byteBuf.clear();
        // 特性1:仅仅只是做了切片,对原先的不处理(即使clear),也可以slice数组
        ByteBuf slice = byteBuf.slice(3, 5);
        printContent(slice);

        slice.clear();
        slice.writeByte(99);
        slice.writeByte(99);

        splitline();
        printContent(byteBuf);


    }


    @Test
    public void testDiscard(){
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(10);

        allocatNumber(byteBuf, 10);

        // 读取两个字节
        readStep(byteBuf, 2);
        splitline();
        // 特性: discardReadBytes返回的this,即byte本省
        ByteBuf byteBuf1 = byteBuf.discardReadBytes();
        // true
        System.out.println(String.format("(byteBuf1 == byteBuf): %b", (byteBuf1 == byteBuf)));
        // 11开始,即从前面的数组的writeindex开始写数据
        allocatNumber(byteBuf, 11, 10);
        splitline();
        printContent(byteBuf);
    }


    @Test
    public void testRest(){
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(10);
        allocatNumber(byteBuf, 0,10);
        readStep(byteBuf,2);
        byteBuf.resetReaderIndex();
        splitline();
        printContent(byteBuf);


        // 在实际场景中writeindex可以小于readindex,这个与nio Bytebuffer不一样
        byteBuf.resetReaderIndex();
        splitline();
        readStep(byteBuf,2);

        // 重置writteIndex,会重置writeINdex到上次mark的地方,默认为0
        byteBuf.resetWriterIndex();
        allocatNumber(byteBuf, 100, 1);
        printContent(byteBuf);

    }

    private void allocatNumber(ByteBuf byteBuf, int start, int length) {
        for (int i = 0; i < length; i++) {
            byteBuf.writeByte(start + i);
        }
    }

    private void readStep(ByteBuf byteBuf, int step) {

        for (int i = 0; i < step; i++) {
            byteBuf.readByte();
        }
    }

    private void allocatNumber(ByteBuf byteBuf, int length) {
        allocatNumber(byteBuf, 0 , length);
    }

    @Test
    public void testClear(){
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(10);
        //        byteBuf.clear();
        // 特性1:仅仅只是做了切片,对原先的不处理(即使clear),也可以slice数组
        // 特性2: 切片的最大容量为指定的length长度,即与复制的writeindex一致.
        // 特性3: 切片不会改变原 ByteBuf 的引用计数。
        ByteBuf slice = byteBuf.slice(3, 5);


        splitline();


        slice.clear();
        slice.writeByte(99);
        slice.writeByte(99);
        printContent(slice);

        splitline();
        printContent(byteBuf);
    }



    @Test
    public void testRelease(){

        // 浅层copy不增加引用
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(100);

        allocatNumber(byteBuf, 0,100);
        ByteBuf slice = byteBuf.slice(10, 10);

        // 4. 对单独一个切片的释放,会导致原先整个buffer的释放
        slice.release();
        printContent(byteBuf);


    }
    private void printContent(ByteBuf buf) {
        while (buf.isReadable()){
            byte b = buf.readByte();
            System.out.println("read: "+ b);
        }
    }

    private void splitline() {
        System.out.println("###############################");
    }

    private void writeAndRead(ByteBuf byteBuf) {
        for (int i = 0; i < 100; i++) {
            byteBuf.writeByte(i);
        }
        // 1. 顺序读取所有数据
        while (byteBuf.isReadable()){
            byte b = byteBuf.readByte();
            System.out.println("read: "+ b);
        }
    }

    /**
     * 自动扩容不能超过maxCapacity
     */
    @Test
    public void testAutoIncrementCapacity(){
        PooledByteBufAllocator allocator = new PooledByteBufAllocator();

        ByteBuf byteBuf = allocator.heapBuffer(100, 110);

        allocatNumber(byteBuf, 0, 111);
        printContent(byteBuf);


    }
}
