package com.osimatic.android_helpers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class HTTPRequest {
	private static final String TAG = Config.START_TAG+"HTTPRequest";

	public static HTTPResponse get(String url) {
		HashMap<String, String> data = new HashMap<String, String>();
		return HTTPRequest.get(url, data);
	}

	public static HTTPResponse get(String url, HashMap<String, String> data) {
		HashMap<String, String> headers = new HashMap<String, String>();
		return HTTPRequest.get(url, data, headers);
	}

	public static HTTPResponse get(String url, HashMap<String, String> data, HashMap<String, String> headers) {
		String result = "";
		int status = 0;
		BufferedReader reader = null;

		try {
			url += (url.contains("?")?"":"?") + com.osimatic.android_helpers.URL.buildQueryString(data);

			Log.d(TAG, "URL : "+url);

			// création de la connection et envoi de la requête
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

			// HTTP Headers
			HTTPRequest.setHttpHeaders(conn, headers);
			//conn.setRequestProperty("Accept-Language", "en-GB");

			// récupération du statut de la réponse
			status = conn.getResponseCode();

			// lecture de la réponse
			InputStream is = null;
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
				is = conn.getErrorStream();
			}
			else {
				is = conn.getInputStream();
			}

			reader = new BufferedReader(
					new InputStreamReader(is)
			);

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				result += inputLine;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try{
				if (reader != null) {
					reader.close();
				}
			}
			catch(Exception e){
				Log.d(TAG, e.getMessage());
			}
		}

		Log.d(TAG, "HTTP Status : " + status + " ; JSON : " + result);
		return new HTTPResponse(status, result);
	}

	public static HTTPResponse post(String url, HashMap<String, String> data) {
		HashMap<String, String> headers = new HashMap<String, String>();
		return HTTPRequest.post(url, data, headers, false);
	}

	public static HTTPResponse post(String url, HashMap<String, String> data, HashMap<String, String> headers) {
		return HTTPRequest.post(url, data, headers, false);
	}

	public static HTTPResponse post(String url, HashMap<String, String> data, HashMap<String, String> headers, boolean dataAsJson) {
		String result = "";
		int status = 0;
		//OutputStreamWriter writer = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;

		try {
			// encodage des paramètres de la requête
			String strData = "";
			if (dataAsJson) {
				JSONObject jsonObject = new JSONObject(data);
				strData = jsonObject.toString();

				headers.put("Content-Type", "application/json");
			}
			else {
				strData = com.osimatic.android_helpers.URL.buildQueryString(data);
			}

			Log.d(TAG, "URL : "+url+" ; POST data : "+strData);

			// création de la connection
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			//URLConnection conn = urlObj.openConnection();
			conn.setDoOutput(true);

			// HTTP Headers
			HTTPRequest.setHttpHeaders(conn, headers);
			//conn.setRequestProperty("Accept-Language", "en-GB");

			// envoi de la requête
			//writer = new OutputStreamWriter(conn.getOutputStream());
			writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			writer.write(strData);
			writer.flush();

			// récupération du statut de la réponse
			status = conn.getResponseCode();

			//lecture de la réponse
			InputStream is = null;
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
				is = conn.getErrorStream();
			}
			else {
				is = conn.getInputStream();
			}
			reader = new BufferedReader(new InputStreamReader(is));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				result += ligne;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try{
				if (writer != null) {
					writer.close();
				}
			}
			catch(Exception e){
				Log.d(TAG, e.getMessage());
			}
			try{
				if (reader != null) {
					reader.close();
				}
			}
			catch(Exception e){
				Log.d(TAG, e.getMessage());
			}
		}

		Log.d(TAG, "HTTP Status : " + status + " ; JSON : " + result);
		return new HTTPResponse(status, result);
	}

	public static void setHttpHeaders(HttpURLConnection conn, HashMap<String, String> headers) {
		//Locale current = getResources().getConfiguration().locale;
		conn.setRequestProperty("Accept-Language", Locale.getDefault().toString().replace("_", "-"));
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			conn.setRequestProperty(key, headers.get(key));
		}
	}
}
