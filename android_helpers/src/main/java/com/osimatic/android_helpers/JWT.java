package com.osimatic.android_helpers;

public class JWT {
	public static String[] parse(String token) {
		String[] chunks = token.split("\\.");
		String[] decodedData = new String[2];

		decodedData[0] = new String(android.util.Base64.decode(chunks[0], android.util.Base64.DEFAULT));
		decodedData[1] = new String(android.util.Base64.decode(chunks[1], android.util.Base64.DEFAULT));

		return decodedData;
	}
}
