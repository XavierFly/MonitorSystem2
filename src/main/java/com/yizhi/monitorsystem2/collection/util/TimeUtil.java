package com.yizhi.monitorsystem2.collection.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.properties.TimeProperties;

public class TimeUtil {
    private static Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    @Autowired
    private static TimeProperties timeProperties;

    /**
     * 获取当前小时
     * @return 当前小时 (yyyy-MM-dd HH:00:00)
     * @author xavierw
     */
    public static String getCurrentHourTime() {
        return new SimpleDateFormat(timeProperties.getHourTimeFormat()).format(new Date());
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
     * 获取当前小时时间戳
     * @return 当前小时时间戳
     * @author xavierw
     */
    public static long getCurrentHourTimestamp() throws BaseException {
        try {
            return timeToTimestamp(getCurrentHourTime(), timeProperties.getCommonTimeFormat());
        } catch (BaseException e) {
            throw new BaseException(e.toString());
        }
    }
}
