/**
 * All rights Reserved, Designed By MiGu
 * Copyright: Copyright(C) 2016-2020
 * Company MiGu Co., Ltd.
 */
package cn.rmy.common.redisUtils;


import cn.rmy.common.redisUtils.concasts.CommonConst;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类作用：日期工具类. 项目名称:common
 * 包: com.migu.redstone.microservice.common.cn.rmy.util
 * 类名称: DateCommonUtil
 * 类描述: 日期工具类
 * 创建人: jianghao
 * 创建时间: 2017/7/27 18:05
 */
public class DateCommonUtil {
    /**
     * .
     */
    public static final int CURRENT_DAY = 1;
    /**
     * .
     */
    public static final int CURRENT_WEEK = 2;
    /**
     * .
     */
    public static final int CURRENT_MONTH = 3;
    /**
     * .
     */
    public static final int CURRENT_QUARTER = 4;
    /**
     * .
     */
    public static final int CURRENT_RANGE = 5;
    /**
     * .
     */
    public static final String CURRENT_TOTAL = "TOTAL";

    /**
     * 用于首次必中的缓存
     */
    public static final String USER_JOIN_CNT = "USER_JOIN_CNT";



    /****
     * 传入具体日期 ，返回具体日期增加一个月。  yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date subMonth(Date date,int n) {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.DATETIME_FORMAT);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, n);
        Date dt1 = rightNow.getTime();
        return dt1;
    }







    /**
     * 获取今年是哪一年. 〈一句话功能简述〉 〈功能详细描述〉
     *
     * @return 1
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }



    /**
     * 按照指定的格式返回格式化日期. 功能详细描述
     *
     * @param date       参数1说明
     * @param dateFormat 参数2说明
     * @return 返回类型说明
     */
    public static String getFormatDateStr(final Date date, String dateFormat) {

        if (date == null) {
            return "";
        }

        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = CommonConst.DATEFORMAT.DATETIME_FORMAT;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }



