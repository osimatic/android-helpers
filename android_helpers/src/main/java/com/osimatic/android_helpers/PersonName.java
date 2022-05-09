package com.osimatic.android_helpers;

import android.content.res.Resources;

public class PersonName {
	// Name

	public static String ucname(String str) {
		str = Text.capitalize(str.toLowerCase());
		/*
		foreach (array('-', '\'') as $delimiter) {
			if (strpos($string, $delimiter) !== false) {
				$string = implode($delimiter, array_map('ucfirst', explode($delimiter, $string)));
			}
		}
		*/
		return str;
	}

	public static String formatTitleForDisplay(int title, Resources r) {
		if (title == 1) return r.getString(R.string.mr);
		if (title == 2) return r.getString(R.string.mrs);
		return "";
	}

	public static String formatFirstNameForDisplay(String firstName) {
		if (firstName == null || firstName.isEmpty()) {
			return "";
		}
		return ucname(firstName.trim());
	}

	public static String formatLastNameForDisplay(String lastName) {
		if (lastName == null || lastName.isEmpty()) {
			return "";
		}
		return lastName.trim().toUpperCase();
	}

	public static String formatNameForDisplay(String title, String firstName, String lastName, Resources r) {
		String nameDisplay = formatTitleForDisplay(Integer.parseInt(title), r);
		nameDisplay = (nameDisplay+' '+formatFirstNameForDisplay(firstName)).trim();
		nameDisplay = (nameDisplay+' '+formatLastNameForDisplay(lastName)).trim();
		return nameDisplay;
	}
}
