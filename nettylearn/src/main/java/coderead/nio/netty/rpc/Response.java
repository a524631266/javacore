package coderead.nio.netty.rpc;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = -55555454646L;
    // 请求参数
    private final Object result;

    public Response( Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                '}';
    }
}
