package cn.nongph.components.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * 
 * @author lisong
 */
public class DateUtil {

	/** 日期格式 **/
	public static final String DATE_PATTERN_HH_MM_SS = "HH:mm:ss";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static String date2String(Date date) {
		return date2String(date, DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
	}

	public static String date2String(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date string2Date(String dateString) {
		return string2Date(dateString, DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
	}

	public static Date string2Date(String dateString, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		ParsePosition pos = new ParsePosition(0);
		return format.parse(dateString, pos);
	}
	
	public static Date getMonthStartDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getMonthEndDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}
	
	public static Date getDayStartDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getDayEndDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, -1);
		return cal.getTime();
	}
	
	public static Date addMonth(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}

	public static Date addDay(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, amount);
		return cal.getTime();
	}

	public static Date addHour(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, amount);
		return cal.getTime();
	}

	public static Date addMinute(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, amount);
		return cal.getTime();
	}
	
}
