package com.osimatic.core_android;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.appcompat.app.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class providing helper methods for form UI styling and JSON-based error handling.
 *
 * <p>The JSON error format supported by this class is either a {@link JSONObject} with {@code "error"} and {@code "message"} fields, or a {@link JSONArray} where each element follows that format or is a two-element array {@code [key, message]}.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see org.json.JSONObject
 * @see org.json.JSONArray
 */
public class Form {

	private Form() {}

	// =============================================================================================
	// UI utilities
	// =============================================================================================

	/**
	 * Applies a tint color to the given {@link RadioButton}'s button drawable and makes it visible with {@code MATCH_PARENT} width.
	 *
	 * @param radioButton   the radio button to style; must not be {@code null}
	 * @param selectedColor the ARGB color to apply as the button tint
	 * @see ColorStateList#valueOf(int)
	 */
	public static void setStyleRadioButton(RadioButton radioButton, int selectedColor) {
		radioButton.setButtonTintList(ColorStateList.valueOf(selectedColor));
		radioButton.setVisibility(View.VISIBLE);
		radioButton.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	// =============================================================================================
	// JSON error parsing
	// =============================================================================================

	/**
	 * Returns {@code true} if the given JSON-encoded error string contains at least one error.
	 *
	 * <p>Returns {@code false} if {@code encodedJsonErrors} is {@code null}, empty, or cannot be parsed.
	 *
	 * @param encodedJsonErrors a JSON string representing one or more errors; may be {@code null}
	 * @return {@code true} if errors are present, {@code false} otherwise
	 * @see #getListErrors(String)
	 */
	public static boolean hasErrors(String encodedJsonErrors) {
		if (encodedJsonErrors == null || encodedJsonErrors.isEmpty()) {
			return false;
		}
		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONArray) return ((JSONArray) json).length() > 0;
			if (json instanceof JSONObject) return ((JSONObject) json).has("error");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns the error message extracted from the given JSON error object.
	 *
	 * <p>Supported formats:
	 * <ul>
	 *   <li>A plain {@link String} — returned as-is.</li>
	 *   <li>A {@link JSONArray} of the form {@code [key, message]} — returns the element at index 1.</li>
	 *   <li>A {@link JSONObject} with an {@code "error"} field and an optional {@code "message"} field — returns {@code "message"}.</li>
	 * </ul>
	 *
	 * @param json the JSON error object; may be a {@link String}, {@link JSONArray}, or {@link JSONObject}
	 * @return the error message string, or {@code null} if none can be extracted
	 * @see #getErrorKey(Object)
	 */
	public static String getErrorMessage(Object json) {
		try {
			if (json instanceof String) {
				return (String) json;
			}
			if (json instanceof JSONArray) {
				return ((JSONArray) json).getString(1);
			}
			JSONObject errorObject = getErrorObject(json);
			if (errorObject != null && errorObject.has("message")) {
				return errorObject.getString("message");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the error key extracted from the given JSON error object.
	 *
	 * <p>Supported formats:
	 * <ul>
	 *   <li>A {@link JSONArray} of the form {@code [key, message]} — returns the element at index 0.</li>
	 *   <li>A {@link JSONObject} with an {@code "error"} field — returns the value of that field.</li>
	 * </ul>
	 *
	 * @param json the JSON error object; may be a {@link JSONArray} or {@link JSONObject}
	 * @return the error key string, or {@code null} if none can be extracted
	 * @see #getErrorMessage(Object)
	 */
	public static String getErrorKey(Object json) {
		try {
			if (json instanceof JSONArray) {
				return ((JSONArray) json).getString(0);
			}
			JSONObject errorObject = getErrorObject(json);
			if (errorObject != null && errorObject.has("error")) {
				return errorObject.getString("error");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the number of errors in the given {@link JSONArray}.
	 *
	 * @param jsonErrors the JSON array of errors; must not be {@code null}
	 * @return the number of error entries in the array
	 */
	public static int getErrorCount(JSONArray jsonErrors) {
		return jsonErrors.length();
	}

	/**
	 * Returns the number of errors in the given JSON-encoded error string.
	 *
	 * <p>Returns {@code 0} if the string is {@code null}, empty, cannot be parsed, or contains no errors.
	 *
	 * @param encodedJsonErrors a JSON string representing one or more errors; may be {@code null}
	 * @return the number of error entries, or {@code 0} if none
	 */
	public static int getErrorCount(String encodedJsonErrors) {
		if (encodedJsonErrors == null || encodedJsonErrors.isEmpty()) return 0;
		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONArray) return ((JSONArray) json).length();
			if (json instanceof JSONObject) return ((JSONObject) json).has("error") ? 1 : 0;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Returns a newline-separated string of all error messages from the given JSON-encoded error string.
	 *
	 * <p>Returns an empty string if {@code encodedJsonErrors} is {@code null} or cannot be parsed.
	 *
	 * @param encodedJsonErrors a JSON string representing one or more errors; may be {@code null}
	 * @return a newline-separated list of error messages, or {@code ""}
	 * @see #getListErrors(JSONArray)
	 * @see #getListErrors(JSONObject)
	 */
	public static String getListErrors(String encodedJsonErrors) {
		if (encodedJsonErrors == null) {
			return "";
		}
		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONObject) return getListErrors((JSONObject) json);
			if (json instanceof JSONArray)  return getListErrors((JSONArray) json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Returns a newline-separated string of all error messages from the given {@link JSONArray}.
	 *
	 * <p>Entries whose message is {@code null} are skipped. Returns an empty string if {@code jsonErrors} is {@code null}.
	 *
	 * @param jsonErrors the JSON array of errors; may be {@code null}
	 * @return a newline-separated list of error messages, or {@code ""}
	 */
	public static String getListErrors(JSONArray jsonErrors) {
		if (jsonErrors == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < jsonErrors.length(); i++) {
				String message = getErrorMessage(jsonErrors.get(i));
				if (message != null) {
					sb.append(message).append('\n');
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Returns a newline-separated string of all error messages from the given {@link JSONObject}.
	 *
	 * <p>Each value of the object is treated as an error message. Returns an empty string if {@code jsonErrors} is {@code null}.
	 *
	 * @param jsonErrors the JSON object of errors; may be {@code null}
	 * @return a newline-separated list of error messages, or {@code ""}
	 */
	public static String getListErrors(JSONObject jsonErrors) {
		if (jsonErrors == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			for (Iterator<String> it = jsonErrors.keys(); it.hasNext(); ) {
				String key = it.next();
				sb.append(jsonErrors.getString(key)).append('\n');
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Returns {@code true} if the given JSON-encoded error string contains at least one error whose key matches one of the given form field keys.
	 *
	 * @param encodedJsonErrors a JSON string representing one or more errors; may be {@code null}
	 * @param formErrorKeys     the field keys to match against; must not be {@code null}
	 * @return {@code true} if any error key is contained in {@code formErrorKeys}
	 * @see #isFormError(JSONArray, String[])
	 * @see #isFormError(JSONObject, String[])
	 */
	public static boolean isFormError(String encodedJsonErrors, String[] formErrorKeys) {
		if (encodedJsonErrors == null) {
			return false;
		}
		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONObject) return isFormError((JSONObject) json, formErrorKeys);
			if (json instanceof JSONArray)  return isFormError((JSONArray) json, formErrorKeys);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns {@code true} if the given {@link JSONArray} contains at least one error whose key matches one of the given form field keys.
	 *
	 * @param jsonErrors    the JSON array of errors; must not be {@code null}
	 * @param formErrorKeys the field keys to match against; must not be {@code null}
	 * @return {@code true} if any error key is contained in {@code formErrorKeys}
	 */
	public static boolean isFormError(JSONArray jsonErrors, String[] formErrorKeys) {
		List<String> keys = Arrays.asList(formErrorKeys);
		try {
			for (int i = 0; i < jsonErrors.length(); i++) {
				if (keys.contains(getErrorKey(jsonErrors.get(i)))) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns {@code true} if the given {@link JSONObject} contains at least one key that matches one of the given form field keys.
	 *
	 * @param jsonErrors    the JSON object of errors; must not be {@code null}
	 * @param formErrorKeys the field keys to match against; must not be {@code null}
	 * @return {@code true} if any key of {@code jsonErrors} is contained in {@code formErrorKeys}
	 */
	public static boolean isFormError(JSONObject jsonErrors, String[] formErrorKeys) {
		List<String> keys = Arrays.asList(formErrorKeys);
		for (Iterator<String> it = jsonErrors.keys(); it.hasNext(); ) {
			if (keys.contains(it.next())) {
				return true;
			}
		}
		return false;
	}

	// =============================================================================================
	// Private helpers
	// =============================================================================================

	/**
	 * Extracts the innermost JSON error object from the given value.
	 *
	 * <p>Accepts a {@link JSONObject} with an {@code "error"} field, or a single-element {@link JSONArray} whose first element is such an object.
	 *
	 * @param json the value to inspect; may be a {@link JSONObject} or {@link JSONArray}
	 * @return the error {@link JSONObject}, or {@code null} if not found
	 */
	private static JSONObject getErrorObject(Object json) {
		JSONObject jsonResultObject;
		try {
			if (json instanceof JSONObject && (jsonResultObject = (JSONObject) json).has("error")) {
				return jsonResultObject;
			}
			if (json instanceof JSONArray
					&& ((JSONArray) json).length() == 1
					&& (jsonResultObject = ((JSONArray) json).getJSONObject(0)).has("error")) {
				return jsonResultObject;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}