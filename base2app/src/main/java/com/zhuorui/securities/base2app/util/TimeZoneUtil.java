package com.zhuorui.securities.base2app.util;

import android.text.TextUtils;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Create by xieyingwu on 2018/8/29
 * 时区util
 */
public class TimeZoneUtil {
    private TimeZoneUtil() {
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String currentTime(String format) {
        return timeFormat(currentTimeMillis(), format);
    }

    public static int getTimeOffset() {
        Calendar instance = Calendar.getInstance();
        int zoneOffset = instance.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = instance.get(java.util.Calendar.DST_OFFSET);
        int offsetMills = zoneOffset + dstOffset;
        return offsetMills / 1000 / 60 / 60;
    }

    /**
     * 字符串时间格式转化
     *
     * @param time      字符串时间
     * @param oldFormat 原时间格式
     * @param newFormat 新时间格式
     * @return 返回新的时间格式，发生错误，原字符串返回
     */
    public static String timeFormat(String time, String oldFormat, String newFormat) {
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(oldFormat) || TextUtils.isEmpty(newFormat)) return "";
        Date sDate = null;
        try {
            sDate = new SimpleDateFormat(oldFormat).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate == null ? time : new SimpleDateFormat(newFormat).format(sDate);
    }


    /**
     * 时间格式转化
     *
     * @param timeMillis 毫秒
     * @param format     时间格式
     * @return 返回新的时间格式，发生错误，原字符串返回
     */
    public static String timeFormat(long timeMillis, String format) {
        return new SimpleDateFormat(format).format(new Date(timeMillis));
    }

    /**
     * 和今天日期比较大小
     *
     * @param date       要比较日期
     * @param dateFormat 时间格式
     * @return 1 日期比今天大 ；0 日期和今天相同；-1 日期比今天小
     */
    public static int dateCompareToday(String date, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String today = format.format(new Date(System.currentTimeMillis()));
        return compareToTime(date, dateFormat, today, dateFormat);
    }


    /**
     * 比较两个时间大小
     *
     * @param date1       时间1
     * @param date1Format 时间1格式
     * @param date2       时间2
     * @param date2Format 时间2格式
     * @return 1 date1>date2 ；0 date1=date2；-1 date1<date2
     */
    public static int compareToTime(String date1, String date1Format, String date2, String date2Format) {
        try {
            Date d1 = new SimpleDateFormat(date1Format).parse(date1);
            Date d2 = new SimpleDateFormat(date2Format).parse(date2);
            if (d1.getTime() > d2.getTime()) {
                return 1;
            } else if (d1.getTime() < d2.getTime()) {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间字符串转成毫秒时间戳
     *
     * @param time
     * @param format
     * @return
     */
    public static long parseTime(String time, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断出生时间到今天是否成年（18岁）
     *
     * @param birthDay   出生日期
     * @param timeFormat 时间格式
     * @return
     */
    public static boolean isAdulthood(String birthDay, String timeFormat) {
        try {
            Date birthDate = new SimpleDateFormat(timeFormat).parse(birthDay);
            Calendar tms = Calendar.getInstance();
            tms.setTime(birthDate);
            int y = tms.get(Calendar.YEAR) + 18;
            //成年日期
            String adulthoodDate = String.format("%d-%02d-%02d", y, tms.get(Calendar.MONTH) + 1, tms.get(Calendar.DAY_OF_MONTH));
            return dateCompareToday(adulthoodDate, "yyyy-MM-dd") < 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据当前时间增减天数
     *
     * @param day
     * @return
     */
    public static long addDayByCurrentTime(int day) {
        return addDay(currentTimeMillis(), day);
    }

    /**
     * 增减天
     *
     * @param startTimeMillis
     * @param day
     * @return
     */
    public static long addDay(long startTimeMillis, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimeMillis);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

    /**
     * 根据当前时间增减月数
     *
     * @param month
     * @return
     */
    public static long addMonthByCurrentTime(int month) {
        return addMonth(currentTimeMillis(), month);
    }

    /**
     * 增减月
     *
     * @param month
     * @return
     */
    public static long addMonth(long startTimeMillis, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimeMillis);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTimeInMillis();
    }

}
