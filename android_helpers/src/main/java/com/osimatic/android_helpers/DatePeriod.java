package com.osimatic.android_helpers;

import android.content.res.Resources;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DatePeriod {
	public static String formatPeriod(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_period),
				DateTime.formatDateShort(startCalendar, locale, timeZone),
				DateTime.formatDateShort(endCalendar, locale, timeZone));
	}

	public static String formatPeriod(long startTimestampInMillis, long endTimestampInMillis, Resources r, Locale locale, String timeZone) {
		return DatePeriod.formatPeriod(Timestamp.toCalendar(startTimestampInMillis, timeZone), Timestamp.toCalendar(endTimestampInMillis, timeZone), r, locale, timeZone);
	}

	public static String formatTimeSlot(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_time_slot),
				DateTime.formatTime(startCalendar, locale, timeZone),
				DateTime.formatTime(endCalendar, locale, timeZone));
	}

	public static String formatTimeSlot(long startTimestampInMillis, long endTimestampInMillis, Resources r, Locale locale, String timeZone) {
		return DatePeriod.formatTimeSlot(Timestamp.toCalendar(startTimestampInMillis, timeZone), Timestamp.toCalendar(endTimestampInMillis, timeZone), r, locale, timeZone);
	}

	public static long getNbDays(Calendar startCalendar, Calendar endCalendar) {
		long timeDiff = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
		return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS) + 1;
	}
}
