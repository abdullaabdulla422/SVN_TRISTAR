package com.tristar.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	
	public static String stringFromDate(String date) {
		if(date.contains("."))
			date = date.replace(date.subSequence(date.indexOf("."), date.length()-6), "");
		SimpleDateFormat oldDateFormat = getTMDateFormat();
		SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy");
		String dateString = null;
		Date dateObj;
		try {
			dateObj = oldDateFormat.parse(date);
			dateString = newDateFormat.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
   
		return dateString;
    }
	
	
	
    public static int timeIntervalSinceDate(String date) {
        try {
            
            long expdays = 0;
            Date currentDate = new Date(System.currentTimeMillis());
            Date expDate = getTMDateFormat().parse(date);

            expdays = expDate.getTime() - currentDate.getTime();
            expdays = expdays / (24 * 3600 * 1000);
            return (int)expdays;

        }catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
   
    private static SimpleDateFormat getTMDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }
    
    @SuppressWarnings("deprecation")
	public static String getDateString(Date date) {
    	try{
    		return getTMDateFormat().format(date).toString();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return date.toLocaleString();
    }
    
    
    public static String getMonthYear(String date) {
    	try {
	    	SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM");
	    	Date dateobj = oldDateFormat.parse(date);
	    	oldDateFormat = new SimpleDateFormat("MMMM yyyy");
	    	date = oldDateFormat.format(dateobj);
    	}catch(Exception e) {
            e.printStackTrace();
        }
    	return date;
    }
    
	public  String getFormatWebservice(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObject = null;
		try {
			dateObject = dateFormat.parse(date);
		} catch(Exception e) {
			dateObject = new Date();
		}
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		return dateFormat.format(dateObject);
	}
    
    public static String getDateAsNumber(String date) {
    	try {
	    	SimpleDateFormat oldDateFormat = new SimpleDateFormat("MMMM d, yyyy");
	    	Date dateobj = oldDateFormat.parse(date);
	    	oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	date = oldDateFormat.format(dateobj);
    	}catch(Exception e) {
            e.printStackTrace();
        }
    	return date;
    }
    
    public static String getDisplayDate(String date) {
    	try {
    		date = date.length() >10 ? date.substring(0, 10) : date;
	    	SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	Date dateobj = oldDateFormat.parse(date);
	    	oldDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    	oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	date = oldDateFormat.format(dateobj);
    	}catch(Exception e) {
            e.printStackTrace();
        }
    	return date;
    }
    public static String getDisplaytime(String date) {
    	try {
    		date = date.length() >10 ? date.substring(0, 10) : date;
	    	SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	Date dateobj = oldDateFormat.parse(date);
	    	oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    	date = oldDateFormat.format(dateobj);
    	}catch(Exception e) {
            e.printStackTrace();
        }
    	return date;
    }
    
    public static String getYearMonthAsNumber(String date) {
    	try {
	    	SimpleDateFormat oldDateFormat = new SimpleDateFormat("MMMM yyyy");
	    	Date dateobj = oldDateFormat.parse(date);
	    	oldDateFormat = new SimpleDateFormat("yyyy-MM");
	    	date = oldDateFormat.format(dateobj);
    	}catch(Exception e) {
            e.printStackTrace();
        }
    	return date;
    }
    
    public static Pair<Date, Date> getCurrentDateRange(String date) {
	    Date begining = null, end = null;
	    Log.d("DateRange function", "called");
	    if(date.equals("year")) {
		    {
		        Calendar calendar = getCalendarForNow();
		        calendar.set(Calendar.DAY_OF_YEAR,
		                calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
		        setTimeToBeginningOfDay(calendar);
		        begining = calendar.getTime();
		    }
	
		    {
		        Calendar calendar = getCalendarForNow();
		        calendar.set(Calendar.DAY_OF_YEAR,
		                calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		        setTimeToEndofDay(calendar);
		        end = calendar.getTime();
		    }
	    }
	    else if(date.equals("month")) {
	    	 {
		        Calendar calendar = getCalendarForNow();
		        calendar.set(Calendar.DAY_OF_MONTH,
		                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		        setTimeToBeginningOfDay(calendar);
		        begining = calendar.getTime();
		    }
	
		    {
		        Calendar calendar = getCalendarForNow();
		        calendar.set(Calendar.DAY_OF_MONTH,
		                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		        setTimeToEndofDay(calendar);
		        end = calendar.getTime();
		    }
	    }
	    Log.d("DateRange function", "returned");
	    return new Pair<Date, Date> (begining, end);
	}
	
	private static Calendar getCalendarForNow() {
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar;
	}

	private static void setTimeToBeginningOfDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setTimeToEndofDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	}
	
	public static Date parseDate(String format, String strValue) {
		try {
			SimpleDateFormat oldDateFormat = new SimpleDateFormat(format);
			return oldDateFormat.parse(strValue);
		} catch (Exception e) {
			return new Date();
		}
	}
}
