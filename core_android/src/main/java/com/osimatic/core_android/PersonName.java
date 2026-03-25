package com.osimatic.core_android;

import android.content.res.Resources;

/**
 * Utility class providing helper methods for formatting and displaying person names.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see Text
 */
public class PersonName {

	private PersonName() {}

	// =============================================================================================
	// Formatting
	// =============================================================================================

	/**
	 * Formats a name string with proper casing: converts to lowercase, then capitalizes the first letter of each word delimited by hyphens or apostrophes.
	 *
	 * <pre>
	 * formatNameCase("JEAN-PIERRE")  = "Jean-Pierre"
	 * formatNameCase("o'brien")      = "O'Brien"
	 * formatNameCase("marie")        = "Marie"
	 * formatNameCase(null)           = null
	 * </pre>
	 *
	 * @param name the name to format; may be {@code null}
	 * @return the name with proper casing, or {@code null} if input is {@code null}
	 */
	public static String formatNameCase(String name) {
		if (name == null) return null;
		String lower = name.toLowerCase();
		StringBuilder sb = new StringBuilder(lower.length());
		boolean capitalizeNext = true;
		for (int i = 0; i < lower.length(); i++) {
			char c = lower.charAt(i);
			sb.append(capitalizeNext ? Character.toUpperCase(c) : c);
			capitalizeNext = (c == '-' || c == '\'');
		}
		return sb.toString();
	}

	/** @deprecated Use {@link #formatNameCase(String)} instead. */
	@Deprecated
	public static String ucname(String str) {
		return formatNameCase(str);
	}

	/**
	 * Returns the localized display string for the given civility title.
	 *
	 * <p>Recognized values: {@code 1} = Mr., {@code 2} = Mrs. Any other value returns an empty string.
	 *
	 * @param title the title code (1 = Mr., 2 = Mrs.)
	 * @param r     the {@link Resources} used to retrieve the localized string; must not be {@code null}
	 * @return the localized title string, or an empty string if the title code is not recognized
	 */
	public static String formatTitleForDisplay(int title, Resources r) {
		if (title == 1) return r.getString(R.string.mr);
		if (title == 2) return r.getString(R.string.mrs);
		return "";
	}

	/**
	 * Returns the formatted display string for the given first name, or an empty string if blank.
	 *
	 * <p>The name is trimmed and formatted with {@link #formatNameCase(String)}.
	 *
	 * <pre>
	 * formatFirstNameForDisplay("JEAN-PIERRE")  = "Jean-Pierre"
	 * formatFirstNameForDisplay("  marie  ")    = "Marie"
	 * formatFirstNameForDisplay(null)           = ""
	 * </pre>
	 *
	 * @param firstName the first name to format; may be {@code null}
	 * @return the formatted first name, or an empty string if {@code null} or blank
	 */
	public static String formatFirstNameForDisplay(String firstName) {
		if (firstName == null || firstName.isEmpty()) return "";
		return formatNameCase(firstName.trim());
	}

	/**
	 * Returns the formatted display string for the given last name, or an empty string if blank.
	 *
	 * <p>The name is trimmed and converted to uppercase.
	 *
	 * <pre>
	 * formatLastNameForDisplay("dupont")  = "DUPONT"
	 * formatLastNameForDisplay(null)      = ""
	 * </pre>
	 *
	 * @param lastName the last name to format; may be {@code null}
	 * @return the last name in uppercase, or an empty string if {@code null} or blank
	 */
	public static String formatLastNameForDisplay(String lastName) {
		if (lastName == null || lastName.isEmpty()) return "";
		return lastName.trim().toUpperCase();
	}

	/**
	 * Returns the formatted full name for display, combining title, first name, and last name.
	 *
	 * <pre>
	 * formatNameForDisplay(1, "jean", "dupont", r)  = "M. Jean DUPONT"
	 * formatNameForDisplay(0, "jean", "dupont", r)  = "Jean DUPONT"
	 * </pre>
	 *
	 * @param title     the title code (1 = Mr., 2 = Mrs.; 0 or unknown = no title)
	 * @param firstName the first name; may be {@code null}
	 * @param lastName  the last name; may be {@code null}
	 * @param r         the {@link Resources} used to retrieve the localized title; must not be {@code null}
	 * @return the formatted full name for display
	 * @see #formatTitleForDisplay(int, Resources)
	 * @see #formatFirstNameForDisplay(String)
	 * @see #formatLastNameForDisplay(String)
	 */
	public static String formatNameForDisplay(int title, String firstName, String lastName, Resources r) {
		String display = formatTitleForDisplay(title, r);
		display = (display + ' ' + formatFirstNameForDisplay(firstName)).trim();
		display = (display + ' ' + formatLastNameForDisplay(lastName)).trim();
		return display;
	}

	/**
	 * Returns the formatted full name for display, combining title (as a string), first name, and last name.
	 *
	 * @param title     the title code as a string (e.g. {@code "1"} for Mr., {@code "2"} for Mrs.); may be {@code null}
	 * @param firstName the first name; may be {@code null}
	 * @param lastName  the last name; may be {@code null}
	 * @param r         the {@link Resources} used to retrieve the localized title; must not be {@code null}
	 * @return the formatted full name for display
	 * @deprecated Use {@link #formatNameForDisplay(int, String, String, Resources)} instead.
	 */
	@Deprecated
	public static String formatNameForDisplay(String title, String firstName, String lastName, Resources r) {
		int titleInt = 0;
		if (title != null && !title.isEmpty()) {
			try {
				titleInt = Integer.parseInt(title);
			} catch (NumberFormatException ignored) {}
		}
		return formatNameForDisplay(titleInt, firstName, lastName, r);
	}

	/**
	 * Returns the formatted full name for display, combining first name and last name without a title.
	 *
	 * <pre>
	 * formatFullName("jean", "dupont")  = "Jean DUPONT"
	 * formatFullName(null, "dupont")    = "DUPONT"
	 * formatFullName("jean", null)      = "Jean"
	 * </pre>
	 *
	 * @param firstName the first name; may be {@code null}
	 * @param lastName  the last name; may be {@code null}
	 * @return the formatted full name for display
	 * @see #formatNameForDisplay(int, String, String, Resources)
	 */
	public static String formatFullName(String firstName, String lastName) {
		String display = formatFirstNameForDisplay(firstName);
		display = (display + ' ' + formatLastNameForDisplay(lastName)).trim();
		return display;
	}

	/**
	 * Returns the initials of the given person as a string of uppercase letters.
	 *
	 * <pre>
	 * initials("Jean", "Dupont")  = "JD"
	 * initials(null, "Dupont")    = "D"
	 * initials("Jean", null)      = "J"
	 * initials(null, null)        = ""
	 * </pre>
	 *
	 * @param firstName the first name; may be {@code null}
	 * @param lastName  the last name; may be {@code null}
	 * @return a string of uppercase initials; never {@code null}
	 * @see Text#initials(String)
	 */
	public static String initials(String firstName, String lastName) {
		String first = (firstName != null && !firstName.isEmpty()) ? firstName : "";
		String last = (lastName != null && !lastName.isEmpty()) ? lastName : "";
		return Text.initials((first + " " + last).trim());
	}
}