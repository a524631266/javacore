package coderead.nio.netty.rpc;

import java.io.Serializable;

public class Transfer {
    // 1. 魔数要不要放在这里？魔数根据不同协议会有不同的标志。
    // 由于这是我们自己自定义的，且一个编解码拥有唯一的魔数
    // no magic

    // 可序列化对象
    Serializable target;
}
