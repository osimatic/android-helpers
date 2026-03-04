package com.osimatic.android_helpers;

import java.util.Locale;

/**
 * Utility class providing helper methods for formatting durations.
 *
 * <p>Unless otherwise noted, duration parameters are expressed in <b>seconds</b>.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see java.util.concurrent.TimeUnit
 */
public class Duration {

	private Duration() {}

	// =============================================================================================
	// Days formatting
	// =============================================================================================

	/**
	 * Formats the given number of days as a decimal string using the default locale.
	 *
	 * <pre>
	 * formatDays(3.5)         = "3.50"
	 * formatDays(-3.5)        = "-3.50"
	 * formatDays(3.5, true)   = "+3.50"
	 * formatDays(-3.5, true)  = "-3.50"
	 * </pre>
	 *
	 * @param nbDays the number of days to format
	 * @return a formatted string with 2 decimal places
	 * @see #formatDays(double, boolean)
	 */
	public static String formatDays(double nbDays) {
		return formatDays(nbDays, false);
	}

	/**
	 * Formats the given number of days as a decimal string using the default locale, optionally prefixed with a sign.
	 *
	 * @param nbDays    the number of days to format
	 * @param withSign  {@code true} to prefix positive values with {@code "+"}
	 * @return a formatted string with 2 decimal places, optionally signed (e.g. {@code "+3.50"} or {@code "-1.25"})
	 */
	public static String formatDays(double nbDays, boolean withSign) {
		String sign = nbDays >= 0 ? (withSign ? "+" : "") : "-";
		return String.format(Locale.getDefault(), "%s%.2f", sign, Math.abs(nbDays));
	}

	// =============================================================================================
	// Time formatting
	// =============================================================================================

	/**
	 * Formats the given duration in seconds as an hours string (e.g. {@code "01:30.45"}), without a sign prefix.
	 *
	 * @param nbSeconds the duration in seconds
	 * @return a formatted hours string
	 * @see #formatHours(long, boolean)
	 */
	public static String formatHours(long nbSeconds) {
		return formatHours(nbSeconds, false);
	}

	/**
	 * Formats the given duration in seconds as an hours string, optionally prefixed with a sign.
	 *
	 * @param nbSeconds the duration in seconds
	 * @param withSign  {@code true} to prefix positive values with {@code "+"}
	 * @return a formatted hours string (e.g. {@code "+ 01:30.45"} or {@code "- 01:30.45"})
	 * @see #toHourChronoString(long)
	 */
	public static String formatHours(long nbSeconds, boolean withSign) {
		String sign = nbSeconds >= 0 ? (withSign ? "+" : "") : "-";
		return sign + ' ' + toHourChronoString(Math.abs(nbSeconds));
	}

	/**
	 * Returns a human-readable string for the given duration in seconds, using hours, minutes, and seconds components.
	 *
	 * <pre>
	 * toHumanReadable(3723)  = "1h 2min 3s"
	 * toHumanReadable(120)   = "2min"
	 * toHumanReadable(45)    = "45s"
	 * toHumanReadable(3600)  = "1h"
	 * </pre>
	 *
	 * @param seconds the duration in seconds; must be ≥ 0
	 * @return a human-readable duration string
	 */
	public static String toHumanReadable(long seconds) {
		long h   = seconds / 3600;
		long min = (seconds % 3600) / 60;
		long s   = seconds % 60;
		StringBuilder sb = new StringBuilder();
		if (h   > 0) sb.append(h).append("h");
		if (min > 0) { if (sb.length() > 0) sb.append(' '); sb.append(min).append("min"); }
		if (s   > 0 || sb.length() == 0) { if (sb.length() > 0) sb.append(' '); sb.append(s).append("s"); }
		return sb.toString();
	}

	// =============================================================================================
	// Chrono formatting
	// =============================================================================================

	/**
	 * Formats the given duration in seconds as an input time string in {@code HH:mm:ss} format.
	 *
	 * <pre>
	 * toInputTimeString(3723)  = "01:02:03"
	 * toInputTimeString(3600)  = "01:00:00"
	 * </pre>
	 *
	 * @param duration the duration in seconds; must be ≥ 0
	 * @return a string in {@code HH:mm:ss} format
	 */
	public static String toInputTimeString(long duration) {
		return String.format(Locale.getDefault(), "%02d:%02d:%02d",
				duration / 3600,
				(duration % 3600) / 60,
				duration % 60);
	}

	/** @deprecated Use {@link #toInputTimeString(long)} instead. */
	@Deprecated
	public static String displayInInputTime(long duration) {
		return toInputTimeString(duration);
	}

	/**
	 * Formats the given duration in seconds as a chrono string in {@code HH:mm} or {@code HH:mm.ss} format.
	 *
	 * <pre>
	 * toHourChronoString(3723, true)   = "01:02.03"
	 * toHourChronoString(3723, false)  = "01:02"
	 * </pre>
	 *
	 * @param duration    the duration in seconds; must be ≥ 0
	 * @param withSeconds {@code true} to include seconds separated by {@code "."}
	 * @return a chrono string
	 * @see #toHourChronoString(long)
	 */
	public static String toHourChronoString(long duration, boolean withSeconds) {
		if (withSeconds) {
			return String.format(Locale.getDefault(), "%02d:%02d.%02d",
					duration / 3600,
					(duration % 3600) / 60,
					duration % 60);
		}
		return String.format(Locale.getDefault(), "%02d:%02d",
				duration / 3600,
				(duration % 3600) / 60);
	}

	/**
	 * Formats the given duration in seconds as a chrono string in {@code HH:mm.ss} format.
	 *
	 * @param duration the duration in seconds; must be ≥ 0
	 * @return a chrono string with seconds (e.g. {@code "01:02.03"})
	 * @see #toHourChronoString(long, boolean)
	 */
	public static String toHourChronoString(long duration) {
		return toHourChronoString(duration, true);
	}

	/** @deprecated Use {@link #toHourChronoString(long)} instead. */
	@Deprecated
	public static String displayInHourChrono(long duration) {
		return toHourChronoString(duration, true);
	}

	/** @deprecated Use {@link #toHourChronoString(long, boolean)} instead. */
	@Deprecated
	public static String displayInHourChrono(long duration, boolean withSeconds) {
		return toHourChronoString(duration, withSeconds);
	}

	/**
	 * Formats the given duration in seconds as a minute chrono string in {@code mm.ss} format.
	 *
	 * <pre>
	 * toMinuteChronoString(185)  = "03.05"
	 * toMinuteChronoString(60)   = "01.00"
	 * </pre>
	 *
	 * @param duration the duration in seconds; must be ≥ 0
	 * @return a chrono string in {@code mm.ss} format
	 */
	public static String toMinuteChronoString(long duration) {
		return String.format(Locale.getDefault(), "%02d.%02d",
				duration / 60,
				duration % 60);
	}

	/** @deprecated Use {@link #toMinuteChronoString(long)} instead. */
	@Deprecated
	public static String displayInMinuteChrono(long duration) {
		return toMinuteChronoString(duration);
	}
}