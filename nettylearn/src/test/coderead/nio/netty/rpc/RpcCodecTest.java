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

        String interfaceName = "abc";
        String methodDesc = "cdb";

        RpcCodec rpcCodec = new RpcCodec();
        Transfer msg = new Transfer(id,
                isHeartBeat, isRequest);
        Request request = new Request(interfaceName, methodDesc, new String[]{"adc"});
        msg.updateTarget(request);

        ByteBuf encodeOut = Unpooled.buffer();
        rpcCodec.doEncode(msg, encodeOut);
        // 转换
        Transfer transfer = rpcCodec.doDecode(encodeOut);

        // 断言是否成立
        assert transfer.target instanceof Request;
        if (transfer.target instanceof Request) {
            assert ((Request) transfer.target).getToken().equals(interfaceName + methodDesc);
        }

        assert transfer.twoway == twoWay;
        assert transfer.isHeartbeat == isHeartBeat;
        assert transfer.isRequest == isRequest;
        assert transfer.getIdCode() == id;


        System.out.println("codec success");
    }


    @Test
    public void codecTest2() {
        Boolean isHeartBeat = true;
        Boolean isRequest = false;
        long id = idCodeGenerator.getAndIncrement();
        Boolean twoWay = true;

        String result = "asdddad";
        RpcCodec rpcCodec = new RpcCodec();
        Transfer msg = new Transfer(id,
                isHeartBeat, isRequest);
        Response response = new Response(result);
        msg.updateTarget(response);

        ByteBuf encodeOut = Unpooled.buffer();
        rpcCodec.doEncode(msg, encodeOut);
        // 转换
        Transfer transfer = rpcCodec.doDecode(encodeOut);

        // 断言是否成立
        assert transfer.target instanceof Response;
        if (transfer.target instanceof Response) {
           assert  ((Response) transfer.target).getResult().equals(result);
        }
        assert transfer.twoway == twoWay;
        assert transfer.isHeartbeat == isHeartBeat;
        assert transfer.isRequest == isRequest;
        assert transfer.getIdCode() == id;
        System.out.println("codec success");
    }

}