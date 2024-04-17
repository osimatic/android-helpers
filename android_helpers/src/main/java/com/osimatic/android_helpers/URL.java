package com.osimatic.android_helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

public class URL {
	public static String buildQueryString(HashMap<String, String> data) {
		// encodage des paramètres de la requête
		String dataStr = "";
		String key;
		Set<String> set = data.keySet();
		for (String aSet : set) {
			key = aSet;
			try {
				String value = data.get(key);
				dataStr += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(null != value ? value : "", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return dataStr;
	}

}
