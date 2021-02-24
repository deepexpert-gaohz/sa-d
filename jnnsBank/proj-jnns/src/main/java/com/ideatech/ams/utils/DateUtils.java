/**
 *
 */
package com.ideatech.ams.utils;

import com.ideatech.ams.pbc.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zoulang
 *
 */
public class DateUtils {

	public static final String[] PARSE_PATTERNS = { "yyyy-MM-dd", "yyyyMMdd", "yyyy年MM月dd日" };

	private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);

	public static String getDateTime() {
		return StringUtils.remove(DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()), "T");
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

//	/**
//	 * 字符串的日期格式的计算
//	 */
//	public static int daysBetween(String smdate, String bdate) throws ParseException {
//		return  daysBetween(smdate,bdate,"yyyy-MM-dd");
//	}

	/**
	 * 字符串的日期格式的计算
	 */
	public  int dayBetween(String smdate, String bdate,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取现在时间
	 *
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static String getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 根据传递的时间格式 获取系统当前时间
	 *
	 * @param format
	 * @return
	 */
	public static String getNowDateShort(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取当前日期的前一天 yyyyMMdd
	 */
	public static String dayBefore(String format) {
		if (StringUtils.isBlank(format)) {
			format = "yyy-MM-dd";
		}
		SimpleDateFormat dft = new SimpleDateFormat(format);
		Date now = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(now);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		String daybefor = "";
		try {
			daybefor = dft.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daybefor;
	}

	/**
	 * 获取传入日期的后一天 yyyyMMdd
	 *
	 * @param
	 */
	public static String dayafter(String dateStr, String format) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		if (StringUtils.isNotBlank(dateStr)) {
			try {
				now = DateUtils.parse(dateStr, "yyyyMMdd");
			} catch (ParseException e) {
				now = new Date();
			}
		} else {
			now = new Date();
		}
		Calendar date = Calendar.getInstance();
		date.setTime(now);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
		String daybefor = "";
		try {
			daybefor = dft.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daybefor;
	}

	/**
	 * 时间根据格式转换
	 *
	 * @param date
	 *            空为当前时间
	 * @param format
	 *            空为 yyyy-MM-dd
	 * @return
	 */
	public static String DateToStr(Date date, String format) {
		if (StringUtils.isBlank(format)) {
			format = "yyyy-MM-dd";
		}
		if (date == null) {
			date = new Date();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 根据传递的格式设置字符串时间样式
	 *
	 * <pre>
	 * 支持 yyyyMMdd 和 yyyy-MM-dd两种格式
	 * </pre>
	 *
	 * @param columnValue
	 * @return
	 */
	public static String setDateColumnFormat(String columnValue, String format) {
		try {
			if (StringUtils.isBlank(columnValue)) {
				return "";
			}
			columnValue = Utils.getStringNoBlank(columnValue);
			format = Utils.getStringNoBlank(format);
			if (columnValue.length() == 10 && format.equals("yyyy-MM-dd")) {
				return columnValue;
			} else if (columnValue.length() == 8 && format.equals("yyyyMMdd")) {
				return columnValue;
			} else if (columnValue.length() == 10 && format.equals("yyyyMMdd")) {
				return DateToStr(parse(columnValue, "yyyy-MM-dd"), format);
			} else if (columnValue.length() == 8 && format.equals("yyyy-MM-dd")) {
				return DateToStr(parse(columnValue, "yyyyMMdd"), format);
			} else if (columnValue.length() > 10) {
				return DateToStr(parse(columnValue, "yyyy/MM/dd"), format);
			} else {
				return columnValue;
			}
		} catch (ParseException e) {
			return "";
		}
	}

	/**
	 * 根据字符串格式转时间
	 *
	 * @param strDate
	 *            字符串时间
	 * @param pattern
	 *            格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
	}

	public static Date parse5Num(String dateNum) {
		Calendar calendar = new GregorianCalendar(1900, 0, 0);
		calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(dateNum));
		return calendar.getTime();
	}

	public static void pause() {
		while (needPause()) {
			LOG.info("禁止在工作时间采集人行数据, 10分钟后重试");
			try {
				TimeUnit.MINUTES.sleep(10);
			} catch (InterruptedException e) {
				// 线程暂停异常，不做处理
				LOG.error("线程暂停异常", e);
			}
		}
	}

	public static boolean needPause() {
		Calendar calendar = Calendar.getInstance();
		return needPause(calendar);
	}

	public static boolean needPause(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_YEAR);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// TODO 处理节假日,还需完善

		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			// 17年9月30日
			return year == 2017 && month == Calendar.SEPTEMBER && day == 30;
		}

		Integer[] days = { 2, 3, 4, 5, 6 };

		// 10月2日 - 10月6日
		if (year == 2017 && month == Calendar.OCTOBER && Arrays.asList(days).contains(day))
			return false;

		return hourOfDay <= 17 && (hourOfDay != 17 || minute <= 30);
	}

//	public static void main(String[] args) {
//		try {
//			System.out.println(DateUtils.daysBetween("20190529","20190530","yyyyMMdd"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public Calendar initHolidayList(String dateStr) {
		try {
			Date date = org.apache.commons.lang.time.DateUtils.parseDate(dateStr, PARSE_PATTERNS);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			LOG.info("添加节假日日期格式错误，请使用yyyy-MM-dd格式", e);
		}
		return null;
	}

	public boolean checkHoliday(Calendar calendar) {
		List<Calendar> holidayList = new ArrayList<Calendar>();
		// 判断日期是否是节假日
		for (Calendar ca : holidayList) {
			if (ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
				return true;
		}
		return false;
	}


	/**
	 * 返回true，则d2大于d1
	 * 反之则d1大于d2
	 *
	 * @param d1
	 * @param d2
	 * @return
	 * @throws Exception
	 */
	public static boolean dateIsLarge(String d1, String d2) throws Exception {
		Date date1 = org.apache.commons.lang.time.DateUtils.parseDate(d1, PARSE_PATTERNS);
		Date date2 = org.apache.commons.lang.time.DateUtils.parseDate(d2, PARSE_PATTERNS);
		return date1.before(date2);
	}

}
