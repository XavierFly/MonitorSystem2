package com.yizhi.monitorsystem2.collection.util;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.properties.LogProperties;
import com.yizhi.monitorsystem2.collection.properties.TimeProperties;

public class TimeAndLogUtil {
    private static Logger logger = LoggerFactory.getLogger(TimeAndLogUtil.class);

    private static String COMMON_TIME_FORMAT;
    private static String HOUR_TIME_FORMAT;
    private static String MID_NIGHT;

    private static String NGINX_LOG_PATH;
    private static String NGINX_LOG_SEPARATOR;
    private static String NGINX_LOG_FORMAT;
    private static String NGINX_LOG_TIME_FORMAT;
    private static String NGINX_LOG_TIME_SIMPLE_FORMAT;
    private static String NGINX_LOG_TIME_FORMAT_ON_HOUR;

    private static String TOMCAT_LOG_PATH;
    private static String TOMCAT_LOG_SEPARATOR;
    private static String TOMCAT_LOG_FORMAT;
    private static String TOMCAT_LOG_TIME_FORMAT;
    private static String TOMCAT_LOG_TIME_SIMPLE_FORMAT;
    private static String TOMCAT_LOG_TIME_FORMAT_ON_HOUR;

    public static void setTimeUtilProperties(TimeProperties timeProperties) {
        TimeAndLogUtil.COMMON_TIME_FORMAT = timeProperties.getCommonTimeFormat();
        TimeAndLogUtil.HOUR_TIME_FORMAT = timeProperties.getHourTimeFormat();
        TimeAndLogUtil.MID_NIGHT = timeProperties.getMidNight();

        TimeAndLogUtil.NGINX_LOG_TIME_FORMAT = timeProperties.getNginxLogTimeFormat();
        TimeAndLogUtil.NGINX_LOG_TIME_SIMPLE_FORMAT = timeProperties.getNginxLogTimeSimpleFormat();
        TimeAndLogUtil.NGINX_LOG_TIME_FORMAT_ON_HOUR = timeProperties.getNginxLogTimeFormatOnHour();

        TimeAndLogUtil.TOMCAT_LOG_TIME_FORMAT = timeProperties.getTomcatLogTimeFormat();
        TimeAndLogUtil.TOMCAT_LOG_TIME_SIMPLE_FORMAT = timeProperties.getTomcatLogTimeSimpleFormat();
        TimeAndLogUtil.TOMCAT_LOG_TIME_FORMAT_ON_HOUR = timeProperties.getTomcatLogTimeFormatOnHour();
    }

    public static void setTimeUtilProperties(LogProperties logProperties) {
        TimeAndLogUtil.NGINX_LOG_PATH = logProperties.getNginxLogPath();
        TimeAndLogUtil.NGINX_LOG_SEPARATOR = logProperties.getNginxLogSeparator();
        TimeAndLogUtil.NGINX_LOG_FORMAT = logProperties.getNginxLogFormat();

        TimeAndLogUtil.TOMCAT_LOG_PATH = logProperties.getTomcatLogPath();
        TimeAndLogUtil.TOMCAT_LOG_SEPARATOR = logProperties.getTomcatLogSeparator();
        TimeAndLogUtil.TOMCAT_LOG_FORMAT = logProperties.getTomcatLogFormat();
    }

    /**
     * 判断是不是半夜12点
     * @param timestamp 时间戳
     * @return 判断结果
     * @author xavierw
     */
    public static boolean isMidNight(long timestamp) {
        return timestampToTime(timestamp, COMMON_TIME_FORMAT).contains(MID_NIGHT);
    }

    /**
     * 判断是不是半夜12点
     * @param time 时间
     * @return 判断结果
     * @author xavierw
     */
    public static boolean isMidNight(String time) {
        return time.contains(MID_NIGHT);
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
     * 获取当前时间
     * @return 当前时间
     * @author xavierw
     */
    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
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
     * 增加或减去自定义天数
     * @param time 原时间
     * @param format 时间格式
     * @param day 天数
     * @return 减一天后的时间
     * @throws BaseException 基础异常
     * @author xavierw
     */
    private static String addOrDeductDay(String time, int day, String format) throws BaseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            logger.error(e.toString(), e);
            throw new BaseException(e.toString());
        }
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取不同服务器类型对应的日志时间
     * @param serverType 服务器类型
     * @return 日志时间
     * @author xavierw
     */
    public static String getLogFilePath(int serverType) throws BaseException {
        long timestamp;
        String logPath;
        String logSeparator;
        String logFormat;
        String logTimeFormat;
        String logTimeSimpleFormat;
        String logTimeFormatOnTime;
        String defaultLog;
        switch (serverType) {
            case 1:
                logPath = NGINX_LOG_PATH;
                logSeparator = NGINX_LOG_SEPARATOR;
                logFormat = NGINX_LOG_FORMAT;
                logTimeFormat = NGINX_LOG_TIME_FORMAT;
                logTimeSimpleFormat = NGINX_LOG_TIME_SIMPLE_FORMAT;
                logTimeFormatOnTime = NGINX_LOG_TIME_FORMAT_ON_HOUR;
                timestamp = timeToTimestamp(getCurrentTime(logTimeFormat), logTimeFormat);
                defaultLog = logFormat;
                break;
            case 2:
                logPath = TOMCAT_LOG_PATH;
                logSeparator = TOMCAT_LOG_SEPARATOR;
                logFormat = TOMCAT_LOG_FORMAT;
                logTimeFormat = TOMCAT_LOG_TIME_FORMAT;
                logTimeSimpleFormat = TOMCAT_LOG_TIME_SIMPLE_FORMAT;
                logTimeFormatOnTime = TOMCAT_LOG_TIME_FORMAT_ON_HOUR;
                timestamp = timeToTimestamp(getCurrentTime(logTimeFormat), logTimeFormat);
                defaultLog = logSeparator + timestampToTime(timestamp, TOMCAT_LOG_TIME_SIMPLE_FORMAT) + TOMCAT_LOG_FORMAT;
                break;
            default:
                throw new BaseException("The server type is not existed");
        }

        try {
            String timeOnHour = timestampToTime(timestamp, logTimeFormatOnTime);
            if (isMidNight(timeOnHour)) {
                return logPath + logSeparator + addOrDeductDay(timeOnHour, -1, logTimeSimpleFormat) + logFormat;
            } else {
                return logPath + defaultLog;
            }
        } catch (BaseException e) {
            logger.error(e.toString(), e);
            return "";
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
            return String.valueOf(TimeAndLogUtil.timeToTimestamp(parsedDataString, COMMON_TIME_FORMAT));
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
