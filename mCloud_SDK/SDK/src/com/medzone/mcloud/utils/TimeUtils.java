package com.medzone.mcloud.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * TimeUtils
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

	public static final int					BIRTHDAY_INTERVAL_YEAR					= 120;

	public static SimpleDateFormat			yyyyMMCn								= new SimpleDateFormat("yyyy年MM月");
	private static SimpleDateFormat			MMCn									= new SimpleDateFormat("MM月");

	private static SimpleDateFormat			chatTimeFormat							= new SimpleDateFormat("MM月dd日 HH:mm");
	public static final SimpleDateFormat	DEFAULT_DATE_FORMAT						= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat	DATE_FORMAT_DATE						= new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat	YYYY_MM_DD_HH_MM_SS						= new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
	public static final SimpleDateFormat	MEASURE_RESULT_DETAILES_HISTORY_TIME	= new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static final SimpleDateFormat	YY_MM_DD								= new SimpleDateFormat("yyyy/MM/dd");
	public static final SimpleDateFormat	HHmmss									= new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat	HHmm									= new SimpleDateFormat("HH:mm");

	public static final SimpleDateFormat	MMDD_HHMMSS								= new SimpleDateFormat("MM/dd  HH:mm:ss");

	public static final SimpleDateFormat	YYYYMMDD_HHMMSS							= new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat	YYYYMM									= new SimpleDateFormat("yyyyMM");
	public static final SimpleDateFormat	YYYY									= new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat	MM										= new SimpleDateFormat("MM");
	public static final SimpleDateFormat	dd										= new SimpleDateFormat("dd");

	public static int getYear(long timeInMillis) {
		return Integer.valueOf(getTime(timeInMillis * 1000, YYYY));
	}

	public static int getMonth(long timeInMillis) {
		return Integer.valueOf(getTime(timeInMillis * 1000, MM));
	}

	public static Double getDay(long timeInMillis) {
		return Double.valueOf(getTime(timeInMillis * 1000, dd));
	}

	public static String getMonthToSecond(long timeInMillis) {
		return getTime(timeInMillis * 1000, MMDD_HHMMSS);
	}

	public static String getYearToSecond(long timeInMillis) {
		return getTime(timeInMillis * 1000, YYYY_MM_DD_HH_MM_SS);
	}

	@Deprecated
	public static String getYYYYMMDDHHMMSS(long timeInMillis) {
		return getTime(timeInMillis * 1000, YYYYMMDD_HHMMSS);
	}

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>>	sdfMap	= new HashMap<String, ThreadLocal<SimpleDateFormat>>();
	private static final Object									lockObj	= new Object();

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 * 
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					System.out.println("put new sdf of pattern " + pattern + " to map");

					// 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new
					// SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	public static Long getMillisecondDate(String time) {
		Args.notNull(time, "time");
		long millionSeconds = 0;
		try {
			Date date = getSdf("yyyyMMddHHmmss").parse(time);
			millionSeconds = date.getTime() / 1000;
			if (millionSeconds < 0) {
				Log.e("robert", "millionSeconds:" + millionSeconds);
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return millionSeconds;
	}

	public static Date getDate(String time) {
		Args.notNull(time, "time");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			Calendar c = Calendar.getInstance();
			date = sdf.parse(time);
			date.setYear(c.get(Calendar.YEAR));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDate(long millisecond, SimpleDateFormat dateFormat) {
		Args.notNull(dateFormat, "dateFormat");
		String date = dateFormat.format(new Date(millisecond));
		try {
			return dateFormat.parse(date);
		}
		catch (ParseException e) {
			throw new org.apache.http.ParseException("formatDate convert error.");
		}
	}

	public static Date getDate(String date, SimpleDateFormat dateFormat) {
		Args.notNull(date, "date");
		Args.notNull(dateFormat, "dateFormat");
		try {
			return dateFormat.parse(date);
		}
		catch (ParseException e) {
			throw new RuntimeException("Please make sure that the incoming birthday 'YYYY-MM-DD'!");
		}
	}

	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static int getMinBirthdayYear() {
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);
		int minYear = curYear - BIRTHDAY_INTERVAL_YEAR;
		return minYear;
	}

	/**
	 * 
	 * @return month which start from 0
	 */
	public static int getCurrentMonth() {

		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH);
	}

	public static int getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	@Deprecated
	public static String getShareMonthTime(String yyyyMM) {
		Date date = TimeUtils.getDate(yyyyMM, TimeUtils.YYYYMM);
		String formatDate;
		if (date.getYear() == new Date().getYear()) {
			formatDate = TimeUtils.getTime(date.getTime(), TimeUtils.MMCn);
		}
		else {
			formatDate = TimeUtils.getTime(date.getTime(), TimeUtils.yyyyMMCn);
		}
		return formatDate;
	}

	@Deprecated
	public static String getShareMonthTime(long seconds) {

		Date date = new Date(seconds);
		String formatDate;
		if (date.getYear() == new Date().getYear()) {
			formatDate = TimeUtils.getTime(date.getTime(), TimeUtils.MMCn);
		}
		else {
			formatDate = TimeUtils.getTime(date.getTime(), TimeUtils.yyyyMMCn);
		}
		return formatDate;
	}

	public static String getChatTime(long timeInMillis) {
		if (LocaleUtil.isEn()) {
			return MEASURE_RESULT_DETAILES_HISTORY_TIME.format(new Date(timeInMillis));
		}
		else {
			return chatTimeFormat.format(new Date(timeInMillis));
		}
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {

		// String example = "1409055697531";
		// String ret = String.valueOf(timeInMillis);
		// if (ret.length() < example.length() - 2) {
		// timeInMillis *= 1000;
		// Log.v("传入值以秒为单位，已经强制转化为毫秒级别！");
		// }
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 获取当前月下自然天数
	 * 
	 * @author Robert. 2014年9月11日
	 * 
	 * @return
	 */
	public static int getDayCountOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		int count = cal.getActualMaximum(Calendar.DATE);
		return count;
	}

	/**
	 * 获取指定月份下的自然天数
	 * 
	 * @author Robert. 2014年9月11日
	 * 
	 * @param month
	 *            从1开始计数，即1月份month为1。
	 */
	public static int getDayCountOfMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		int count = cal.getActualMaximum(Calendar.DATE);
		return count;
	}

	public static float getGapYearByBirthday(Date date) {

		float age = 0;

		if (date != null) {
			String birthday = getTime(date.getTime(), DATE_FORMAT_DATE);

			int yearLine = birthday.indexOf("-");
			int bornYear = Integer.valueOf(birthday.substring(0, yearLine));

			int monthLine = birthday.indexOf("-", yearLine + 1);
			int bornMonth = Integer.valueOf(birthday.substring(yearLine + 1, monthLine));

			int nowYear = Calendar.getInstance().get(Calendar.YEAR);
			int nowMonth = Calendar.getInstance().get(Calendar.MONTH);

			if (nowMonth < bornMonth) {
				nowYear--;
				nowMonth += 12;
			}
			age = (float) ((nowMonth - bornMonth) / 12.0);
			age += nowYear - bornYear;

			age = (float) RandomUtils.round(age, 1, BigDecimal.ROUND_UP);
		}

		return age;
	}

	public static Long getDaysBetween(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 当月第一天
	 * 
	 * @return
	 */
	public static String getFirstDay() {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();

		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		return str.toString();

	}

	/**
	 * 当月最后一天
	 * 
	 * @return
	 */
	public static String getLastDay() {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		String s = df.format(theDate);
		StringBuffer str = new StringBuffer().append(s);
		return str.toString();

	}

	/**
	 * 当月天数
	 * 
	 * @return
	 */
	public static int getLastDayNO() {
		SimpleDateFormat df = new SimpleDateFormat("dd");
		Calendar calendar = Calendar.getInstance();
		Date theDate = calendar.getTime();
		String s = df.format(theDate);
		StringBuffer str = new StringBuffer().append(s);
		return Integer.valueOf(str.toString());

	}

	/**
	 * 某一个月第一天
	 * 
	 * @param date
	 *            yyyy-MM
	 * @return
	 */
	public static String getFirstdayMonth(String yearMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = sdf.parse(yearMonth);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat df = new SimpleDateFormat("MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date theDate = calendar.getTime();

		// 上个月第一天
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();
		return day_first;
	}

	/**
	 * 某一个月最后一天
	 * 
	 * @param date
	 *            yyyy-MM
	 * @return
	 */
	public static String getLastdayMonth(String yearMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = sdf.parse(yearMonth);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat df = new SimpleDateFormat("MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();
		return day_last;
	}

	/**
	 * 某一个月的天数
	 * 
	 * @param date
	 *            yyyy-MM
	 * @return
	 */
	public static int getLastdayNOMonth(String yearMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = sdf.parse(yearMonth);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat df = new SimpleDateFormat("dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 上个月最后一天
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();
		return Integer.valueOf(day_last);
	}

	/**
	 * 获取指定时间所在月的最后一天
	 * 
	 * @param timemillis
	 * @return
	 */
	public static int getLastDateByTimemillis(long timemillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timemillis);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return cal.get(Calendar.DATE);
	}

	// 月报部分填充10以后的月份+0
	public static String StringConcatenation(int initData) {
		if (initData < 10) {
			return "0" + initData;
		}
		else {
			return "" + initData;
		}
	}

	// 月报部分填充10以后的月份+0
	public static String StringConcatenation(String initData) {
		if (initData != null) {
			if (initData.length() <= 1) {
				return "0" + initData;

			}
			else {
				return initData;
			}
		}
		else {
			return "error";
		}
	}

	/**
	 * 闊充箰鏃堕棿杞崲
	 * 
	 * @param milliseconds
	 *            姣
	 * @return
	 */
	public static String formatDuration(int milliseconds) {
		int seconds = milliseconds / 1000;
		int secondPart = seconds % 60;
		int minutePart = seconds / 60;
		return (minutePart >= 10 ? minutePart : "0" + minutePart) + ":" + (secondPart >= 10 ? secondPart : "0" + secondPart);
	}

	/** 鑾峰彇褰撳墠鏃ユ湡 */
	public static String getNowDate() {
		String temp_str = "";
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		temp_str = sdf.format(dt);
		return temp_str;
	}

	/** 杞崲鏃跺垎绉� */
	public static String getSecToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
			else {
				hour = minute / 60;
				if (hour > 99) return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
}
