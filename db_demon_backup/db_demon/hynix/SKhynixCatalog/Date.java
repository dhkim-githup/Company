package SKhynixCatalog;

import java.util.*;
import java.text.*;

public class Date {
	private Calendar cal;
	private final String dateSep = "/";
	private final String timeSep = ":";
	private final String[] day = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
	public Date() {
		cal = Calendar.getInstance();
	}
	
	public void set_today() {	//  . 
		cal = Calendar.getInstance();
	} 

	public void set_move_day(int p_gap) { // 현재날짜를 기준으로 +- 날짜이동
		cal.add(Calendar.DATE,p_gap);
	}

	public int Year()		{ return cal.get(Calendar.YEAR); }
	public int Month()		{ return cal.get(Calendar.MONTH)+1; }
	public int Day()		{ return cal.get(Calendar.DATE); }
	public String DayOfWeek() { return day[cal.get(Calendar.DAY_OF_WEEK)-1]; }
	public int Hour()		{ return cal.get(Calendar.HOUR_OF_DAY); }
	public int Minute()		{ return cal.get(Calendar.MINUTE); }
	public int Second()		{ return cal.get(Calendar.SECOND); }
	
	public String Date()	{ return Date(dateSep); }
	public String Date(String p_dlmt)	{ return SYear() + p_dlmt + SMonth() + p_dlmt + SDay(); }
	
	public String Time()	{ return Time(timeSep); }
	public String Time(String p_dlmt)	{ return Hour() + p_dlmt + Minute() + p_dlmt + Second(); }
	
	public String DateTime(){ return Date() + " " + Time(); }

	public String SYear()	{ return Integer.toString(Year()); }
	public String SMonth()	{
		int mon = cal.get(Calendar.MONTH)+1;
		if(mon < 10) { return "0"+Integer.toString(mon);
		} else { return Integer.toString(mon); }
	}
	public String SDay()	{
		int day = cal.get(Calendar.DATE);
		if(day < 10) { return "0"+Integer.toString(day);
		} else { return Integer.toString(day); }
	}
}