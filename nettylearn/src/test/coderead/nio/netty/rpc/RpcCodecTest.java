package coderead.nio.netty.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RpcCodecTest {
    public AtomicLong idCodeGenerator = new AtomicLong(100);
    @Test
    public void codecTest() {
        Boolean isHeartBeat = false;
        Boolean isRequest = true;
        long id = idCodeGenerator.getAndIncrement();
        Boolean twoWay = true;


        RpcCodec rpcCodec = new RpcCodec();
        Transfer msg = new Transfer(id,
                isHeartBeat, isRequest);
        Request request = new Request("abc", "cdb", new String[]{"adc"});
        msg.updateTarget(request);

        ByteBuf encodeOut = Unpooled.buffer();
        rpcCodec.doEncode(msg, encodeOut);
        // 转换
        Transfer transfer = rpcCodec.doDecode(encodeOut);

        // 断言是否成立
        assert transfer.target instanceof Request;
        assert transfer.twoway == twoWay;
        assert transfer.isHeartbeat == isHeartBeat;
        assert transfer.isRequest == isRequest;
        assert transfer.getIdCode() == id;
        System.out.println("codec success");
    }

}