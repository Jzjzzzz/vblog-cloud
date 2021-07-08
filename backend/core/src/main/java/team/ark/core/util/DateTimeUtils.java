package team.ark.core.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间日期工具类
 *
 * @author Ashinch
 * @date 2020/12/15
 */
@Slf4j
public class DateTimeUtils {
    /**
     * 日期时间格式化规则 yyyy-MM-dd HH:mm:ss
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期时间格式化规则 yyyy-MM-dd HH:mm
     */
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /**
     * 日期时间格式化规则 yyyy-MM-dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 日期时间格式化规则 yyyy-MM
     */
    public static final String YYYY_MM = "yyyy-MM";
    /**
     * 日期时间格式化规则 yyyy
     */
    public static final String YYYY = "yyyy";

    /**
     * 返回格式化后的日期时间字符串, 默认使用 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date 本地日期时间
     */
    public static String toString(Date date) {
        return toStringInternal(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回当前本地日期时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 返回格式化后的日期时间字符串
     *
     * @param date    本地日期时间
     * @param pattern 格式化规则
     */
    public static String toString(Date date, String pattern) {
        return toStringInternal(date, pattern);
    }

    /**
     * 返回格式化后的当前日期时间字符串, 默认使用 yyyy-MM-dd HH:mm:ss 格式
     */
    public static String nowToString() {
        return toStringInternal(now(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回格式化后的当前日期时间字符串, 默认使用 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param pattern 格式化规则
     */
    public static String nowToString(String pattern) {
        return toStringInternal(now(), pattern);
    }

    /**
     * 返回格式化后的时间戳
     *
     * @param date 本地日期时间
     */
    public static long toTimestamp(Date date) {
        return date.getTime();
    }

    /**
     * 返回当前时间戳
     */
    public static long nowToTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 返回格式化后的IOS日期时间字符串格式
     *
     * @param dateString 原日期时间字符串
     */
    public static String formatIosDateTime(String dateString) {
        return dateString.replace("-", "/");
    }

    /**
     * 返回转换后的Date
     *
     * @param timestamp 时间戳
     */
    public static Date toDate(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 返回转换后的Date, 默认时间格式为 yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 日期时间格式字符串
     */
    public static Date toDate(String dateString) {
        return toDateInternal(dateString, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回转换后的Date, 指定格式化规则
     *
     * @param dateString 日期时间格式字符串
     * @param pattern    格式化规则
     */
    public static Date toDate(String dateString, String pattern) {
        return toDateInternal(dateString, pattern);
    }

    private static String toStringInternal(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    private static Date toDateInternal(String dateString, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException e) {
            log.error("日期时间转换异常 dateString:[{}] pattern:[{}]", dateString, pattern);
        }
        return null;
    }
}
