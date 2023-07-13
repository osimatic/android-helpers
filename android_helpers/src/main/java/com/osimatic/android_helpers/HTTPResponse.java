package com.osimatic.android_helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HTTPResponse {
	public static final int HTTP_STATUS_SUCCESS = 200;
	public static final int HTTP_STATUS_CREATED = 201;
	public static final int HTTP_STATUS_UPDATED = 204;
	public static final int HTTP_STATUS_DELETED = 204;

	public interface SuccessListener<T> {
		/** Called when a response is received. */
		void onSuccess(T response);
	}

	public interface ErrorListener {
		/** Called when a response is received. */
		void onError(int httpResponseCode, String data);
	}

	private int statusCode;
	private String data;

	public HTTPResponse(int statusCode, String data) {
		this.statusCode = statusCode;
		this.data = data;
	}

	public JSONObject getJSONObject() {
		try {
			Object json = new JSONTokener(this.getData()).nextValue();
			if (json instanceof JSONObject) {
				return ((JSONObject) json);
			}
			if (json instanceof JSONArray) {
				return ((JSONArray) json).getJSONObject(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
