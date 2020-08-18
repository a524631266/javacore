package coderead.nio.netty.rpc;

import java.io.Serializable;

public class Request  implements Serializable {
    private static final long serialVersionUID = -55555454645L;
    // 请求的类名
    private final String className;
    // 请求的方法签名
    private final String methodDesc;
    // 请求参数
    private final String[] args;

    public Request(String className, String methodDesc, String[] args) {
        this.className = className;
        this.methodDesc = methodDesc;
        this.args = args;
    }
}
