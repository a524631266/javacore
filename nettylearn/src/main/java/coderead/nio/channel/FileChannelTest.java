package coderead.nio.channel;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest  {

    String file_name="/Users/tommy/temp/coderead-netty/test.txt";
    @Test
    public void test1() throws IOException {
        //1. 打开文件管道
        /*FileInputStream inputStream=new FileInputStream(file_name);
        FileChannel channel = inputStream.getChannel();*/
        FileChannel channel = new RandomAccessFile(file_name,"rw").getChannel();

        ByteBuffer buffer=ByteBuffer.allocate(1024); // 声明1024个空间
        // 从文件中 读取数据并写入管道 在写入缓冲
        channel.read(buffer);
        buffer.flip();
        byte[] bytes= new byte[buffer.remaining()];
        int i =0;
        while (buffer.hasRemaining()){
            bytes[i++]= buffer.get();
        }
        // position=10 limit=10
        System.out.println(new String(bytes));

        // 把缓冲区数据写入到管道
        channel.write(ByteBuffer.wrap("hello 大叔".getBytes()));
        channel.close();
    }
}
