package com.zhangll.apm;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.cj.jdbc.Driver;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;


import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * c3p0Source$agent 为获取数据的地址
 *
 * https://www.cnblogs.com/demingblog/p/9970772.html
 */
public class C3P0Test {
    public ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
    public static String KEY = "c3p0Source$agent$connect_num";
    public C3P0Test() {
        try {
            System.out.println(Driver.class.getName());
            comboPooledDataSource.setDriverClass(Driver.class.getName());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/caoren?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
       comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("root");
    }

    public void exec(String sql) throws SQLException {

        Connection connection = comboPooledDataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        boolean execute1 = preparedStatement.execute();
        preparedStatement.executeQuery();
        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            String string = resultSet.getString(1);
//            System.out.println(string);
        }
//        boolean execute = connection.createStatement().execute(sql);
        connection.close();
    }
    public static void main(String[] args) throws SQLException, InterruptedException {


        startHttpServer();

        startDataSource();



    }

    private static void startDataSource() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("start sql:");
            String s = scanner.nextLine();
            System.out.println("sql :" + s);
            C3P0Test c3P0Test = new C3P0Test();
            c3P0Test.exec(s);
        }
    }

    public static void startHttpServer() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        EventLoopGroup boss = new NioEventLoopGroup(1);

        EventLoopGroup worker = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("http", new HttpServerCodec());
                        ch.pipeline().addLast("httpAggregator", new HttpObjectAggregator(512 * 1024));
                        ch.pipeline().addLast(new HttpRequestHandler());
                    }
                });

        ChannelFuture bind = bootstrap.bind(8080);
        bind.sync().channel().closeFuture();
    }

    /**
     * 回归业务流程
     */
    private static class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            String uri = msg.uri();

            HttpMethod method = msg.method();
            String name = method.name();
            System.out.println("method: "+ name);
            if(skipUri(uri)){
                System.out.println("filter uri:" + uri);

            } else {
                System.out.println("response uri:" + uri);

                String responseContext = getProperties(uri);

                String msgs = "<html><head><title>test</title></head><body>你请求uri为：" + uri+" <br/>" + responseContext +"</body></html>";
                FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(msgs, CharsetUtil.UTF_8));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

                // 因为ｈｔｔｐ是一次性的，所以刷新完要记得关闭链接
                ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        }

        private String getProperties(String uri) {
            String[] split = uri.split("\\?");
            if(split==null ){

                return null;
            }
            int length = split.length;
            if(length > 1){
                String interestContext = split[1];// asd=123;
                System.out.println("request name:" + split[0]);
                String properties = interestContext.split("=")[1];
                System.out.println("properties:" + properties);
                Object o = System.getProperties().get(KEY);
                return o!=null?o.toString():"has no properties";
            }
            return null;
        }
    }

    /**
     * 跳过的链接
     * @param uri
     * @return
     */
    private static boolean skipUri(String uri){
        return "/favicon.ico".equals(uri);
    }

}
