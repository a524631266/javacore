package com.zhangll.core.coderead.tx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 不可重复读的额情况
 * 在同一个事务中（同一链接）的所有查询结果一致，不管在间隔中是否提交过数据
 * 即在同一链接且在autocomit = false块中都属于同一事务
 */
public class CommitRead extends JdbcBase {


    public static void main(String[] args) {

        new Thread(() -> {

            Connection connection = openConnector();
            insert(connection, "zll",100, "zhangll");

        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 未提交读，在同一个链接中的不同事务中
            Connection connection = openConnector();
            try {
                connection.setAutoCommit(false);
//　ｏｒａｃｌｅ中可以设置ｒｅａｄ.uncommited
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//                mysql  中需要设置ｉｒｅｐａｔｅａｂｌｅ　ｒｅａｄ
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            select(connection, "zll");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            select(connection, "zll");

            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();


    }

    private static void insert(Connection connection, String user, int money, String accountname) {

        try {
//            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement("insert into account(user, money, accountname) values (?,?,?)");
            preparedStatement.setString(1,user);
            preparedStatement.setInt(2,money);
            preparedStatement.setString(3,accountname);

            preparedStatement.executeUpdate();

            System.out.println("提交完毕");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void select(Connection connection, String queryname) {
        try {


            PreparedStatement preparedStatement = connection.prepareStatement("select * from account where user = ?");

            preparedStatement.setString(1, queryname);


            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("执行查询");
            while (resultSet.next()){
                for (int i = 0; i < 4; i++) {
                    String string = resultSet.getString(i+1);
                    System.out.print(string + " ");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
