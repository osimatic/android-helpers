package com.osimatic.android_helpers;

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

public class Form {
	public static void setStyleRadioButton(RadioButton radioButton, int selectedColor) {
		//radioButton.setTextColor(androidx.appcompat.R.attr.showText);
		//radioButtonTypePointage.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.text_noir));
		radioButton.setButtonTintList(ColorStateList.valueOf(selectedColor));
		radioButton.setVisibility(View.VISIBLE);
		radioButton.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public static String getErrorMessage(Object json) {
		try {
			if (json instanceof String) {
				return (String) json;
			}

			if (json instanceof JSONArray) {
				return ((JSONArray) json).getString(1);
			}

			JSONObject errorObject = Form.getErrorObject(json);
			if (null != errorObject && errorObject.has("message")) {
				return errorObject.getString("message");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getErrorKey(Object json) {
		try {
			if (json instanceof JSONArray) {
				return ((JSONArray) json).getString(0);
			}

			JSONObject errorObject = Form.getErrorObject(json);
			if (null != errorObject) {
				return errorObject.getString("error");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static JSONObject getErrorObject(Object json) {
		JSONObject jsonResultObject;
		try {
			if (json instanceof JSONObject && (jsonResultObject = ((JSONObject) json)).has("error")) {
				return jsonResultObject;
			}
			if (json instanceof JSONArray && ((JSONArray) json).length() == 1 && (jsonResultObject = ((JSONArray) json).getJSONObject(0)).has("error")) {
				return jsonResultObject;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getListErrors(String encodedJsonErrors) {
		if (null == encodedJsonErrors) {
			return "";
		}

		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONObject) {
				return Form.getListErrors((JSONObject) json);
			}
			if (json instanceof JSONArray) {
				return Form.getListErrors((JSONArray) json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getListErrors(JSONArray jsonErrors) {
		if (null == jsonErrors) {
			return "";
		}

		String errors = "";
		try {
			for (int i=0; i<jsonErrors.length();i++) {
				errors += getErrorMessage(jsonErrors.get(i))+"\n";
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return errors;
	}

	public static String getListErrors(JSONObject jsonErrors) {
		if (null == jsonErrors) {
			return "";
		}

		String errors = "";

		try {
			for (Iterator<String> it = jsonErrors.keys(); it.hasNext(); ) {
				String key = it.next();
				errors += jsonErrors.getString(key) + "\n";
			}
		} catch (JSONException var3) {
			var3.printStackTrace();
		}

		return errors;
	}

	public static boolean isFormError(String encodedJsonErrors, String[] formErrorKeys) {
		if (null == encodedJsonErrors) {
			return false;
		}

		try {
			Object json = new JSONTokener(encodedJsonErrors).nextValue();
			if (json instanceof JSONObject) {
				return Form.isFormError((JSONObject) json, formErrorKeys);
			}
			if (json instanceof JSONArray) {
				return Form.isFormError((JSONArray) json, formErrorKeys);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean isFormError(JSONArray jsonErrors, String[] formErrorKeys) {
		try {
			for (int i=0; i<jsonErrors.length();i++) {
				if (Arrays.asList(formErrorKeys).contains(getErrorKey(jsonErrors.get(i)))) {
					return true;
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isFormError(JSONObject jsonErrors, String[] formErrorKeys) {
		for (Iterator<String> it = jsonErrors.keys(); it.hasNext(); ) {
			String key = it.next();
			if (Arrays.asList(formErrorKeys).contains(key)) {
				return true;
			}
		}
		return false;
	}

}
