package com.rimalholdings.expensemanager.util;

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

public static Timestamp convertStringToTimestamp(String invoiceDate) {
	return Timestamp.valueOf(invoiceDate);
}
}
