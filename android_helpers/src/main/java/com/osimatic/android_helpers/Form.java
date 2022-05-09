package com.osimatic.android_helpers;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.appcompat.app.ActionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Form {
	public static void setStyleRadioButton(RadioButton radioButton, int selectedColor) {
		radioButton.setTextColor(androidx.appcompat.R.attr.showText);
		//radioButtonTypePointage.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.text_noir));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			radioButton.setButtonTintList(ColorStateList.valueOf(selectedColor));
		}
		radioButton.setVisibility(View.VISIBLE);
		radioButton.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public static String getErrorMessage(Object json) {
		try {
			JSONObject errorObject = Form.getErrorJsonObject(json);
			if (null != errorObject) {
				return errorObject.getString("error_description");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getErrorKey(Object json) {
		try {
			JSONObject errorObject = Form.getErrorJsonObject(json);
			if (null != errorObject) {
				return errorObject.getString("error");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static JSONObject getErrorJsonObject(Object json) {
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

	public static String getListErrors(JSONArray jsonArray) {
		String errors = "";
		try {
			for (int i=0; i<jsonArray.length();i++) {
				errors += jsonArray.getJSONObject(i).getString("error_description");
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return errors;
	}
}
