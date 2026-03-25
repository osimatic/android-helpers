package com.osimatic.core_android;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Signs sensitive HTTP requests using HMAC-SHA256.
 *
 * <p>Initialize the secret at app startup (e.g. in {@code Application.onCreate()}):
 * <pre>
 *   RequestSigner.setSecret(BuildConfig.HMAC_SECRET);
 * </pre>
 *
 * <p>The server must verify:
 * <ul>
 *   <li>that the signature matches (same secret + same canonical string)</li>
 *   <li>that X-Timestamp is recent (e.g. ±5 min)</li>
 *   <li>that X-Nonce has not already been used (anti-replay)</li>
 * </ul>
 */
public class RequestSigner {

	/** Volatile to ensure visibility across threads. */
	private static volatile String secret = "";

	/** Initializes the HMAC secret. Must be called once at startup (e.g. in {@code Application.onCreate()}). */
	public static void setSecret(String s) {
		secret = s != null ? s : "";
	}

	/**
	 * Signs a set of critical fields.
	 *
	 * <p>Canonical string that is signed:
	 * <pre>
	 *   timestamp\n
	 *   nonce\n
	 *   field1=value1&field2=value2   (keys sorted alphabetically, no trailing '&')
	 * </pre>
	 *
	 * @param criticalFields fields to include in the signature (key → value)
	 * @return {@link SignatureResult} containing the 3 headers to add to the request
	 * @throws GeneralSecurityException if HMAC-SHA256 is unavailable or the key is invalid
	 */
	public static SignatureResult sign(Map<String, String> criticalFields) throws GeneralSecurityException {
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String nonce = UUID.randomUUID().toString().replace("-", "");

		List<String> keys = new ArrayList<>(criticalFields.keySet());
		Collections.sort(keys);

		StringBuilder canonical = new StringBuilder();
		canonical.append(timestamp).append("\n");
		canonical.append(nonce).append("\n");
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = criticalFields.get(key);
			canonical.append(key).append("=").append(value != null ? value : "");
			if (i < keys.size() - 1) {
				canonical.append("&");
			}
		}

		String signature = hmacSha256(secret, canonical.toString());
		return new SignatureResult(timestamp, nonce, signature);
	}

	private static String hmacSha256(String secret, String data) throws GeneralSecurityException {
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return Base64.encodeToString(raw, Base64.NO_WRAP);
	}

	public static class SignatureResult {
		public final String timestamp;
		public final String nonce;
		public final String signature;

		/** Package-private: only {@link RequestSigner} should create instances. */
		SignatureResult(String timestamp, String nonce, String signature) {
			this.timestamp = timestamp;
			this.nonce = nonce;
			this.signature = signature;
		}

		/** Returns the 3 headers to include in the HTTP request. */
		public Map<String, String> asHeaders() {
			Map<String, String> headers = new HashMap<>();
			headers.put("X-Timestamp", timestamp);
			headers.put("X-Nonce", nonce);
			headers.put("X-Signature", signature);
			return headers;
		}
	}
}