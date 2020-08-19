package coderead.nio.netty.rpc;

import java.io.Serializable;
import java.util.Arrays;

public class Request  implements Serializable {
    private static final long serialVersionUID = -55555454645L;
    // 请求的类名
    private final String interfaceName;
    // 请求的方法签名 methodName + 参数
    private final String methodDesc;
    // 请求参数
    private final Object[] parameters;

    public Request(String interfaceName, String methodDesc, Object[] parameters) {
        this.interfaceName = interfaceName;
        this.methodDesc = methodDesc;
        this.parameters = parameters;
    }

    /**
     * 返回唯一标志
     * @return
     */
    public String getToken(){
        return interfaceName + methodDesc;
    }
    @Override
    public String toString() {
        return "Request{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodDesc='" + methodDesc + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
