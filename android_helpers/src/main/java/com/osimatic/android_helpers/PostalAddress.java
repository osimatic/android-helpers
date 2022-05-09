package com.osimatic.android_helpers;

public class PostalAddress {
	public static String formatAddressForDisplay(String street1, String street2, String zipCode, String city, String countryIsoCode) {
		return formatAddressForDisplay(street1, street2, zipCode, city, countryIsoCode, true, null);
	}

	public static String formatAddressForDisplay(String street1, String street2, String zipCode, String city, String countryIsoCode, boolean upperCase, String sep) {
		sep = (sep==null?"\n":sep);

		String addressDisplay = "";

		// Ajout de la rue (ligne 1)
		if (street1 != null && !street1.isEmpty()) {
			street1 = (upperCase ? street1.toUpperCase() : street1);
			addressDisplay += street1 + sep;
		}

		// Ajout de la rue (ligne 2)
		if (street2 != null && !street2.isEmpty()) {
			street2 = (upperCase ? street2.toUpperCase() : street2);
			addressDisplay += street2 + sep;
		}

		// Ajout du code postal et ville
		if ((zipCode != null && !zipCode.isEmpty()) || (city != null && !city.isEmpty())) {
			if (zipCode != null && !zipCode.isEmpty()) {
				zipCode = (upperCase ? zipCode.toUpperCase() : zipCode);
			}
			else {
				zipCode = "";
			}
			if (city != null && !city.isEmpty()) {
				city = (upperCase ? city.toUpperCase() : city);
			}
			else {
				city = "";
			}
			addressDisplay += zipCode + " " + city + sep;
		}

		// Ajout du pays
		if (countryIsoCode != null && !countryIsoCode.isEmpty()) {
			String countryName = Country.getCountryNameFromCountryCode(countryIsoCode);
			countryName = (upperCase ? countryName.toUpperCase() : countryName);
			addressDisplay += countryName + sep;
		}

		return addressDisplay.substring(0, (addressDisplay.length()-sep.length()));
	}
}
