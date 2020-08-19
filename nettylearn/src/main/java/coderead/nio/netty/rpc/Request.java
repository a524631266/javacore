package coderead.nio.netty.rpc;

import java.io.Serializable;

public class Request  implements Serializable {
    private static final long serialVersionUID = -55555454645L;
    // 请求的类名
    private final String interfaceName;
    // 请求的方法签名 methodName + 参数
    private final String methodDesc;
    // 请求参数
    private final Object[] parameters;

    public Request( String interfaceName, String methodDesc, String[] parameters) {
        this.interfaceName = interfaceName;
        this.methodDesc = methodDesc;
        this.parameters = parameters;
    }



}
