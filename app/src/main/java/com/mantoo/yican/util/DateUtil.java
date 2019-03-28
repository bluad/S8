package com.mantoo.yican.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * Created by Administrator on 2017/10/12.
 */

public class DateUtil {


    public static final String DATA_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String TIME_PATTERN_HM = "HH:mm";
    public static final String DATE_TIME_PATTERN = DATA_PATTERN + " "
            + TIME_PATTERN;
    public static final String DATE_TIME_PATTERN_HM = DATA_PATTERN + " "
            + TIME_PATTERN_HM;

    /**
     * 默认构造方法
     */
    private DateUtil() {
    }

    /**
     * 日期时间显示两位数的方法
     */
    public static String format(final int x) {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    /**
     * 根据默认格式获取当前时间
     */
    public static Calendar getToday(final Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            String todayAsString = df.format(date);
            Calendar cal = new GregorianCalendar();
            cal.setTime(strtoDate(todayAsString));
            df = null;
            return cal;
        } else {
            return null;
        }
    }

    /**
     * 根据默认格式获取当前时间
     */
    public static String getCurrentTimeStr(final Date date) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (date != null) {
            df = new SimpleDateFormat(DATE_TIME_PATTERN);
            returnValue = df.format(date);
            df = null;
        }
        return (returnValue);
    }

    /**
     * 获取两个时间段的时间差（单位秒）
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static long getTimeDistance(String beginTime, String endTime) {
        long distance = 0l;
        SimpleDateFormat dfs = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            Date begin = dfs.parse(beginTime);
            Date end = dfs.parse(endTime);

            distance = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return distance;
    }

    /**
     * 根据日期格式，返回日期按DATA_PATTERN格式转换后的字符串
     */
    public static String getDate(final Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(TIME_PATTERN);
            returnValue = df.format(aDate);
            df = null;
        }
        return (returnValue);
    }

    /**
     * 根据日期格式，返回日期按TIME_PATTERN格式转换后的字符串
     */
    public static String getTimeStr(String dateStr) {
        Date date = convertStringToDate(DATE_TIME_PATTERN, dateStr);
        String returnValue = "";
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        returnValue = df.format(date);
        return returnValue;
    }

    /**
     * 将日期对象转换成代年日月时分的字符串格式
     *
     * @param date
     *            日期类型
     * @return 返回类型为String 格式为yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(final Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
        String returnValue = "";
        if (date != null) {
            returnValue = df.format(date);
        }
        df = null;
        return (returnValue);
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     * @param aMask
     *            输入字符串的格式
     * @param strDate
     *            一个按aMask格式排列的日期的字符串描述
     * @return Date 对象
     */
    public static Date convertStringToDate(final String aMask,
                                           final String strDate) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            df = new SimpleDateFormat(DATA_PATTERN);
            try {
                date = df.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } finally {
            df = null;
        }
        return (date);
    }

    /**
     * 根据默认格式获取当前时间
     *
     * @param theTime
     *            当前时间
     * @return 格式化的时间
     */
    public static String getTimeNow(final Date theTime) {
        return getTimeNow(theTime, DATE_TIME_PATTERN);
    }

    /**
     * 根据格式获取当前时间
     *
     * @param theTime
     *            当前时间
     * @param format
     *            格式化
     * @return 格式化的时间
     */
    public static String getTimeNow(final Date theTime, final String format) {
        return getDateTime(format, theTime);
    }

    /**
     * 根据给定格式格式化日期
     *
     * @param aMask
     *            参数模板为用户自定义模板例如:yyyy-MM-dd HH:mm:SS
     * @param aDate
     *            日期对象
     * @return 返回一个根据模板将日期类型转换的字符串
     *
     * @see java.text.SimpleDateFormat
     */
    public static String getDateTime(final String aMask, final Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        df = null;
        return (returnValue);
    }

    /**
     * 根据日期格式，返回日期按DATA_PATTERN格式转换后的字符串
     *
     * @param date
     *            日期
     * @return 格式转换后的字符串
     */
    public static String dateToStr(final Date date) {
        return getDateTime(DATA_PATTERN, date);
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     * @param strDate
     *            (格式 yyyy-MM-dd HH:mm:ss)
     * @return 日期对象
     *
     */
    public static Date strtoDate(final String strDate) {
        return convertStringToDate(DATE_TIME_PATTERN, strDate);
    }

    /**
     * 按照日期格式，将字符串解析为日期对象
     *
     */
    public static Date strtoDate(final String strDate, String format) {
        return convertStringToDate(format, strDate);
    }

    /**
     * 将秒换算为mm:ss形式
     */
    public static String timeToString(final long duration) {
        if (duration < 0)
            return "00:00";
        StringBuffer sb = new StringBuffer();
        long m = duration / 60;
        if (m < 10) {
            sb.append("0" + m);
        } else {
            sb.append(m);
        }
        sb.append(":");
        long s = duration % 60;
        if (m < 10) {
            sb.append("0" + s);
        } else {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 一个日期型，减去或加上一个整形，求出日期结果
     */
    public static String dateNum(final String dateFormate, final int type,
                                 final int num) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(type, num);
        java.util.Date date = cal.getTime();
        return getDateTime(dateFormate, date);
    }

    /**
     * 功能:计算两个日期相隔的具体时间
     */
    public static String getBetweenDayTime(final Date beginTime,
                                           final Date endtime) {
        if (beginTime == null || endtime == null) {
            return "0天";
        }
        long l = endtime.getTime() - beginTime.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuffer sb = new StringBuffer();
        if (day != 0) {
            sb.append(day + "天");
        }
        if (hour != 0) {
            sb.append(hour + "小时");
        }
        if (min != 0) {
            sb.append(min + "分");
        }
        if (s != 0) {
            sb.append(s + "秒");
        }
        return sb.toString();
    }

    /**
     * 返回两个日期相差多少秒
     *
     */
    public static long getSeconds(final Date beginTime, final Date endtime) {
        if (beginTime.getTime() >= endtime.getTime()) {
            return 0;
        } else {
            long l = endtime.getTime() - beginTime.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            return Math.abs(day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        }
    }

    /**
     * 跟定一个字符串，获取其分钟数
     */
    /**
     * 获得给定日期所在的星期几
     *
     * @param date
     *            给定日期
     * @return 整数类型
     */
    public static int getWeekForDate(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;

    }

    /**
     * 获得给定日期所在的星期几
     *
     * @param gc
     *            给定日期
     * @return 整数类型
     */
    public static int getWeekForDate(final Calendar gc) {
        return gc.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取当前日期是星期几
     *
     * @param date
     *            给定日期
     * @return 当前日期是星期几
     */
    public static String getWeekForDate2Str(final Date date) {
        if (date != null) {
            String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                    "星期六" };
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } else {
            return "";
        }
    }

    /**
     * 将毫秒转换为日期
     */

    /**
     * 判断给定日期是否为今天。
     */

    /**
     * 计算时间差
     *
     * @param time1
     * @param time2
     * @return time2 - time1
     */
    public static long diffTimes(String time1, String time2) {

        // 格式化时间
        DateFormat df = new SimpleDateFormat("HH:mm");
        try {

            Date time11 = df.parse(time1);
            Date time22 = df.parse(time2);
            // 时间差（分） = 到达时间 - 离开时间
            long mins = (time22.getTime() - time11.getTime());
            // long mins = (time22.getTime() - time11.getTime()) / 60000;
            return mins;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;

    }

    public static long diffDay(String day1, String day2) {

        // 格式化时间
        DateFormat df = new SimpleDateFormat(DATA_PATTERN);
        try {

            Date time11 = df.parse(day1);
            Date time22 = df.parse(day2);
            // 时间差（分） = 到达时间 - 离开时间
            long mins = (time22.getTime() - time11.getTime());
            // long mins = (time22.getTime() - time11.getTime()) / 60000;
            return mins;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * 判断时间是否过期
     *
     * @param resourceTime
     * @param targetTime
     * @param spaceTime
     *            时间间隔
     * @return
     */
    public static boolean isTimeOutDate(String resourceTime, String targetTime,
                                        long spaceTime) {
        boolean mFlag = false;
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            long mStartTime = format.parse(resourceTime).getTime();
            long mEndTime = format.parse(targetTime).getTime();

            if (mStartTime - mEndTime > spaceTime) {
                mFlag = true;
            } else {
                mFlag = false;
            }

        } catch (ParseException e) {
            mFlag = true;
            e.printStackTrace();
        }
        return mFlag;
    }

    public static int getMonth() {
        // TODO Auto-generated method stub
        Calendar c = Calendar.getInstance();//
        int  mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        return mMonth;
    }


    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

    public static String clanderTodatetime(Calendar calendar, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());
    }

    public static String DateTotime(long date, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }

}
