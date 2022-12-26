package com.zhangll.apm.agent;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/11/8
 */
public class JdbcAgentTest {
    @Test
    public void test(){

        String JDBC_URL = "jdbc:mysql://10.240.13.75:3306/yygl_test?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, "yygl_test", "NbAGuKi^ZnUgfGh2")) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("select * from t_trial_vehicle_project")) {
                    while (rs.next()) {
                        long id = rs.getLong(1); // 注意：索引从1开始
                        long grade = rs.getLong(2);
                        String name = rs.getString(3);
                        String gender = rs.getString(4);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}