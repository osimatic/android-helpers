package com.osimatic.android_helpers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

public class HTTPRequest {
	private static final String TAG = Config.START_TAG+"HTTPRequest";

	public static HTTPResponse get(String url, Hashtable<String, String> data) {
		String result = "";
		int status = 0;
		BufferedReader reader = null;

		try {
			url += (url.contains("?")?"":"?") + buildQueryString(data);

			Log.d(TAG, "URL : "+url);

			// création de la connection et envoi de la requête
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

			// HTTP Headers
			HTTPRequest.setHttpHeaders(conn);
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

	public static HTTPResponse post(String url, Hashtable<String, String> data) {
		String result = "";
		int status = 0;
		//OutputStreamWriter writer = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;

		try {
			// encodage des paramètres de la requête
			String dataStr = "";
			String key;
			Set<String> set = data.keySet();
			for (String aSet : set) {
				key = aSet;
				//System.out.println(key + ": " + data.get(key));
				//dataStr += "&" + URLEncoder.encode(key, "ISO-8859-1") + "=" + URLEncoder.encode(data.get(key), "ISO-8859-1");
				dataStr += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(data.get(key), "UTF-8");
			}

			Log.d(TAG, "URL : "+url+" ; POST data : "+dataStr);

			// création de la connection
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			//URLConnection conn = urlObj.openConnection();
			conn.setDoOutput(true);

			// HTTP Headers
			HTTPRequest.setHttpHeaders(conn);
			//conn.setRequestProperty("Accept-Language", "en-GB");

			// envoi de la requête
			//writer = new OutputStreamWriter(conn.getOutputStream());
			writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			writer.write(dataStr);
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

	public static HTTPResponse get(String url) {
		Hashtable<String, String> data = new Hashtable<String, String>();
		return HTTPRequest.get(url, data);
	}

	public static String buildQueryString(Hashtable<String, String> data) {
		// encodage des paramètres de la requête
		String dataStr = "";
		String key;
		Set<String> set = data.keySet();
		for (String aSet : set) {
			key = aSet;
			try {
				dataStr += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(data.get(key), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return dataStr;
	}

	public static void setHttpHeaders(HttpURLConnection conn) {
		//Locale current = getResources().getConfiguration().locale;
		conn.setRequestProperty("Accept-Language", Locale.getDefault().toString().replace("_", "-"));
	}

}
