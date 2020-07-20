package coderead.nio.httpprotocal;

import coderead.nio.util.ThreadUtil;
import scala.util.parsing.input.StreamReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * IO线程 ：接收线程
 * work 线程 ： 处理业务，通知io线程事件类型
 *
 *
 * Http是半双工吗？对于半双工，半双工是两方可以相互通信，但是只有一条线，意味着
 * 同一时间双方的通信只能有一方发送，另一方堵塞。这个是在tcp/ip协议的基本要求
 * 保证数据流的执行秩序。
 */
public class HttpServer {
    int port;
    Selector selector;
    HttpServlet httpServlet;
    // 处理业务
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    // 初始化操作
    public HttpServer(int port, HttpServlet httpServlet) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        this.httpServlet = httpServlet;
    }
    // 新建一个IO线程
    // 用来接收数
    // 专门处理select请求
    public void start() throws IOException, InterruptedException {
        Thread io_thread = new Thread( () -> {
            while (true) {
                try {
//                    dispatch();
                    dispatch_version2();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        },"IO thread");
        io_thread.start();
        io_thread.join();
    }

    private void dispatch() throws IOException {

        // 堵塞
        int select = selector.select();
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()){
            SelectionKey readyKey = iterator.next();
            iterator.remove();
            // 有效
            if(!readyKey.isValid()){
                continue;
            }
            // 1. 接受新链接并注册给selector一个新的channel保证能够被selector检测到
            // （socket文件以fd文件描述符的形式被channel链接，并给selector检测）
            // 在http中，重新发起请求也会重新建立，因此关闭通道时有必要的，
            // 否则会堆积socket
            if(readyKey.isAcceptable()){
                System.out.println("1. accept new socket!!");
                ServerSocketChannel ssc2 = (ServerSocketChannel) readyKey.channel();
                SocketChannel sc  = ssc2.accept();
                sc.configureBlocking(false);
                sc.register(readyKey.selector(), SelectionKey.OP_READ);
                // 当前是可读状态，device接收完数据会触发OP_READ相关的事件，此时
                // 会做这个操作
            }else if (readyKey.isReadable()) {
                // 2. 业务处理，第一个模型
                // 先读数据，并保存数据到指定的Request对象中
                // 并处理数据（解码+ 业务处理 + 编码）
                System.out.println("2. start handle readable!!");
                SocketChannel sc = (SocketChannel) readyKey.channel();
                System.out.println("获取远程url地址: "+ sc.getRemoteAddress());
                System.out.println("获取当前url地址: "+ sc.getLocalAddress());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // channel的read方法，当sc管道中的数据还有的话，继续读
                // 一般业务处理跟 ByteArrayOutputStream 流一起使用
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (sc.read(buffer) > 0) {
                    // 1. 读取数据结束
                    buffer.flip();
                    baos.write(buffer.array(), 0 , buffer.limit());
                    // 2. 清空数据
                    buffer.clear();
                }
                // bug 发送了一个空消息，所以会不停的
                if ("".equals(baos.toString())) {
                    System.out.println("空连接close！！！！");
                    sc.close();
//                    readyKey.interestOps(SelectionKey.OP_READ);
                    continue;
                }
                // 2.2 解码 decode
                // 对baos读取数据，保证
                Request decode = decode(baos);
                // 3.业务处理（异步）
                Response response = new Response();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("start worker hanlder");
                        if (decode.method.equalsIgnoreCase("get")) {
                            httpServlet.doGet(decode, response);
                        }else {
                            httpServlet.doPost(decode, response);
                        }
                        try {
                            // 4. 解码，需要同步信息的话
                            byte[] encode = encode(response);
                            // 3.返回数据通过Response对象返回给channel
                            sc.write(ByteBuffer.wrap(encode));
//                            sc.close();
                        } catch ( IOException e ) {
                            e.printStackTrace();
                        }
                    }
                });
//                executorService.
                // 异步处理
//                // 4. 解码，需要同步信息的话
//                byte[] encode = encode(response);
//
//                // 3.返回数据通过Response对象返回给channel
//                sc.write(ByteBuffer.wrap(encode));
            }
        }

    }


    private void dispatch_version2() throws IOException {

        // 堵塞
        int select = selector.select(100);
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()){
            SelectionKey readyKey = iterator.next();
            iterator.remove();
            // 有效
            if(!readyKey.isValid()){
                continue;
            }
            // 1. 接受新链接并注册给selector一个新的channel保证能够被selector检测到
            // （socket文件以fd文件描述符的形式被channel链接，并给selector检测）
            // 在http中，重新发起请求也会重新建立，因此关闭通道时有必要的，
            // 否则会堆积socket
            if(readyKey.isAcceptable()){
                System.out.println("1. accept new socket!!");
                ServerSocketChannel ssc2 = (ServerSocketChannel) readyKey.channel();
                SocketChannel sc  = ssc2.accept();
                sc.configureBlocking(false);
                sc.register(readyKey.selector(), SelectionKey.OP_READ);
                // 当前是可读状态，device接收完数据会触发OP_READ相关的事件，此时
                // 会做这个操作
            }else if (readyKey.isReadable()) {
                // 2. 业务处理，第一个模型
                // 先读数据，并保存数据到指定的Request对象中
                // 并处理数据（解码+ 业务处理 + 编码）
                System.out.println("2. start handle readable!!");
                SocketChannel sc = (SocketChannel) readyKey.channel();
                System.out.println("获取远程url地址: "+ sc.getRemoteAddress());
                System.out.println("获取当前url地址: "+ sc.getLocalAddress());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // channel的read方法，当sc管道中的数据还有的话，继续读
                // 一般业务处理跟 ByteArrayOutputStream 流一起使用
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                System.out.println("connectable" + sc.isConnected());
                while (sc.read(buffer) > 0) {
                    // 1. 读取数据结束
                    buffer.flip();
                    baos.write(buffer.array(), 0 , buffer.limit());
                    // 2. 清空数据
                    buffer.clear();
                }
                // bug 发送了一个空消息，所以会不停的
                if ("".equals(baos.toString())) {
                    System.out.println("空连接close！！！！");
                    sc.close();
//                    readyKey.interestOps(SelectionKey.OP_READ);
                    continue;
                }
                // 2.2 解码 decode
                // 对baos读取数据，保证
                Request decode = decode(baos);
                // 3.业务处理（异步）
                Response response = new Response();
                readyKey.attach(response);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("start worker hanlder");
                        if (decode.method.equalsIgnoreCase("get")) {
                            httpServlet.doGet(decode, response);
                        }else {
                            httpServlet.doPost(decode, response);
                        }
                        System.out.println("触发异步的wirte");
                        readyKey.interestOps(SelectionKey.OP_WRITE);
                        // 如果不wakeup之后时，有时候时获取不到write事件的！！！
                        selector.wakeup();
                    }
                });
                // 延时加载，通过监听
            }else if(readyKey.isWritable()){
                try {
                    //
                    SocketChannel sc = (SocketChannel) readyKey.channel();
                    Response response = (Response) readyKey.attachment();
                    // 4. 解码，需要同步信息的话
                    byte[] encode = encode(response);
                    // 3.返回数据通过Response对象返回给channel
                    sc.write(ByteBuffer.wrap(encode));
                    // 必须要设置一个可读操作，否则会死循环
                    readyKey.interestOps(SelectionKey.OP_READ);
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }

    }

    private byte[] encode(Response response) {
        StringBuilder builder = new StringBuilder(512);
        builder.append("HTTP/1.1 ")
                .append(response.code)
                .append(Code.msg(response.code))
                .append("\r\n");

        if(response.body != null && response.body.length()!=0){
            builder.append("Content-Length: ")
                    .append(response.body.length()).append("\r\n")
                    .append("Content-Type: text/html\r\n");
        }
        if (response.headers!=null) {
            String headStr = response.headers.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue() + " \r\n")
                    .collect(Collectors.joining("\r\n"));
            builder.append(headStr);
        }
        builder.append("\r\n").append(response.body);
        return builder.toString().getBytes();
    }

    /**
     *
     * @param baos
     */
    private Request decode(ByteArrayOutputStream baos) throws IOException {
        Request request = new Request();
        //    GET / HTTP/1.1
        //    Host: 192.168.1.86:9999
        //    Connection: keep-alive
        //    Upgrade-Insecure-Requests: 1
        //    User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36
        //    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
        //    Accept-Encoding: gzip, deflate
        //    Accept-Language: en-US,en;q=0.9

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));

        String firstLine = reader.readLine();
        if(firstLine == null){
            return request;
        }
        String[] split = firstLine.trim().split(" ");
        // GET
        request.method = split[0];
        // /
        request.url = split[1];
        //HTTP/1.1
        request.version = split[2];
        // 获取头
        request.heads = getHeads(reader);
        // 请求参数 只有在get的方式下才能处理 为0 ，否则为 -1
        if(request.method.compareToIgnoreCase("get") == 0){


                request.params = getUrlParams(request.url);
                System.out.println("parms" + request.params);
            request.body =getBody(reader);
        }
        // 请求体

        return request;
    }

    private Map<String, String> getHeads(BufferedReader reader) throws IOException {
        Map<String, String> heads = new HashMap<>();
        while (true){
            String line = reader.readLine();
            // 读取完毕
            if("".equals(line.trim())){
                break;
            }
            // 否则处理请求头
            String[] split1 = line.split(":");
            heads.put(split1[0], split1[1]);
        }
        return heads;
    }

    private String getBody(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            // 读取完毕
            if (line==null || "".equals(line.trim())) {
                break;
            }
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * c处理请求
     *
     * @param url
     * @return
     */
    private Map<String, String> getUrlParams(String url) {
        System.out.println("handle url: "+url);
        String[] split1 = url.split("\\?"); // /favicon.ico没有？的时候
        Map<String, String> params = new HashMap<>();
        if(split1.length>1){
            String[] split2 = split1[1].split("&");

            for (int i = 0; i < split2.length; i++) {
                String[] split3 = split2[i].split("=");
                if (split3.length > 1) {
                    // param name value
                    params.put(split3[0], split3[1]);
                } else {
                    params.put(split3[0], null);
                }
            }
        }
        return params;
    }


    /**
     *  1. method 2. 请求地址 3.请求http请求版本
     *  4. 请求参数 5. 请求body
     */
    public static class Request{

        Map<String, String> heads;
        String method;
        String url;
        String version;
        String body;
        Map<String, String> params;

    }

    /**
     *   1. 请求头 2. 状态码 3.请求体
     */
    public static class Response{

        Map<String, String> headers = new HashMap<>();
        int code; // 状态码
        String body;

    }

    public static abstract class HttpServlet{
        public abstract void doGet(Request request, Response response);
        public abstract void doPost(Request request, Response response);
    }

    public static void main(String[] args) {
        try {
            new HttpServer(9999, new HttpServlet() {
                @Override
                public void doGet(Request request, Response response) {
                    response.code = 200;
                    response.body = "hello world";

                    // 细节2
                    System.out.println("request.params" + request.params);
//                if(request.params.containsKey("short")){
//                    response.headers.put("Connection", "close");
//                }else if(request.params.containsKey("long")){
//                    // www.nowamagic.net/academy/detail/23350305
//                    // 为什么长连接失效呢？
//                    response.headers.put("Connection", "keep-alive");
//                    response.headers.put("Keep-Alive", "timeout=30,max=300");
//                }

                }

                @Override
                public void doPost(Request request, Response response) {

                }
            }).start();
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }

    private static class Code {
        public static char[] msg(int code) {
            if(code == 200){
                return "ok".toCharArray();
            }else{
                return "error".toCharArray();
            }
        }
    }
}
