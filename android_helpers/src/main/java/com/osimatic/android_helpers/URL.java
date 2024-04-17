package com.osimatic.android_helpers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
			String value = data.get(key);
			dataStr += "&" + URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(null != value ? value : "", StandardCharsets.UTF_8);
		}
		return dataStr;
	}

}
