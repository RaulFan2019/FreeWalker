package cn.yy.freewalker.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/10 13:46
 */
public class TimeU {

    private static final String TAG = "TimeU";

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;

    public static final String FORMAT_TYPE_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TYPE_2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_TYPE_3 = "yyyy-MM-dd";
    public static final String FORMAT_TYPE_4 = "dd";
    public static final String FORMAT_TYPE_5 = "yyyy年MM月dd日 HH时mm分";
    public static final String FORMAT_TYPE_6 = "yyyy.MM.dd";
    public static final String FORMAT_TYPE_7 = "MM月dd日";
    public static final String FORMAT_TYPE_8 = "yyyy.MM.dd HH:mm";
    public static final String FORMAT_TYPE_9 = "yyyyMMddhhmmss";



    /**
     * 获取当前时间的描述字符串
     *
     * @param format 字符串格式
     * @return 格式化后结果
     */
    public static String nowTimeStr(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 获取当前时间的描述字符串
     *
     * @param format 字符串格式
     * @return 格式化后结果
     */
    public static String timeToTimeStr(final long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * 时间字符串格式转成date
     *
     * @param timeStr 时间字符串
     * @param format 格式化
     */
    public static Date timeStrToDate(String timeStr, final String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间变成时间字符串
     *
     * @param date 时间
     * @param format 格式化
     */
    public static String dateToTimeStr(final Date date, final String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 获取今日时间戳
     * @return
     */
    public static long getTodayTimeStamp(){
        long zeroTimestamp = System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000));
        return zeroTimestamp;
    }

    /**
     * 获取当前时间的String(HH:mm)
     *
     * @return 刷新时间
     */
    public static String refreshTime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

}
