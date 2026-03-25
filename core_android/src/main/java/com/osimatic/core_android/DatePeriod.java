package com.osimatic.core_android;

import android.content.res.Resources;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility class providing helper methods for date/time periods and time slots.
 *
 * <p>A period is defined by a start and an end {@link Calendar} (or timestamp in milliseconds). All day counts are inclusive: a period from day D to day D has a count of 1.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see DateTime
 * @see Timestamp
 * @see java.util.Calendar
 */
public class DatePeriod {

	private DatePeriod() {}

	// =============================================================================================
	// Formatting
	// =============================================================================================

	/**
	 * Formats the given period as a localized date range string using the {@code R.string.formatted_period} resource pattern.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @param r             the {@link Resources} used to retrieve the format string; must not be {@code null}
	 * @param locale        the locale to use for date formatting; must not be {@code null}
	 * @param timeZone      the time zone ID to use for date formatting (e.g. {@code "Europe/Paris"}); must not be {@code null}
	 * @return a formatted period string (e.g. {@code "01/01/2024 – 31/01/2024"})
	 * @see DateTime#formatDateShort(Calendar, Locale, String)
	 */
	public static String formatPeriod(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_period),
				DateTime.formatDateShort(startCalendar, locale, timeZone),
				DateTime.formatDateShort(endCalendar, locale, timeZone));
	}

	/**
	 * Formats the given period as a localized date range string using the {@code R.string.formatted_period} resource pattern.
	 *
	 * @param startTimestampInMillis the start of the period as a Unix timestamp in milliseconds
	 * @param endTimestampInMillis   the end of the period as a Unix timestamp in milliseconds
	 * @param r                      the {@link Resources} used to retrieve the format string; must not be {@code null}
	 * @param locale                 the locale to use for date formatting; must not be {@code null}
	 * @param timeZone               the time zone ID to use for date formatting (e.g. {@code "Europe/Paris"}); must not be {@code null}
	 * @return a formatted period string (e.g. {@code "01/01/2024 – 31/01/2024"})
	 * @see #formatPeriod(Calendar, Calendar, Resources, Locale, String)
	 */
	public static String formatPeriod(long startTimestampInMillis, long endTimestampInMillis, Resources r, Locale locale, String timeZone) {
		return formatPeriod(
				Timestamp.toCalendar(startTimestampInMillis, timeZone),
				Timestamp.toCalendar(endTimestampInMillis, timeZone),
				r, locale, timeZone);
	}

	/**
	 * Formats the given time slot as a localized time range string using the {@code R.string.formatted_time_slot} resource pattern.
	 *
	 * @param startCalendar the start of the time slot; must not be {@code null}
	 * @param endCalendar   the end of the time slot; must not be {@code null}
	 * @param r             the {@link Resources} used to retrieve the format string; must not be {@code null}
	 * @param locale        the locale to use for time formatting; must not be {@code null}
	 * @param timeZone      the time zone ID to use for time formatting (e.g. {@code "Europe/Paris"}); must not be {@code null}
	 * @return a formatted time slot string (e.g. {@code "09:00 – 17:00"})
	 * @see DateTime#formatTime(Calendar, Locale, String)
	 */
	public static String formatTimeSlot(Calendar startCalendar, Calendar endCalendar, Resources r, Locale locale, String timeZone) {
		return String.format(r.getString(R.string.formatted_time_slot),
				DateTime.formatTime(startCalendar, locale, timeZone),
				DateTime.formatTime(endCalendar, locale, timeZone));
	}

	/**
	 * Formats the given time slot as a localized time range string using the {@code R.string.formatted_time_slot} resource pattern.
	 *
	 * @param startTimestampInMillis the start of the time slot as a Unix timestamp in milliseconds
	 * @param endTimestampInMillis   the end of the time slot as a Unix timestamp in milliseconds
	 * @param r                      the {@link Resources} used to retrieve the format string; must not be {@code null}
	 * @param locale                 the locale to use for time formatting; must not be {@code null}
	 * @param timeZone               the time zone ID to use for time formatting (e.g. {@code "Europe/Paris"}); must not be {@code null}
	 * @return a formatted time slot string (e.g. {@code "09:00 – 17:00"})
	 * @see #formatTimeSlot(Calendar, Calendar, Resources, Locale, String)
	 */
	public static String formatTimeSlot(long startTimestampInMillis, long endTimestampInMillis, Resources r, Locale locale, String timeZone) {
		return formatTimeSlot(
				Timestamp.toCalendar(startTimestampInMillis, timeZone),
				Timestamp.toCalendar(endTimestampInMillis, timeZone),
				r, locale, timeZone);
	}

	// =============================================================================================
	// Duration
	// =============================================================================================

	/**
	 * Returns the number of days in the period defined by {@code startCalendar} and {@code endCalendar}, inclusive.
	 *
	 * <p>The order of arguments does not matter: the absolute difference is used. For example, a period from January 1 to January 3 returns {@code 3}.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return the number of days in the period, always ≥ 1
	 */
	public static long getDayCount(Calendar startCalendar, Calendar endCalendar) {
		long diff = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
	}

	/** @deprecated Use {@link #getDayCount(Calendar, Calendar)} instead. */
	@Deprecated
	public static long getNbDays(Calendar startCalendar, Calendar endCalendar) {
		return getDayCount(startCalendar, endCalendar);
	}

	/**
	 * Returns the number of days in the period defined by the two timestamps, inclusive.
	 *
	 * <p>The order of arguments does not matter: the absolute difference is used.
	 *
	 * @param startTimestampInMillis the start of the period as a Unix timestamp in milliseconds
	 * @param endTimestampInMillis   the end of the period as a Unix timestamp in milliseconds
	 * @return the number of days in the period, always ≥ 1
	 * @see #getDayCount(Calendar, Calendar)
	 */
	public static long getDayCount(long startTimestampInMillis, long endTimestampInMillis) {
		long diff = Math.abs(endTimestampInMillis - startTimestampInMillis);
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
	}

	/**
	 * Returns the number of whole hours between {@code startCalendar} and {@code endCalendar}.
	 *
	 * <p>The order of arguments does not matter: the absolute difference is used.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return the number of whole hours between the two instants
	 */
	public static long getHourCount(Calendar startCalendar, Calendar endCalendar) {
		long diff = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
		return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the number of whole minutes between {@code startCalendar} and {@code endCalendar}.
	 *
	 * <p>The order of arguments does not matter: the absolute difference is used.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return the number of whole minutes between the two instants
	 */
	public static long getMinuteCount(Calendar startCalendar, Calendar endCalendar) {
		long diff = Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
		return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the absolute duration in milliseconds between {@code startCalendar} and {@code endCalendar}.
	 *
	 * <p>The order of arguments does not matter: the absolute difference is returned.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return the absolute duration in milliseconds
	 */
	public static long getDurationMillis(Calendar startCalendar, Calendar endCalendar) {
		return Math.abs(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
	}

	// =============================================================================================
	// Comparison
	// =============================================================================================

	/**
	 * Returns {@code true} if the given instant falls within the period [{@code startCalendar}, {@code endCalendar}], inclusive.
	 *
	 * @param instant       the instant to test; must not be {@code null}
	 * @param startCalendar the start of the period (inclusive); must not be {@code null}
	 * @param endCalendar   the end of the period (inclusive); must not be {@code null}
	 * @return {@code true} if {@code instant} is within the period
	 */
	public static boolean contains(Calendar instant, Calendar startCalendar, Calendar endCalendar) {
		long t = instant.getTimeInMillis();
		return t >= startCalendar.getTimeInMillis() && t <= endCalendar.getTimeInMillis();
	}

	/**
	 * Returns {@code true} if the two periods overlap.
	 *
	 * <p>Two periods overlap if one starts before the other ends. Periods that share only a boundary instant are considered overlapping.
	 *
	 * @param start1 the start of the first period; must not be {@code null}
	 * @param end1   the end of the first period; must not be {@code null}
	 * @param start2 the start of the second period; must not be {@code null}
	 * @param end2   the end of the second period; must not be {@code null}
	 * @return {@code true} if the two periods overlap
	 */
	public static boolean overlaps(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
		return start1.getTimeInMillis() <= end2.getTimeInMillis()
				&& start2.getTimeInMillis() <= end1.getTimeInMillis();
	}

	/**
	 * Returns {@code true} if both calendars represent the same calendar day (year, month, and day of month).
	 *
	 * <p>Time-of-day and time zone fields are ignored.
	 *
	 * @param a the first calendar; must not be {@code null}
	 * @param b the second calendar; must not be {@code null}
	 * @return {@code true} if both calendars fall on the same day
	 */
	public static boolean isSameDay(Calendar a, Calendar b) {
		return a.get(Calendar.YEAR)         == b.get(Calendar.YEAR)
				&& a.get(Calendar.MONTH)        == b.get(Calendar.MONTH)
				&& a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH);
	}

	// =============================================================================================
	// Validation
	// =============================================================================================

	/**
	 * Returns {@code true} if {@code startCalendar} is strictly before {@code endCalendar}.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return {@code true} if the start is strictly before the end
	 */
	public static boolean isStartBeforeEnd(Calendar startCalendar, Calendar endCalendar) {
		return startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis();
	}

	/**
	 * Returns {@code true} if {@code startCalendar} is before or equal to {@code endCalendar}.
	 *
	 * @param startCalendar the start of the period; must not be {@code null}
	 * @param endCalendar   the end of the period; must not be {@code null}
	 * @return {@code true} if the start is before or at the same instant as the end
	 */
	public static boolean isValidPeriod(Calendar startCalendar, Calendar endCalendar) {
		return startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis();
	}
}