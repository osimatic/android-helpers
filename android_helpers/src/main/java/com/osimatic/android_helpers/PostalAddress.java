package com.osimatic.android_helpers;

public class PostalAddress {
	public static boolean isEmpty(String street, String additionalAddress, String zipCode, String city, String countryIsoCode) {
		return isEmpty(street, additionalAddress, zipCode, city, countryIsoCode, false);
	}

	public static boolean isEmpty(String street, String additionalAddress, String zipCode, String city, String countryIsoCode, boolean ignoreCountry) {
		return
			(null == street || street.isEmpty()) &&
			(null == additionalAddress || additionalAddress.isEmpty()) &&
			(null == zipCode || zipCode.isEmpty()) &&
			(null == city || city.isEmpty()) &&
			(ignoreCountry || (null == countryIsoCode || countryIsoCode.isEmpty()))
		;
	}

	public static String formatAddressForDisplay(String street, String additionalAddress, String zipCode, String city, String countryIsoCode) {
		return formatAddressForDisplay(street, additionalAddress, zipCode, city, countryIsoCode, true, null);
	}

	public static String formatAddressForDisplay(String street, String additionalAddress, String zipCode, String city, String countryIsoCode, boolean upperCase, String sep) {
		sep = (sep==null?"\n":sep);

		String addressDisplay = "";

		// Ajout de la rue
		if (street != null && !street.isEmpty()) {
			street = (upperCase ? street.toUpperCase() : street);
			addressDisplay += street + sep;
		}

		// Ajout du complément d'adresse
		if (additionalAddress != null && !additionalAddress.isEmpty()) {
			additionalAddress = (upperCase ? additionalAddress.toUpperCase() : additionalAddress);
			addressDisplay += additionalAddress + sep;
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

		if (addressDisplay.isEmpty()) {
			return "";
		}

		return addressDisplay.substring(0, (addressDisplay.length()-sep.length()));
	}
}
