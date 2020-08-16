package coderead.nio.netty.redis;

import org.junit.Test;

/**
 * jedis-> bio -> nio
 * netty-> websocket
 *
 *
 * redis 使用文本协议传输数据，不过又有跟二进制一样，体积小，效率高，易阅读
 * 这样只要客户端实现协议就可以通信，所以支持很多语言
 *
 *
 * *3
 * $3
 * set
 * $4
 * name
 * $5
 * luban
 * +OK
 * *2
 * $3
 * get
 * $4
 * name
 * $5
 * luban
 * set age 10
 * +OK
 * get age
 * $2
 * 10
 * ping
 * +PONG
 */
public class Demo {
    // 单个命令
    @Test
    public void testSingle(){
        // 单个命令 + \r\n
        // 1. ping
    }
    // get name
    //  * + 参数个数（2） + \r\n
    // $ + 参数长度（3）+ \r\n
    // 参数1（get） + \r\n
    // $ + 参数长度（4）+ \r\n
    // 参数1（name） + \r\n
    @Test
    public void testName(){
        // set name luban
        // get name
    }


    // list lpush list a
    // get 多个结果与*
    /**
     * 内联命令，不用写报文，通过空格分割
     * 缺点是，内联命令中不能包含\r\n
     * 给list1 添加3个元素
     * lpush list1 a
     * :1
     * lpush list1 b
     * :2
     * lpush list1 c
     * :3
     *
     * 获取3个元素
     * lrange list1 0 5
     * *3
     * $1
     * c
     * $1
     * b
     * $1
     * a
     */


    /**
     * 状态信息 +OK， +PONG
     * 错误信息 -
     */
}
