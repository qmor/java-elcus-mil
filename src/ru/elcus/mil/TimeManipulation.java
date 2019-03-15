package ru.elcus.mil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeManipulation {
	public static String ToLongTimeStringMillis(LocalDateTime dt)
	{
		String result = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");
		result =  dt.format(formatter);
		return result;
	}
	private static final ZoneId utcZone = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0));
	public static double getUnixTimeUTC(LocalDateTime dt)
	{
		ZonedDateTime dtatz =  dt.atZone(utcZone);
		return dtatz.toEpochSecond()+((dtatz.getNano()/1000000.0)/1000.0);
	}

	public static LocalDateTime getDateTimeFromUnixUTC(double unix_utc)
	{
		long millis = (long)(unix_utc*1000.0);
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.ofOffset("UTC", ZoneOffset.ofHours(0)));
	}
}
