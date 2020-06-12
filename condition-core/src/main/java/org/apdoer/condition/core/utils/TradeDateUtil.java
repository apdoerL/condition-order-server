package org.apdoer.condition.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TradeDateUtil {

    private TradeDateUtil() {
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    /**
     * @Author y
     * @Description 根据时间戳格式化月份
     * @Date 2020/4/2 4:42 下午
     * @Return
     **/
    public static String formatMonth(Long timestamp) {
        return dateFormat.format(timestamp);
    }

    /**
     * @Author y
     * @Description 根据Date格式化月份
     * @Date 2020/4/2 4:43 下午
     * @Return
     **/
    public static String formatMonth(Date date) {
        return dateFormat.format(date);
    }

    /**
     * @Author y
     * @Description 根据时间戳格式化日期
     * @Date 2020/4/29 10:18 上午
     * @Return
     **/
    public static String formatDay(Long timestamp) {
        return DAY_FORMAT.format(timestamp);
    }
}
