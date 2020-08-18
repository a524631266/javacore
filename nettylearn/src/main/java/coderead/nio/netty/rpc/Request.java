package coderead.nio.netty.rpc;

import java.io.Serializable;

public class Request  implements Serializable {
    private static final long serialVersionUID = -55555454645L;
    final long requestId;
    // 请求的类名
    private final String interfaceName;
    // 请求的方法签名 methodName + 参数
    private final String methodDesc;
    // 请求参数
    private final Object[] parameters;

    public Request(long requestId, String interfaceName, String methodDesc, String[] parameters) {
        this.requestId = requestId;
        this.interfaceName = interfaceName;
        this.methodDesc = methodDesc;
        this.parameters = parameters;
    }
}
