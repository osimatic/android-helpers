package com.osimatic.android_helpers;

public class JWT {
	public static String[] parse(String token) {
		String[] chunks = token.split("\\.");
		String[] decodedData = new String[2];

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
			decodedData[0] = new String(decoder.decode(chunks[0]));
			decodedData[1] = new String(decoder.decode(chunks[1]));
		}
		else {
			// 05/04/2024 : ne fonctionne pas avec les accents
			decodedData[0] = new String(android.util.Base64.decode(chunks[0], android.util.Base64.DEFAULT));
			decodedData[1] = new String(android.util.Base64.decode(chunks[1], android.util.Base64.DEFAULT));
		}
		return decodedData;
	}
}
