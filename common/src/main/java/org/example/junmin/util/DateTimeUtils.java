package org.example.junmin.util;

import java.time.*;
import java.util.Date;

/**
 * 时间转换工具类
 */
public class DateTimeUtils {

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    /**
     * LocalDateTime -> Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    /**
     * Date -> LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(DEFAULT_ZONE)
                .toLocalDateTime();
    }

    /**
     * LocalDate -> Date（当天 00:00:00）
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE).toInstant());
    }

    /**
     * Date -> LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(DEFAULT_ZONE)
                .toLocalDate();
    }

    /**
     * 获取当前时间（Date）
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 获取当前时间（LocalDateTime）
     */
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 时间戳 -> LocalDateTime
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                DEFAULT_ZONE
        );
    }

    /**
     * LocalDateTime -> 时间戳
     */
    public static long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0L;
        }
        return localDateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }
}
