package com.osimatic.android_helpers;

import static android.text.format.Time.MONDAY_BEFORE_JULIAN_EPOCH;

import android.content.res.Resources;
import android.text.format.Time;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime {
	// ------------------------------------------------------------
	// Fonction depuis objet Calendar
	// ------------------------------------------------------------

	public static String formatDateShort(Calendar calendar, Locale locale, String timeZone) {
		return Timestamp.formatDateShort(calendar.getTimeInMillis(), locale, timeZone);
	}

	public static String formatTime(Calendar calendar, Locale locale, String timeZone) {
		return Timestamp.formatTime(calendar.getTimeInMillis(), locale, timeZone);
	}

	public static String formatDateTime(Calendar calendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_date_time),
				DateTime.formatDateShort(calendar, locale, timeZone),
				DateTime.formatTime(calendar, locale, timeZone));
	}

	public static String getSqlDate(Calendar calendar) {
		return (new java.sql.Date(calendar.getTimeInMillis())).toString();
	}

	public static String getSqlTime(Calendar calendar) {
		return (new java.sql.Time(calendar.getTimeInMillis())).toString();
	}

	public static Calendar parse(String sqlDateTime) {
		return DateTime.parse(sqlDateTime, TimeZone.getTimeZone("UTC"));
	}
	public static Calendar parse(String sqlDateTime, TimeZone timeZone) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
			df.setTimeZone(timeZone);
			Calendar calendar = Calendar.getInstance();
			Date date = df.parse(sqlDateTime);
			if (date != null) {
				calendar.setTime(date);
			}
			return calendar;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Calendar make(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day);
	}
	public static Calendar make(int year, int month, int day, int hour, int minute) {
		return new GregorianCalendar(year, month - 1, day, hour, minute);
	}
	public static Calendar make(int year, int month, int day, int hour, int minute, int second) {
		return new GregorianCalendar(year, month - 1, day, hour, minute, second);
	}

	public static Calendar getFirsDayOfMonth(int year, int month) {
		return make(year, month, 1);
	}
	public static Calendar getLastDayOfMonth(int year, int month) {
		return make(year, month, DateTime.getNumberOfDaysOfMonth(year, month));
	}

	/**
	 * Returns a {@link Calendar} set to midnight (00:00:00.000) of the current local date,
	 * expressed in the given timezone.
	 * The local date (year, month, day) is read from the device's default timezone so that the
	 * returned instant always corresponds to "today as the user perceives it", regardless of
	 * the target timezone. This avoids the common off-by-one-day issue that occurs in the first
	 * hours of the day when UTC is behind the local timezone (e.g. 00:30 UTC+1 → still yesterday
	 * in UTC).
	 * @param targetTimeZone the timezone in which the resulting Calendar is expressed; must not be null.
	 * @return a new Calendar at midnight (00:00:00.000) of today's local date in {@code targetTimeZone}.
	 */
	public static Calendar getTodayAtMidnight(TimeZone targetTimeZone) {
		Calendar localNow = Calendar.getInstance();
		Calendar cal = Calendar.getInstance(targetTimeZone, Locale.getDefault());
		cal.set(localNow.get(Calendar.YEAR), localNow.get(Calendar.MONTH), localNow.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * Convenience method returning a {@link Calendar} at midnight (00:00:00.000 UTC) of the current
	 * local date.
	 * Equivalent to {@code getTodayAtMidnight(TimeZone.getTimeZone("UTC"))}.
	 * @return a new Calendar at midnight (00:00:00.000 UTC) of today's local date.
	 * @see #getTodayAtMidnight(TimeZone)
	 */
	public static Calendar getTodayAtMidnightUtc() {
		return getTodayAtMidnight(TimeZone.getTimeZone("UTC"));
	}


	// ------------------------------------------------------------
	// Fonction depuis jour/mois/année
	// ------------------------------------------------------------

	public static boolean isToday(int year, int month, int day) {
		Calendar today = Calendar.getInstance(Locale.getDefault());
		return (year == today.get(Calendar.YEAR) && month == (today.get(Calendar.MONTH) + 1) && day == today.get(Calendar.DAY_OF_MONTH));
	}

	public static String getMonthAsString(int month) {
		return new DateFormatSymbols().getMonths()[month-1];
		//return new SimpleDateFormat("MMMM").format(date);
	}

	public static String getWeekDayAsString(int weekDay) {
		return DateTime.getWeekDayAsString(weekDay, false);
	}

	public static String getWeekDayAsString(int weekDay, boolean isShort) {
		//myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
		weekDay = (weekDay == 7) ? 1 : weekDay+1;
		return isShort ? new DateFormatSymbols().getShortWeekdays()[weekDay] : new DateFormatSymbols().getWeekdays()[weekDay];
	}

	public static int getNumberOfDaysOfMonth(int year, int month) {
		return (new GregorianCalendar(year, month - 1, 1)).getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	// ------------------------------------------------------------
	// Fonction diverses
	// ------------------------------------------------------------

	/**
	 * Returns the week since {@link Time#EPOCH_JULIAN_DAY} (Jan 1, 1970)
	 * adjusted for first day of week.
	 * <p>
	 * This takes a julian day and the week start day and calculates which
	 * week since {@link Time#EPOCH_JULIAN_DAY} that day occurs in, starting
	 * at 0. *Do not* use this to compute the ISO week number for the year.
	 *
	 * @param julianDay The julian day to calculate the week number for
	 * @param firstDayOfWeek Which week day is the first day of the week,
	 * see {@link Time#SUNDAY}
	 * @return Weeks since the epoch
	 */
	public static int getWeeksSinceEpochFromJulianDay(int julianDay, int firstDayOfWeek) {
		int diff = Time.THURSDAY - firstDayOfWeek;
		if (diff < 0) {
			diff += 7;
		}
		int refDay = Time.EPOCH_JULIAN_DAY - diff;
		return (julianDay - refDay) / 7;
	}

	/**
	 * Takes a number of weeks since the epoch and calculates the Julian day of
	 * the Monday for that week.
	 * <p>
	 * This assumes that the week containing the {@link Time#EPOCH_JULIAN_DAY}
	 * is considered week 0. It returns the Julian day for the Monday
	 * {@code week} weeks after the Monday of the week containing the epoch.
	 *
	 * @param week Number of weeks since the epoch
	 * @return The julian day for the Monday of the given week since the epoch
	 */
	public static int getJulianMondayFromWeeksSinceEpoch(int week) {
		return MONDAY_BEFORE_JULIAN_EPOCH + week * 7;
	}

	/**
	 * Get first day of week as android.text.format.Time constant.
	 *
	 * @return the first day of week in android.text.format.Time
	 */
	public static int getFirstDayOfWeek() {
		int startDay = Calendar.getInstance().getFirstDayOfWeek();
		if (startDay == Calendar.SATURDAY) {
			return Time.SATURDAY;
		} else if (startDay == Calendar.MONDAY) {
			return Time.MONDAY;
		} else {
			return Time.SUNDAY;
		}
	}

	/**
	 * Get first day of week as java.util.Calendar constant.
	 *
	 * @return the first day of week as a java.util.Calendar constant
	 */
	public static int getFirstDayOfWeekAsCalendar() {
		return convertDayOfWeekFromTimeToCalendar(getFirstDayOfWeek());
	}

	/**
	 * Converts the day of the week from android.text.format.Time to java.util.Calendar
	 */
	public static int convertDayOfWeekFromTimeToCalendar(int timeDayOfWeek) {
		return switch (timeDayOfWeek) {
			case Time.MONDAY -> Calendar.MONDAY;
			case Time.TUESDAY -> Calendar.TUESDAY;
			case Time.WEDNESDAY -> Calendar.WEDNESDAY;
			case Time.THURSDAY -> Calendar.THURSDAY;
			case Time.FRIDAY -> Calendar.FRIDAY;
			case Time.SATURDAY -> Calendar.SATURDAY;
			case Time.SUNDAY -> Calendar.SUNDAY;
			default ->
				throw new IllegalArgumentException("Argument must be between Time.SUNDAY and Time.SATURDAY");
		};
	}


	/** @deprecated */
	@Deprecated
	public static String formatPeriod(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_period),
				DateTime.formatDateShort(startCalendar, locale, timeZone),
				DateTime.formatDateShort(endCalendar, locale, timeZone));
	}

	/** @deprecated */
	@Deprecated
	public static String formatTimeSlot(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_time_slot),
				DateTime.formatTime(startCalendar, locale, timeZone),
				DateTime.formatTime(endCalendar, locale, timeZone));
	}
}
