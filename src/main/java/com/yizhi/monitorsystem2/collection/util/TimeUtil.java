package com.yizhi.monitorsystem2.collection.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.properties.TimeProperties;

public class TimeUtil {
    private static Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    private static String COMMON_TIME_FORMAT;
    private static String HOUR_TIME_FORMAT;

    public static void setConfigInfo(TimeProperties timeProperties) {
        TimeUtil.COMMON_TIME_FORMAT = timeProperties.getCommonTimeFormat();
        TimeUtil.HOUR_TIME_FORMAT = timeProperties.getHourTimeFormat();
    }

    /**
     * 判断是不是半夜12点
     * @param timestamp 时间戳
     * @return 判断结果
     * @author xavierw
     */
    public static boolean isMidNight(long timestamp) {
        return timestampToTime(timestamp, COMMON_TIME_FORMAT).contains("00:00:00");
    }

    /**
     * 获取当前小时
     * @return 当前小时 (yyyy-MM-dd HH:00:00)
     * @author xavierw
     */
    public static String getCurrentHourTime() {
        return new SimpleDateFormat(HOUR_TIME_FORMAT).format(new Date());
    }

    /**
     * 时间转时间戳
     * @param time 时间 (yyyy-MM-dd HH:mm:ss)
     * @return 时间戳 (long)
     * @throws BaseException 基础异常
     * @author xavierw
     */
    public static long timeToTimestamp(String time, String format) throws BaseException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            logger.error(e.toString(), e);
            throw new BaseException(e.toString());
        }
    }

    /**
     * 时间戳转时间
     * @param timestamp 时间戳 (long)
     * @return 时间
     */
    public static String timestampToTime(long timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(timestamp));
    }

    /**
     * 获取当前小时时间戳
     * @return 当前小时时间戳
     * @author xavierw
     */
    public static long getCurrentHourTimestamp() throws BaseException {
        try {
            return timeToTimestamp(getCurrentHourTime(), COMMON_TIME_FORMAT);
        } catch (BaseException e) {
            throw new BaseException(e.toString());
        }
    }

    /**
     * 解析日志记录时间
     * @param dateString 日志记录时间
     * @return 常用格式时间 (yyyy-MM-dd HH:mm:ss)
     * @throws BaseException 基础异常
     * @author xavierw
     */
    public static String parseSystemLogTime(String dateString) throws BaseException {
        try {
            int index = dateString.indexOf("/") + 1;
            String parsedDataString = dateString.substring(7, 11) + "-" +
                    parseMonthInNumber(dateString.substring(index, index + 3)) + "-" +
                    dateString.substring(0, 2) + " " +
                    dateString.substring(12, 20);
            return String.valueOf(TimeUtil.timeToTimestamp(parsedDataString, COMMON_TIME_FORMAT));
        } catch (BaseException e) {
            throw new BaseException(e.toString());
        }
    }

    /**
     * 将月份从英文转为数字
     * @param monthInEnglish 英文月份
     * @return 数字月份
     * @throws BaseException 基础异常
     * @author xavierw
     */
    private static String parseMonthInNumber(String monthInEnglish) throws BaseException {
        String month;
        switch (monthInEnglish) {
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                throw new BaseException("The month in english is not illegal");
        }
        return month;
    }
}