    /**
     * 字符串转日期. @param str 日期的字符串形式 @param paten 转换为日期的格式 @return 本月结束时间 @throws
     */
    public static Date stringToDate(final String str, final String paten) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(paten);
        sdf.setLenient(CommonConst.NumberBool.FALSE_CONST);
        Date date = sdf.parse(str);
        return date;
    }

    /**
     * 字符串转日期.
     *
     * @param date  要转换的日期
     * @param paten 转换为日期的格式
     * @return 本月结束时间
     */
    public static String dateToString(final Date date, final String paten) throws ParseException {
        if (CommonUtil.isNull(date) || StringUtils.isBlank(paten)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(paten);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 获取当前时间.
     *
     * @return 当前时间
     */
    public static Date getCurrentDate() {
        Date currentTime = new Date();
        // java.sql.Timestamp time = new
        // Timestamp(System.currentTimeMillis());
        return currentTime;
    }

    /**
     * 根据小时时间段获取当日时间段.
     *
     * @param dayTimeRange 时间段 1:00:00,3:00:00
     * @return 当前时间段的字符串
     */
    public static String getCurrentDayTimeRange(final String dayTimeRange) {
        StringBuffer buffer = new StringBuffer();
        String[] times = dayTimeRange.split(CommonConst.RegExp.SPLIT_BY_COMMA);
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.DAY_FORMAT);
        String str = sdf.format(new Date());
        buffer.append(str).append(CommonConst.OrderWay.SPACE).append(times[0]).append(CommonConst.RegExp.SPLIT_BY_COMMA)
                .append(str).append(CommonConst.OrderWay.SPACE).append(times[1]);
        return buffer.toString();
    }

    /**
     * 根据日时间段获取当月日时间段.
     *
     * @param dayRange 日时间段 9,18
     * @return 当前时间段的字符串
     */
    public static String getCurrentMonthDayRange(final String dayRange) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String[] days = dayRange.split(CommonConst.RegExp.SPLIT_BY_COMMA);
        String startTime = "";
        String endTime = new SimpleDateFormat(CommonConst.DATEFORMAT.YYYY_MM).format(new Date())
                + days[1];
        if (days[0].length() == 1) {
            startTime = new SimpleDateFormat(CommonConst.DATEFORMAT.YYYY_MM).format(new Date())
                    + "0" + days[0];
        } else if (days[0].length() == 2) {
            startTime = new SimpleDateFormat(CommonConst.DATEFORMAT.YYYY_MM).format(new Date())
                    + days[0];
        } else {
            throw new Exception();
        }
        buffer.append(startTime).append(" 00:00:00").append(CommonConst.RegExp.SPLIT_BY_COMMA).append(endTime)
                .append(" 23:59:59");
        return buffer.toString();
    }

    /**
     * 根据指定时间段. 根据指定时间段(年月日)获取详细时间段(年月日时分秒)
     *
     * @param timeRange 日时间段
     * @return 当前时间段的字符串
     */
    public static String getTimeRange(final String timeRange) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String[] days = timeRange.split(CommonConst.RegExp.SPLIT_BY_COMMA);
        buffer.append(days[0]).append(" 00:00:00").append(CommonConst.RegExp.SPLIT_BY_COMMA).append(days[1])
                .append(" 23:59:59");
        return buffer.toString();
    }

    /**
     * 根据指定时间段. 根据指定时间段(年月日)获取详细时间段(年月日时分秒)
     *
     * @param timeRange 日时间段
     * @return 当前时间段的字符串
     */
    public static String getCampTimeRange(final String timeRange) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String[] days = timeRange.split(CommonConst.RegExp.SPLIT_BY_COMMA);
        buffer.append(days[0]).append(" 00:00:00").append(CommonConst.RegExp.SPLIT_BY_COMMA).append(days[1])
                .append(" 23:59:59");
        return buffer.toString();
    }


    /**
     * 根据时间获取第几个周
     *
     * @param dateStr 如20170812
     * @return 第N周
     * @throws ParseException
     */
    public static int getWeekOfYear(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        format.setLenient(CommonConst.NumberBool.FALSE_CONST);
        date = format.parse(dateStr);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 根据当前时间获取第几个周
     *
     * @param
     * @return 第N周
     */
    public static int getWeekOfYearByNow() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 根据当前时间获取第几个月
     *
     * @param
     * @return 第N月
     */
//    public static int getMonthOfYearByNow() {
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int month = calendar.get(Calendar.MONTH) + MktCampaignConst.Number.ONE;
//        return month;
//    }

    /**
     * 根据时间获取第几个月
     *
     * @param dateStr 如20171013
     * @return 第N月
     * @throws ParseException
     */
//    public static int getMonthOfYear(String dateStr) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//        Date date = null;
//        int month = 0;
//        format.setLenient(CommonConst.NumberBool.FALSE_CONST);
//        date = format.parse(dateStr);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        month = calendar.get(Calendar.MONTH) + MktCampaignConst.Number.ONE;
//        return month;
//    }

    /**
     * 〈获取当天时间以yyyyMMddHHmmss格式返回字符串〉 〈获取当天时间以yyyyMMdd格式返回字符串〉
     *
     * @return 当天时间
     */
    public static String getCurCurrentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.TIMESTAMP_FORMAT);
        String nowDate = sdf.format(getCurrentDate());
        return nowDate;
    }

    public static Date getCurCurrentDateStrToDate() throws ParseException {
        return new SimpleDateFormat(CommonConst.DATEFORMAT.TIMESTAMP_FORMAT).parse(getCurCurrentDateStr());
    }

    /**
     * 〈获取当天时间以yyyyMMdd格式返回字符串〉 〈获取当天时间以yyyyMMdd格式返回字符串〉
     *
     * @return 当天时间
     */
    public static String getDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.YYYYMMDD);
        String nowDate = sdf.format(date);
        return nowDate;
    }

    /**
     *〈返回字符串格式日期〉.
     *〈根据传入日期和格式返回字符串格式日期〉
     * @param date     [日期]
     * @param format   [字符串格式]
     * @return String [字符串格式日期]
     * @author yangyuan
     */
    public static String getDateString(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowDate = sdf.format(date);
        return nowDate;
    }

    /**
     * 〈获取当天时间以yyMM格式返回字符串〉 〈获取当天时间以yyMM格式返回字符串〉
     *
     * @return 当天时间
     */
    public static String getCurCurrentMonthStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.MONTH_FORMAT);
        String nowDate = sdf.format(getCurrentDate());
        return nowDate;
    }

    /**
     * 〈获取当天时间以yyyyMM格式返回字符串〉 〈获取当天时间以yyyyMM格式返回字符串〉
     *
     * @return 当天时间
     */
    public static String getMonthString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConst.DATEFORMAT.MON_FORMAT);
        String nowDate = sdf.format(date);
        return nowDate;
    }


    /**
     * 根据时间获取第多少周的字符串作为redis的key 比如：1707，表示17年第7周
     *
     * @param date [ date]
     * @return 第N月
     */
    public static String getHourStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHH");
        return format.format(date);
    }

    /**
     * 根据时间获取日期的字符串作为redis的key 返回：比如：171018，表示17年10月18日
     */
    public static String getDayStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        return format.format(date);
    }

    /**
     * 根据时间获取日期的字符串作为redis的key 返回：比如：20171018，表示2017年10月18日
     */
    public static String getDayString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 根据时间获取第多少周的字符串作为redis的key 返回：比如：1707，表示17年第7周
     *
     * @return 返回字符日期
     */
