package com.osimatic.android_helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostalAddress {
	public static Address getAddressFromLocation(Context context, Locale locale, Location location) {
		try {
			Geocoder geocoder = new Geocoder(context, locale);
			List<Address> fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

			if (null == fromLocation) {
				return null;
			}

			return fromLocation.get(0);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	public static String formatAddressForDisplay(Address address) {
		return formatAddressForDisplay(address, ", ");
	}

	public static String formatAddressForDisplay(Address address, String separator) {
		List<String> addressLines = new ArrayList<>();
		for (int i=0; i<=address.getMaxAddressLineIndex(); i++) {
			addressLines.add(address.getAddressLine(i));
		}
		return String.join(separator, addressLines);
		//return formatAddressForDisplay(street, additionalAddress, address.getPostalCode(), address.getLocality(), address.getCountryCode(), true, null);
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
