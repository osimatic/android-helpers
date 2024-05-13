package com.osimatic.android_helpers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HTTPRequest {
	private static final String TAG = Config.START_TAG+"HTTPRequest";

	/**
	 * usage : HTTPRequest.allowMySSL(getResources().openRawResource(R.raw.xxx));
	 * @param caInput ca file
	 */
	public static void addTrustedCa(InputStream caInput)
	{
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			Certificate ca;
			try (caInput) {
				ca = cf.generateCertificate(caInput);
			}
			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);
			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);
			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		}
		catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException | CertificateException e) {
			e.printStackTrace();
		}
	}

	public static HTTPResponse get(String url) {
		HashMap<String, String> data = new HashMap<>();
		return HTTPRequest.get(url, data);
	}

	public static HTTPResponse get(String url, HashMap<String, String> data) {
		HashMap<String, String> headers = new HashMap<>();
		return HTTPRequest.get(url, data, headers);
	}

	public static HTTPResponse get(String url, HashMap<String, String> data, HashMap<String, String> headers) {
		StringBuilder result = new StringBuilder();
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
			InputStream is;
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
				result.append(inputLine);
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
			catch(Exception e) {
				Log.e(TAG, null != e.getMessage() ? e.getMessage() : "");
			}
		}

		Log.d(TAG, "HTTP Status : " + status + " ; JSON : " + result);
		return new HTTPResponse(status, result.toString());
	}

	public static HTTPResponse post(String url, HashMap<String, String> data) {
		HashMap<String, String> headers = new HashMap<>();
		return HTTPRequest.post(url, data, headers, false);
	}

	public static HTTPResponse post(String url, HashMap<String, String> data, HashMap<String, String> headers) {
		return HTTPRequest.post(url, data, headers, false);
	}

	public static HTTPResponse post(String url, HashMap<String, String> data, HashMap<String, String> headers, boolean dataAsJson) {
		StringBuilder result = new StringBuilder();
		int status = 0;
		//OutputStreamWriter writer = null;
		BufferedWriter writer = null;
		BufferedReader reader = null;

		try {
			// encodage des paramètres de la requête
			String strData;
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
			InputStream is;
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
				is = conn.getErrorStream();
			}
			else {
				is = conn.getInputStream();
			}
			reader = new BufferedReader(new InputStreamReader(is));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				result.append(ligne);
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
			catch(Exception e) {
				Log.e(TAG, null != e.getMessage() ? e.getMessage() : "");
			}
			try{
				if (reader != null) {
					reader.close();
				}
			}
			catch(Exception e) {
				Log.e(TAG, null != e.getMessage() ? e.getMessage() : "");
			}
		}

		Log.d(TAG, "HTTP Status : " + status + " ; JSON : " + result);
		return new HTTPResponse(status, result.toString());
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