//    public static String getWeekStr(Date date) {
//        String returnStr = null;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//        calendar.setTime(date);
//        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
//        if (week.length() == MktCampaignConst.Number.ONE) {
//            returnStr = year.substring(2) + "0" + week;
//        } else {
//            returnStr = year.substring(2) + week;
//        }
//        return returnStr;
//    }

    /**
     * 根据时间获取第多少周的字符串作为redis的key 返回：比如：201707，表示2017年第7周
     * 返回格式：yyyyWeek
     * @return 返回字符日期
     */
//    public static String getWeekString(Date date) {
//        String returnStr = null;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//        calendar.setTime(date);
//        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
//        if (week.length() == MktCampaignConst.Number.ONE) {
//            returnStr = year + "0" + week;
//        } else {
//            returnStr = year + week;
//        }
//        return returnStr;
//    }

    /**
     * 根据时间获取第几个月的字符串作为redis的key 返回：比如：1710，表示17年10月份
     *
     * @return 返回字符日期
     */
//    public static String getMonthStr(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyMM");
//        return format.format(date);
//    }
//
//    public static Long calculateExpireTime(Date now, Date end) {
//        // 多给一个小时的时间，防止机器时间有误差
//        long expireSecond = ((end.getTime() - now.getTime()) / MktCampaignConst.Number.THOUSAND)
//                + MktCampaignConst.Number.ONE * MktCampaignConst.Number.SIXTY * MktCampaignConst.Number.SIXTY;
//
//        return expireSecond;
//    }
//
//    public static Long expireEndTime(Date endTime) {
//        // 多给一个小时的时间，防止机器时间有误差
//        long expireSecond = endTime.getTime() / CommonConst.LONG_THOUSAND + CommonConst.HOUR_SECOND;
//        return expireSecond;
//    }

    /**
     * 〈一句话功能简述〉. 〈功能详细描述〉
     *
     * @param args main入参
     * @throws ParseException
     */
   /* public static void main(String[] args) throws Exception {
        // System.out.println(getDayStartTime(stringToDate("2017-09","yyyy-MM")));
        // System.out.println(getDayEndTime(stringToDate("2017-09","yyyy-MM")));
        // System.out.println(getWeekDayRange("1,7"));
        *//*
         * System.out.println(getWeekOfYear("20171006"));
		 * System.out.println(getWeekOfYearByNow());
		 * System.out.println(getMonthOfYearByNow());
		 * System.out.println(getMonthOfYear("20171006"));
		 *//*

        // getWeekOfYearByNow

		*//*
		 * System.out.println("SSSSSSSSSSSSSSSSSSSS"+getHourStr(new Date()));
		 * System.out.println("SSSSSSSSSSSSSSSSSSSS"+getDayStr(new Date()));
		 * System.out.println("SSSSSSSSSSSSSSSSSSSS"+getWeekStr(new Date()));
		 * System.out.println("SSSSSSSSSSSSSSSSSSSS"+getMonthStr(new Date()));
		 *//*

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		*//* Calendar cal = Calendar.getInstance(); *//*
        // cal.set(Calendar.DAY_OF_MONTH, 10);
        Date start = sdf.parse("2017-12-20");
        // cal.set(Calendar.DAY_OF_MONTH, 12);
        Date end = sdf.parse("2018-01-10");
        List<String> stringList = getDaysFromTowDate(start, end, "11");
        System.out.println(stringList.toString());

        SimpleDateFormat sdfH = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startH = sdfH.parse("2017-12-31 10:12:12");
        Date endH = sdfH.parse("2018-01-02 02:11:11");
        List<String> stringListH = getHoursFromTowDate(startH, endH, "11");
        System.out.println(stringListH.toString());
    }*/

    /**
     * 计算两个时间段内yyyyMMdd片段 〈功能详细描述〉
     *
     * @param startDate [startDate]
     * @param endDate   [endDate]
     * @param prefix    ]prefix]
     * @return 返回类型说明
     */
    public static List<String> getDaysFromTowDate(Date startDate, Date endDate, String prefix) {

        if (CommonUtil.isNull(startDate) || CommonUtil.isNull(endDate) || startDate.after(endDate)) {
            return null;
        }
        if (StringUtils.isEmpty(prefix)) {
            prefix = "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        List<String> dateList = new ArrayList<>();
        while (cal.getTime().before(endDate)) {
            dateList.add(prefix + getDayStr(cal.getTime()));
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
        }
        return dateList;
		/*
		 * List<String> dateList = new ArrayList<>(); String startStr =
		 * getDayStr(startDate); String endStr = getDayStr(endDate);
		 * 
		 * Integer startInt = Integer.parseInt(startStr); Integer endInt =
		 * Integer.parseInt(endStr);
		 * 
		 * Integer days = Integer.parseInt(startStr.substring(4,6)); Integer month =
		 * Integer.parseInt(startStr.substring(2,4)); StringBuilder s; int maxDay = 31;
		 * for (Integer i = startInt; i < endInt ; i++) { dateList.add(prefix +
		 * i.toString()); days += 1; if (days > maxDay) { days = 0; s = new
		 * StringBuilder(i.toString()); s.replace(4,6, "00"); i =
		 * Integer.parseInt(s.toString()); month += 1; //月份进1 i += 100; if (month > 12)
		 * { month = 0; s.replace(2,4, "01"); i = Integer.parseInt(s.toString()); //年份进1
		 * i += 10000; } } } return dateList;
		 */

    }



    /**
     * 根据时间获取日期的字符串作为redis的key 返回：比如：1710181256，表示17年10月18日12点56分
     * 日期字符串格式转换：将格式为yyyy-MM-dd HH:mm:ss 转换为 yyMMddHHmm
     */
    public static String getMinuteStr(String dateString) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format1.setLenient(CommonConst.NumberBool.FALSE_CONST);
        Date date = format1.parse(dateString);
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
        return format.format(date);
    }

    /**
     * 获取时间段内的分钟数
     * 时间段格式为："yyyy-MM-dd HH:mm:ss，yyyy-MM-dd HH:mm:ss"
     */
    public static Long getMinutesFromRangeTimeStr(String rangeTimeStr) throws ParseException {
        String[] dateStrArr = rangeTimeStr.split(",");
        Long minutes = 0L;
        if (null != dateStrArr && dateStrArr.length == 2){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setLenient(CommonConst.NumberBool.FALSE_CONST);
            Date dateStart = format.parse(dateStrArr[0]);
            Date dateEnd = format.parse(dateStrArr[1]);
            minutes = (dateEnd.getTime() - dateStart.getTime())/(1000*60);
        }
        return minutes;
    }

    /**
     * 根据时间获取日期的字符串作为redis的key 返回：比如：1710181256，表示17年10月18日12点56分
     * 日期字符串格式转换：将格式为yyyy-MM-dd HH:mm:ss 转换为 yyMMdd
     */
    public static String getDayStr(String dateString) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format1.setLenient(CommonConst.NumberBool.FALSE_CONST);
        Date date = format1.parse(dateString);
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        return format.format(date);
    }

    /**
     * 获取时间段内的天数
     * 时间段格式为："yyyy-MM-dd HH:mm:ss，yyyy-MM-dd HH:mm:ss"
     */
    public static Long getDaysFromRangeTimeStr(String rangeTimeStr) throws ParseException {
        String[] dateStrArr = rangeTimeStr.split(",");
        Long days = 0L;
        if (null != dateStrArr && dateStrArr.length == 2){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(CommonConst.NumberBool.FALSE_CONST);
            Date dateStart = format.parse(dateStrArr[0]);
            Date dateEnd = format.parse(dateStrArr[1]);
            days = (dateEnd.getTime() - dateStart.getTime())/(1000*60*60*24);
        }
        return days;
    }

    /**
     * <判断当前时间是否在时间段内>.
     * <时间段格式为：yyyy-MM-dd HH:mm:ss，yyyy-MM-dd HH:mm:ss>
     * @param rangeTimeStr
     * @return
     */
    public static boolean timeIsInRange(String rangeTimeStr) throws ParseException {
        boolean flag = false;
        String[] dateStrArr = rangeTimeStr.split(",");
        if (null != dateStrArr && dateStrArr.length == 2){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setLenient(CommonConst.NumberBool.FALSE_CONST);
            Date now =  new Date();
            Date dateStart = format.parse(dateStrArr[0]);
            Date dateEnd = format.parse(dateStrArr[1]);
            if (now.after(dateStart) && now.before(dateEnd)){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取时间段
     * 返回2017-01-02-2017-12-25
     */
	public static String getDateInterval(Date startDate, Date endDate) throws ParseException {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat(CommonConst.DATEFORMAT.DAY_FORMAT);

		String dateStart = format.format(startDate);
		String dateEnd = format.format(endDate);
		return sb.append(dateStart).append(" ").append(CommonConst.RegExp.LINEAE).append(" ").append(dateEnd)
				.toString();
	}

	   /**
     * 计算几天前的时间的数组. 计算几天前的时间的数组.(月-日，如[0919,0920,0921]),包括当天
     *
     * @param days 天数
     * @return 返回类型说明
     */
    public static String[] getBeforeDaysArr(final int days) {
        Calendar cal = Calendar.getInstance();
        String[] day_cycle = new String[7];
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE, cal.get(Calendar.DATE));
        day_cycle[days - 1] = getFormatDateStr(cal.getTime(), "YYYYMMdd");
        for (int i = days - 2; i >= 0; i--) {
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
            day_cycle[i] = getFormatDateStr(cal.getTime(), "YYYYMMdd");
        }
        return day_cycle;
    }
    /**
     * 计算几月前的时间的数组.
     *
     * @param months 月数
     * @return 返回类型说明
     */
    public static String[] getBeforeMonthsArr(final int months) {
        Calendar cal = Calendar.getInstance();
        String[] month_cycle = new String[months];
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DATE, cal.get(Calendar.DATE));
        month_cycle[months - 1] = getFormatDateStr(cal.getTime(), "yyyyMM");
        for (int i = months - 2; i >= 0; i--) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            month_cycle[i] = getFormatDateStr(cal.getTime(), "yyyyMM");
        }
        return month_cycle;
    }

    public static int getDaysBetween (Calendar d1, Calendar d2) {
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
            return 0;
            /*java.cn.rmy.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;*/
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
    * <getDayInWeek>.
    * <获取当前时间是星期几>
    * @return [返回当前时间字符串形式]
    * @author jianghao
    */
    public static String getDayInWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekIndex = cal.get(Calendar.DAY_OF_WEEK)
                - 1;
        if (weekIndex < 0) {
            weekIndex = 0;
        }
        if (cal.get(
            Calendar.DAY_OF_WEEK) == 1) {
            weekIndex = 7;
        }
        String weekIndexStr = String.valueOf(weekIndex);
        return weekIndexStr;
    }

    public static boolean checkDateTimePeriod(String dateRange,
        Date now, String dateFormat) throws ParseException {
        boolean flag = false;
        if (StringUtils.isBlank(dateRange) || StringUtils.isBlank(dateFormat) || now == null) {
            return flag;
        }
        String[] dates = dateRange.split(CommonConst.RegExp.SPLIT_BY_COMMA);
        Date startDate = null;
        Date endDate = null;
        if (dates.length == 2) {
            if (CommonConst.DATEFORMAT.DAY_FORMAT.equals(dateFormat)) {
                startDate = stringToDate(dates[0], CommonConst
                        .DATEFORMAT.DAY_FORMAT);
                endDate = stringToDate(dates[1], CommonConst.DATEFORMAT
                        .DAY_FORMAT);
                now = getSpecifyFormatDate(now, CommonConst.DATEFORMAT.DAY_FORMAT);
            } else if (CommonConst.DATEFORMAT.DATETIME_FORMAT.equals(dateFormat)) {
                startDate = stringToDate(dates[0], CommonConst
                        .DATEFORMAT.DATETIME_FORMAT);
                endDate = stringToDate(dates[1], CommonConst.DATEFORMAT
                        .DATETIME_FORMAT);
                now = getSpecifyFormatDate(now, CommonConst.DATEFORMAT.DATETIME_FORMAT);
            } else if(CommonConst.DATEFORMAT.HHMMSS_FORMAT.equals(dateFormat)) {
                startDate = stringToDate(dates[0], CommonConst
                        .DATEFORMAT.HHMMSS_FORMAT);
                endDate = stringToDate(dates[1], CommonConst.DATEFORMAT
                        .HHMMSS_FORMAT);
                now = getSpecifyFormatDate(now, CommonConst.DATEFORMAT.HHMMSS_FORMAT);
            } else {
                return flag;
            }
            if (now.getTime() >= startDate.getTime() && now.getTime() <= endDate.getTime()) {
                flag = true;
            }
        }
        return flag;
    }

	/**
	*<getSpecifyFormatDate>.
	*<获取指定格式的日期>
	* @param  date   [date]
	* @param  formatStr   [formatStr]
	* @return [返回指定格式的日期]
	* @exception/throws [ParseException] [ParseException]
	* @author jianghao
	*/
	public static Date getSpecifyFormatDate(Date date, String formatStr) throws ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
	    String specifyFormatDateStr = dateFormat.format(date);
	    return dateFormat.parse(specifyFormatDateStr);
	}

	/**
	*<checkDateIsExpire>.
	*<校验日期是否过期>
	* @param  effectiveDate   [effectiveDate]
	* @param  expireDate   [expireDate]
	* @param  tempDate   [tempDate]
	* @return [返回校验结果]
	* @exception/throws [Exception] [Exception]
	* @author jianghao
	*/
	public static boolean checkDateIsExpire(Date effectiveDate, Date expireDate,
	    Date tempDate) throws Exception {
	    if (CommonUtil.isNull(effectiveDate) || CommonUtil.isNull(expireDate)
	        || CommonUtil.isNull(tempDate)) {
            return false;
        } else {
            if (tempDate.getTime() < effectiveDate.getTime()
                || tempDate.getTime() > expireDate.getTime()) {
                return false;
            } else {
                return true;
            }
        }
	}

	/**
	*<getBeforeDay>.
	*<获取指定天数之前的日期>
	* @param  day   [day]
	* @return [返回指定天数之前的日期]
	* @exception/throws [Exception] [Exception]
	* @author jianghao
	*/
	public static Date getBeforeDay(int day) {
        Date date = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_YEAR,- day);
        Date  dBefore = calendar.getTime();
        return dBefore;
    }

	public static Date getBeforeDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_YEAR,- day);
        Date  dBefore = calendar.getTime();
        return dBefore;
    }
	/**
	*<checkIsSame>.
	*<比较两个日期是否相等>
	* @param  dateOne   [dateOne]
	* @param  dateTwo  [dateTwo]
	* @param  dateFormat  [dateFormat]
	* @return [返回校验结果]
	* @exception/throws [Exception] [Exception]
	* @author jianghao
	*/
	public static boolean checkIsSame(Date dateOne, Date dateTwo, String dateFormat) throws Exception {
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(CommonConst.NumberBool.FALSE_CONST);
        String dateOneStr = sdf.format(dateOne);
        String dateTwoStr =sdf.format(dateTwo);
        if (sdf.parse(dateOneStr).getTime() == sdf.parse(dateTwoStr).getTime()) {
            return true;
        } else {
            return false;
        }
	}

	/**
	*<addHourOfDay>.
	*<添加小时数>
	* @param  date   [date]
	* @param  hour  [hour]
	* @return [返回添加结果]
	* @author jianghao
	*/
	public static Date addHourOfDay(Date date, int hour) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
	}

	/*public static void main(String[] args) throws Exception {
	    Date date = new Date();
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 24);
        Date date2 = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sdf.format(date2));
	}*/
}
