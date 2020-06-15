package com.zhangll.core;

import java.time.*;

public class TimeUtilss {
    public static void main(String[] args) {
        /**
         * LocalDate 使用静态方法构造
         * 获取本地时间，本地时间的应用比较广泛，数据同步是否可以用这个时间来做？
         */
        LocalDate localDate = LocalDate.now();
        // 获取localDate的时间间隔
        LocalDate of = LocalDate.of(1970, 1, 31);
        System.out.println(of);
        // 获取年分
        int year = localDate.getYear();
        System.out.println(year);
        // 能够获取距离Epoch时间的时长 这里为30 天
        System.out.println(of.toEpochDay());

        /**
         * 这个工具很有用，切记
         */
        Instant now = Instant.now();
        // 根据时间戳EpochSecond 以1970-01-01为标准的纪元时间获取instant，
        Instant instant = Instant.ofEpochSecond(1589123451);
        //获取10位数字 即到秒
        System.out.println(now.getEpochSecond());
        // 获取13位数字 毫秒级别的milli
        System.out.println(instant.toEpochMilli());
        //

        // zonedDateTime 是用来获取哪里的时间段呢
        // zoned是 LocalDate和
        ZonedDateTime now1 = ZonedDateTime.now();

        DayOfWeek dayOfWeek = now1.getDayOfWeek();
        int value = dayOfWeek.getValue();
        // 获取当前区域 Asia/Shanghai
        System.out.println(now1.getZone());
        // 转换为纽约时间
        ZonedDateTime zonedDateTime = now1.withZoneSameInstant(ZoneId.of("Africa/Cairo"));
        System.out.println(zonedDateTime);

        // 当前区域系统时间
        ZonedDateTime zonedDateTime1 = instant.atZone(ZoneId.systemDefault());

        // Duration是time-based implementation 的时间量
        // 时间量是一种累积值的概念 ,用于计算时长
        Duration between = Duration.between(instant, now1);
        System.out.println(between.getSeconds());


    }
}
