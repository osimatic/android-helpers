package com.osimatic.android_helpers.duration_picker;

public class DurationUtils {
	/** The number of milliseconds within a minute. */
	public static final int SECONDS_PER_MINUTE = 60;
	/** The number of milliseconds within an hour. */
	public static final int SECONDS_PER_HOUR = 60 * SECONDS_PER_MINUTE;

	/**
	 * Calculates the number of hours within the specified duration.
	 * @param duration duration in milliseconds
	 * @return number of hours within the specified duration.
	 */
	public static int hoursOf(long duration) {
		return (int) duration / SECONDS_PER_HOUR;
	}

	/**
	 * Calculates the full number of minutes within the specified duration.
	 * @param duration duration in seconds
	 * @return number of minutes within the specified duration.
	 */
	public static int minutesOf(long duration) {
		return (int) duration / SECONDS_PER_MINUTE;
	}

	/**
	 * Calculates the number of minutes within the specified duration excluding full hours.
	 * @param duration duration in seconds
	 * @return number of minutes within the specified duration.
	 */
	public static int minutesInHourOf(long duration) {
		return (int) (duration - hoursOf(duration) * SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
	}

	/**
	 * Calculates the number of seconds within the specified duration excluding full minutes.
	 * @param duration duration in seconds
	 * @return number of seconds within the specified duration.
	 */
	public static int secondsInMinuteOf(long duration) {
		return (int) (duration - (hoursOf(duration) * SECONDS_PER_HOUR) - (minutesInHourOf(duration) * SECONDS_PER_MINUTE));
	}

	/**
	 * Calculates a duration from hours, minutes and seconds.
	 * @param hours full hours of the duration
	 * @param minutes full minutes of the duration
	 * @param seconds full seconds of the duration
	 * @return duration in seconds.
	 */
	public static long durationOf(int hours, int minutes, int seconds) {
		return (hours * SECONDS_PER_HOUR) + (minutes * SECONDS_PER_MINUTE) + seconds;
	}

	/**
	 * Returns a string representing the specified duration in the format {@code h:mm:ss}.
	 * @param duration duration in seconds
	 * @return string representation of the duration.
	 */
	public static String formatHoursMinutesSeconds(long duration) {
		return String.format("%d:%02d:%02d", hoursOf(duration), minutesInHourOf(duration), secondsInMinuteOf(duration));
	}

	/**
	 * Returns a string representing the specified duration in the format {@code m:ss}.
	 * @param duration duration in seconds
	 * @return string representation of the duration.
	 */
	public static String formatMinutesSeconds(long duration) {
		return String.format("%d:%02d", minutesOf(duration), secondsInMinuteOf(duration));
	}

	/**
	 * Returns a string representing the specified duration in the format {@code s}.
	 * @param duration duration in seconds
	 * @return string representation of the duration.
	 */
	public static String formatSeconds(long duration) {
		return String.format("%d", secondsInMinuteOf(duration));
	}
}
