package com.osimatic.core_android;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Utility class providing helper methods for currency formatting and currency metadata.
 *
 * <p>Currency codes follow the ISO 4217 standard (three uppercase letters, e.g. {@code "EUR"}, {@code "USD"}).
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see java.util.Currency
 * @see NumberFormat#getCurrencyInstance()
 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
 */
public class Currency {

	private Currency() {}

	// =============================================================================================
	// Formatting
	// =============================================================================================

	/**
	 * Formats the given amount as a localized currency string using the device's default locale.
	 *
	 * <p><b>Note:</b> using {@code double} to represent monetary values may introduce floating-point precision errors. Prefer {@link #format(BigDecimal, String)} for financial calculations.
	 *
	 * @param amount       the monetary amount to format
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @return a formatted currency string (e.g. {@code "1 234,56 €"})
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see NumberFormat#getCurrencyInstance()
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
	 */
	public static String format(double amount, String currencyCode) {
		return format(amount, currencyCode, Locale.getDefault());
	}

	/**
	 * Formats the given amount as a localized currency string using the given locale.
	 *
	 * <p><b>Note:</b> using {@code double} to represent monetary values may introduce floating-point precision errors. Prefer {@link #format(BigDecimal, String, Locale)} for financial calculations.
	 *
	 * @param amount       the monetary amount to format
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @param locale       the locale to use for formatting; must not be {@code null}
	 * @return a formatted currency string
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see NumberFormat#getCurrencyInstance(Locale)
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
	 */
	public static String format(double amount, String currencyCode, Locale locale) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		formatter.setCurrency(java.util.Currency.getInstance(currencyCode));
		return formatter.format(amount);
	}

	/**
	 * Formats the given amount as a localized currency string using the device's default locale.
	 *
	 * <p>Prefer this overload over {@link #format(double, String)} to avoid floating-point precision issues in financial calculations.
	 *
	 * @param amount       the monetary amount to format; must not be {@code null}
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @return a formatted currency string (e.g. {@code "1 234,56 €"})
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see <a href="https://developer.android.com/reference/java/math/BigDecimal">BigDecimal — Android</a>
	 */
	public static String format(BigDecimal amount, String currencyCode) {
		return format(amount, currencyCode, Locale.getDefault());
	}

	/**
	 * Formats the given amount as a localized currency string using the given locale.
	 *
	 * <p>Prefer this overload over {@link #format(double, String, Locale)} to avoid floating-point precision issues in financial calculations.
	 *
	 * @param amount       the monetary amount to format; must not be {@code null}
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @param locale       the locale to use for formatting; must not be {@code null}
	 * @return a formatted currency string
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see <a href="https://developer.android.com/reference/java/math/BigDecimal">BigDecimal — Android</a>
	 */
	public static String format(BigDecimal amount, String currencyCode, Locale locale) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
		formatter.setCurrency(java.util.Currency.getInstance(currencyCode));
		return formatter.format(amount);
	}

	// =============================================================================================
	// Currency info
	// =============================================================================================

	/**
	 * Returns the currency symbol for the given ISO 4217 currency code, as used in the device's default locale.
	 *
	 * <p>For example, {@code "EUR"} may return {@code "€"} or {@code "EUR"} depending on the locale.
	 *
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @return the currency symbol
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see java.util.Currency#getSymbol()
	 */
	public static String getSymbol(String currencyCode) {
		return getSymbol(currencyCode, Locale.getDefault());
	}

	/**
	 * Returns the currency symbol for the given ISO 4217 currency code, as used in the given locale.
	 *
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @param locale       the locale to use for the symbol; must not be {@code null}
	 * @return the currency symbol
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see java.util.Currency#getSymbol(Locale)
	 */
	public static String getSymbol(String currencyCode, Locale locale) {
		return java.util.Currency.getInstance(currencyCode).getSymbol(locale);
	}

	/**
	 * Returns the display name of the currency identified by the given ISO 4217 code, using the device's default locale.
	 *
	 * <p>For example, {@code "EUR"} returns {@code "Euro"}.
	 *
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @return the localized display name of the currency
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see java.util.Currency#getDisplayName()
	 */
	public static String getDisplayName(String currencyCode) {
		return getDisplayName(currencyCode, Locale.getDefault());
	}

	/**
	 * Returns the display name of the currency identified by the given ISO 4217 code, using the given locale.
	 *
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "USD"}); must not be {@code null}
	 * @param locale       the locale to use for the display name; must not be {@code null}
	 * @return the localized display name of the currency
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see java.util.Currency#getDisplayName(Locale)
	 */
	public static String getDisplayName(String currencyCode, Locale locale) {
		return java.util.Currency.getInstance(currencyCode).getDisplayName(locale);
	}

	/**
	 * Returns the default number of fraction digits for the given ISO 4217 currency code.
	 *
	 * <p>For example, {@code "EUR"} returns {@code 2} and {@code "JPY"} returns {@code 0}.
	 *
	 * @param currencyCode an ISO 4217 currency code (e.g. {@code "EUR"}, {@code "JPY"}); must not be {@code null}
	 * @return the default number of fraction digits
	 * @throws IllegalArgumentException if {@code currencyCode} is not a supported ISO 4217 code
	 * @see java.util.Currency#getDefaultFractionDigits()
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
	 */
	public static int getDefaultFractionDigits(String currencyCode) {
		return java.util.Currency.getInstance(currencyCode).getDefaultFractionDigits();
	}

	// =============================================================================================
	// Validation / utilities
	// =============================================================================================

	/**
	 * Returns {@code true} if the given string is a valid ISO 4217 currency code.
	 *
	 * @param currencyCode the string to validate; may be {@code null}
	 * @return {@code true} if {@code currencyCode} is a recognized ISO 4217 code, {@code false} otherwise
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
	 */
	public static boolean isValidCurrencyCode(String currencyCode) {
		if (currencyCode == null) {
			return false;
		}
		try {
			java.util.Currency.getInstance(currencyCode);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Returns a list of all ISO 4217 currency codes available on the current JVM.
	 *
	 * @return a list of three-letter ISO 4217 currency codes
	 * @see java.util.Currency#getAvailableCurrencies()
	 * @see <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO 4217 — Wikipedia</a>
	 */
	public static List<String> getAvailableCurrencyCodes() {
		Set<java.util.Currency> currencies = java.util.Currency.getAvailableCurrencies();
		List<String> codes = new ArrayList<>(currencies.size());
		for (java.util.Currency currency : currencies) {
			codes.add(currency.getCurrencyCode());
		}
		return codes;
	}
}