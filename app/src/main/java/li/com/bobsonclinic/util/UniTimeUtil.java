package li.com.bobsonclinic.util;


/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UniTimeUtil {
	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
		Date date = new Date(timestamp);
		return sdf.format(date);
	}

	public static String getNowDateString(){
		return getStandardTime(getlongTime());
	}

	public static long getlongTime() {
		return System.currentTimeMillis();
	}


    //String str = UniTimeUtil.formatDate(UniTimeUtil.getNowDateString(),new SimpleDateFormat("yyyy/MM/dd") );
	public static String formatDate(String inputDate, SimpleDateFormat newSdf){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String finalDate = "";
		try {
			Date date1 = dateFormat.parse(inputDate);
			finalDate =  newSdf.format(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return finalDate;
	}
	
	public static boolean isSameDay(long time1, long time2) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		Date date1 = new Date(time1);
		Date date2 = new Date(time2);
		return fmt.format(date1).equals(fmt.format(date2));
	}

	public static String getArticleEditTimeString(long time) {
		long now = System.currentTimeMillis() / 1000;

		String returnStr = "";
		long timeOffset = now - time;
		if (timeOffset < 3600) {
			long minute = timeOffset / 60;
			returnStr = minute + "分鐘前";
		} else if (timeOffset < 86400) {
			long hour = timeOffset / 3600;
			returnStr = hour + "小時前";
		} else if (timeOffset < 2592000) {
			long day = timeOffset / 86400;
			returnStr = day + "天前";
		} else if (timeOffset < 31536000) {
			long month = timeOffset / 2592000;
			returnStr = month + "個月前";
		} else {
			long year = timeOffset / 31536000;
			returnStr = year + "年前";
		}
		return returnStr;
	}

	public static String getWeeklyPeriod(long dayTimeStamp){

		SimpleDateFormat firstDateOfWeekFormat = new SimpleDateFormat("M/dd");
		SimpleDateFormat lastDatOfWeekeFormat = new SimpleDateFormat("dd");
		Calendar thisday = Calendar.getInstance();

		thisday.setFirstDayOfWeek(Calendar.MONDAY);
		thisday.setTimeInMillis(dayTimeStamp);

		int dayOfWeek = thisday.get(Calendar.DAY_OF_WEEK); //Start from monday

		dayOfWeek = dayOfWeek - 1; //Start from monday
		if(dayOfWeek == 0){
			dayOfWeek = 7;
		}

		//first date of week
		Calendar firstDay = Calendar.getInstance();
		firstDay.setFirstDayOfWeek(Calendar.MONDAY);
		firstDay.setTimeInMillis(thisday.getTimeInMillis()-((dayOfWeek-1)*24*60*60*1000));

		//last date of week
		Calendar lastDay = Calendar.getInstance();
		lastDay.setFirstDayOfWeek(Calendar.MONDAY);
		lastDay.setTimeInMillis(thisday.getTimeInMillis() + ((7 - dayOfWeek) * 24 * 60 * 60 * 1000));

		String currentWeek = firstDateOfWeekFormat.format(firstDay.getTimeInMillis()) + "-" + lastDatOfWeekeFormat.format(lastDay.getTimeInMillis());

		return currentWeek;
	}

	public static String getFirstDayOfWeek(long dayTimeStamp){

		SimpleDateFormat dateOfWeekFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00");
		Calendar thisday = Calendar.getInstance();
		thisday.setFirstDayOfWeek(Calendar.MONDAY);
		thisday.setTimeInMillis(dayTimeStamp);
		int dayOfWeek = thisday.get(Calendar.DAY_OF_WEEK); //Start from monday

		dayOfWeek = dayOfWeek - 1; //Start from monday
		if(dayOfWeek == 0){
			dayOfWeek = 7;
		}

		//first date of week
		Calendar firstDay = Calendar.getInstance();
		firstDay.setFirstDayOfWeek(Calendar.MONDAY);
		firstDay.setTimeInMillis(thisday.getTimeInMillis() - ((dayOfWeek - 1) * 24 * 60 * 60 * 1000));

		return dateOfWeekFormat.format(firstDay.getTimeInMillis());
	}

	public static String getlastDayOfWeek(long dayTimeStamp){

		SimpleDateFormat dateOfWeekFormat = new SimpleDateFormat("yyyy-MM-dd'T'24:00");
		Calendar thisday = Calendar.getInstance();
		thisday.setFirstDayOfWeek(Calendar.MONDAY);
		thisday.setTimeInMillis(dayTimeStamp);

		int dayOfWeek = thisday.get(Calendar.DAY_OF_WEEK); //Start from monday

		dayOfWeek = dayOfWeek - 1; //Start from monday
		if(dayOfWeek == 0){
			dayOfWeek = 7;
		}

		//last date of week
		Calendar lastDay = Calendar.getInstance();
		lastDay.setFirstDayOfWeek(Calendar.MONDAY);
		lastDay.setTimeInMillis(thisday.getTimeInMillis() + ((7 - dayOfWeek ) * 24 * 60 * 60 * 1000));


		return dateOfWeekFormat.format(lastDay.getTimeInMillis());
	}
}
