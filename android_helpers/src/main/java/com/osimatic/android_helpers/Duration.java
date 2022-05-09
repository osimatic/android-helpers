package com.osimatic.android_helpers;

import java.util.Locale;

public class Duration {
	public static String formatNbDays(double nbDays) {
		return formatNbDays(nbDays, false);
	}
	public static String formatNbDays(double nbDays, boolean withSign) {
		return String.format(Locale.FRANCE, "%s %.2f", (nbDays>=0 ? (withSign?"+":"") : "-"), Math.abs(nbDays));
	}

	public static String formatNbHours(double nbSeconds) {
		return formatNbHours((long)nbSeconds, false);
	}
	public static String formatNbHours(double nbSeconds, boolean withSign) {
		return formatNbHours((long)nbSeconds, withSign);
	}
	public static String formatNbHours(long nbSeconds) {
		return formatNbHours(nbSeconds, false);
	}
	public static String formatNbHours(long nbSeconds, boolean withSign) {
		return (nbSeconds>=0 ? (withSign?"+":"") : "-")+' '+ displayInHourChrono(Math.abs(nbSeconds));
	}



	public static String displayInInputTime(long duration) {
		return String.format(Locale.FRANCE, "%02d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
	}

	public static String displayInHourChrono(long duration) {
		return displayInHourChrono(duration, true);
	}

	public static String displayInHourChrono(long duration, boolean withSeconds) {
		if (withSeconds) {
			return String.format(Locale.FRANCE, "%02d:%02d.%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
		}
		return String.format(Locale.FRANCE, "%02d:%02d", duration / 3600, (duration % 3600) / 60);
	}

	public static String displayInMinuteChrono(long duration) {
		return String.format(Locale.FRANCE, "%02d.%02d", duration / 60, (duration % 60));
		/* return String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toMinutes((long) duration),
				TimeUnit.MILLISECONDS.toSeconds((long) duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration))
		); */
	}
}
