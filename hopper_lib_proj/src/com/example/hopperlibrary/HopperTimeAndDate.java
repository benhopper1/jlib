package com.example.hopperlibrary;

import java.sql.Date;
import java.util.Calendar;

public class HopperTimeAndDate {
	
	public static String getTimeAndDateString(long inStamp){
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(inStamp);
		Date date = (Date) cal.getTime();
		int mHour = date.getHours();
		int mMinute = date.getMinutes();
		return date+" "+mHour+":"+ mMinute;
	}

}
