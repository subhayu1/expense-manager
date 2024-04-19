package com.rimalholdings.expensemanager.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

public static Timestamp getCurrentTimeInUTC() {
	ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
	return Timestamp.valueOf(zonedDateTime.toLocalDateTime());
}

public static String convertTimestampToISO8601(Timestamp timestamp) {
	return timestamp.toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
}

public static Date getCurrentDate() {
	return Date.valueOf(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate());
}

public static Timestamp convertStringToTimestamp(String invoiceDate) {
	return Timestamp.valueOf(invoiceDate);
}

// convert "2024-04-17T14:08:34.653Z" to timestamp
public static Timestamp convertISO8601ToTimestamp(String iso8601) {
	// check if the string is in the format "2024-04-17T14:08:34.653Z and only then convert it to
	// timestamp else return as it is
	if (iso8601.length() == 24
		&& iso8601.charAt(10) == 'T'
		&& iso8601.charAt(19) == '.'
		&& iso8601.charAt(23) == 'Z') {
	return Timestamp.valueOf(
		ZonedDateTime.parse(iso8601, DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime());
	}

	return Timestamp.valueOf(iso8601);
}
}
