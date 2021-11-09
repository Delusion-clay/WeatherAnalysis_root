package com.it.time;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 时间转换工具类
 */
public class TimeTranstationUtils {

	private static final Logger logger = LoggerFactory.getLogger(TimeTranstationUtils.class);

/*	private static SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat sdFormatternew = new SimpleDateFormat("yyyyMMddHH");
	private static SimpleDateFormat sdFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdFormatter3 = new SimpleDateFormat("yyyyMMdd");*/

	private static Date nowTime;

	public static String getWeek(String dates) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = f.parse(dates);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		cal.setTime(d);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 0) w = 7;
		switch (w){
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			case 7:
				return "星期日";
			default:
				return"";

		}
	}


	public static String Date2yyyyMMddHHmmss() {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		nowTime = new Date(System.currentTimeMillis());
		String time = sdFormatter.format(nowTime);
		return time;
	}


	public static String Date2yyyyMMdd_HHmmss(long timestamp) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowTime = new Date(timestamp);
		String time = sdFormatter.format(nowTime);
		return time;
	}

	public static String Date2yyyyMMddHHmmss(long timestamp) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		nowTime = new Date(timestamp);
		String time = sdFormatter.format(nowTime);
		return time;
	}

	public static String Date2yyyyMMdd(long timestamp) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
		nowTime = new Date(timestamp);
		String time = sdFormatter.format(nowTime);
		return time;
	}


	public static String Date2yyyyMMddHH(String str) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdFormatternew = new SimpleDateFormat("yyyyMMddHH");
		try {
			nowTime = sdFormatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String time = sdFormatternew.format(nowTime);
		return time;
	}

	public static String Date2yyyy_MM_dd() {
		SimpleDateFormat sdFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		nowTime = new Date(System.currentTimeMillis());
		String time = sdFormatter1.format(nowTime);
		return time;
	}

	public static String Date2yyyy_MM_dd_HH_mm_ss() {
		SimpleDateFormat sdFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowTime = new Date(System.currentTimeMillis());
		String time = sdFormatter2.format(nowTime);
		return time;
	}

	public static String Date2yyyyMMdd() {
		SimpleDateFormat sdFormatter3 = new SimpleDateFormat("yyyyMMdd");
		nowTime = new Date(System.currentTimeMillis());
		String time = sdFormatter3.format(nowTime);
		return time;
	}

	public static String Date2yyyyMMdd(String str) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdFormatter3 = new SimpleDateFormat("yyyyMMdd");
		try {
			nowTime = sdFormatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String time = sdFormatter3.format(nowTime);
		return time;
	}

	public static Long Date2yyyyMMddHHmmssToLong() {

		return System.currentTimeMillis() / 1000;
	}

	public static String long2date(String capturetime){

		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		Date dt = new Date(Long.valueOf(capturetime) * 1000);
		String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		return sDateTime;

	}


	public static Long yyyyMMddHHmmssToLong(String time) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		if (StringUtils.isBlank(time)) {
			return 0L;
		} else {
			boolean isNum = time.matches("[0-9]+");
			if (isNum) {
				long long1 = 0;
				try {
					long1 = sdFormatter.parse(time).getTime();

				} catch (ParseException e) {
					logger.error(time + "时间转换为long错误" + isNum);
					return 0L;
				}
				return long1 / 1000;
			}
		}

		return 0L;
	}

	public static Date yyyyMMddHHmmssToDate(String time) {
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		if (StringUtils.isBlank(time)) {
			return new Date();
		} else {
			boolean isNum = time.matches("[0-9]+");
			if (isNum) {
				Date date = null;
				try {
					date = sdFormatter.parse(time);
				} catch (ParseException e) {
					logger.error(time + "时间转换为date错误" + isNum, e);
					System.out.println(time);
					System.out.println(isNum);
					e.printStackTrace();
				}
				return date;
			}
		}
		return new Date();
	}

	public static Date yyyyMMddHHmmssToDate() {
		Date date = null;
		SimpleDateFormat sdFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdFormatter2.parse(Date2yyyy_MM_dd_HH_mm_ss());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static java.sql.Date strToDate(String strDate) {
		String str = strDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		Date d = null;
		try {
			d = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.sql.Date date = new java.sql.Date(d.getTime());
		return date;
	}

	public static Long str2Long(String str){
		if(!StringUtils.isBlank(str)){
			return Long.valueOf(str);
		}else{
			return 0L;
		}
	}

	public static Double str2Double(String str){
		if(!StringUtils.isBlank(str)){
			return Double.valueOf(str);
		}else{
			return 0.0;
		}
	}



	public static HashMap<String,Object> mapString2Long(Map<String,String> map, String key, HashMap<String,Object> objectMap) {
		String logouttime = map.get(key);
		if (!StringUtils.isBlank(logouttime)) {
			objectMap.put(key, Long.valueOf(logouttime));

		} else {
			objectMap.put(key, 0L);
		}
		return objectMap;
	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println(getWeek("2021-08-01"));

	}
}
