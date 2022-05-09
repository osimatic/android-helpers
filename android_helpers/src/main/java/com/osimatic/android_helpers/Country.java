package com.osimatic.android_helpers;

import java.util.Locale;

public class Country {
	public static String getCountryNameFromCountryCode(String countryCode) {
		Locale l = new Locale("", countryCode);
		return l.getDisplayCountry(Locale.getDefault());
	}
}
