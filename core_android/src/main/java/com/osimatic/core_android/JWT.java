package com.osimatic.core_android;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for decoding and inspecting JSON Web Tokens (JWT).
 *
 * <p>This class supports decoding the header and payload of a JWT. It does <b>not</b> perform signature verification; use a dedicated JWT library for that purpose.
 *
 * <p>A JWT is a dot-separated string of three Base64URL-encoded parts: {@code header.payload.signature}. This class decodes the first two parts.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519">RFC 7519 — JSON Web Token (JWT)</a>
 * @see <a href="https://jwt.io/introduction">Introduction to JSON Web Tokens — jwt.io</a>
 */
public class JWT {

	private JWT() {}

	// =============================================================================================
	// Parsing
	// =============================================================================================

	/**
	 * Returns {@code true} if the given string has the structure of a JWT (three dot-separated parts).
	 *
	 * <p>This method only validates the format; it does not verify the signature or the content of the token.
	 *
	 * @param token the string to validate; may be {@code null}
	 * @return {@code true} if the string consists of exactly three dot-separated parts
	 */
	public static boolean isValidFormat(String token) {
		if (token == null || token.isEmpty()) return false;
		String[] parts = token.split("\\.");
		return parts.length == 3;
	}

	/**
	 * Decodes the header and payload of the given JWT and returns them as a two-element array of JSON strings.
	 *
	 * <p>Index 0 contains the decoded header; index 1 contains the decoded payload. Both are UTF-8 JSON strings.
	 *
	 * <p>On API 26+, uses {@link java.util.Base64#getUrlDecoder()}. On earlier API levels, falls back to {@link android.util.Base64} with the {@link android.util.Base64#URL_SAFE} flag.
	 *
	 * @param token a valid JWT string with three dot-separated Base64URL-encoded parts; must not be {@code null}
	 * @return a two-element array {@code [header, payload]} of decoded JSON strings, or {@code null} if the token format is invalid
	 * @see #getHeader(String)
	 * @see #getPayload(String)
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-3">RFC 7519 §3 — JWT structure</a>
	 */
	public static String[] parse(String token) {
		if (!isValidFormat(token)) {
			return null;
		}
		String[] chunks = token.split("\\.");
		String[] decoded = new String[2];
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
			decoded[0] = new String(decoder.decode(chunks[0]), StandardCharsets.UTF_8);
			decoded[1] = new String(decoder.decode(chunks[1]), StandardCharsets.UTF_8);
		} else {
			decoded[0] = new String(android.util.Base64.decode(chunks[0], android.util.Base64.URL_SAFE), StandardCharsets.UTF_8);
			decoded[1] = new String(android.util.Base64.decode(chunks[1], android.util.Base64.URL_SAFE), StandardCharsets.UTF_8);
		}
		return decoded;
	}

	// =============================================================================================
	// Payload accessors
	// =============================================================================================

	/**
	 * Returns the decoded JWT header as a {@link JSONObject}.
	 *
	 * @param token a valid JWT string; must not be {@code null}
	 * @return the parsed header {@link JSONObject}, or {@code null} if the token is invalid or the header cannot be parsed
	 * @see #getPayload(String)
	 */
	public static JSONObject getHeader(String token) {
		String[] parts = parse(token);
		if (parts == null) return null;
		try {
			return new JSONObject(parts[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the decoded JWT payload as a {@link JSONObject}.
	 *
	 * @param token a valid JWT string; must not be {@code null}
	 * @return the parsed payload {@link JSONObject}, or {@code null} if the token is invalid or the payload cannot be parsed
	 * @see #getHeader(String)
	 * @see #getClaim(String, String)
	 */
	public static JSONObject getPayload(String token) {
		String[] parts = parse(token);
		if (parts == null) return null;
		try {
			return new JSONObject(parts[1]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the value of the specified claim from the JWT payload as a {@link String}.
	 *
	 * @param token    a valid JWT string; must not be {@code null}
	 * @param claimKey the claim name to retrieve (e.g. {@code "sub"}, {@code "email"}); must not be {@code null}
	 * @return the claim value as a string, or {@code null} if the claim is absent or the token is invalid
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4">RFC 7519 §4 — JWT Claims</a>
	 */
	public static String getClaim(String token, String claimKey) {
		try {
			JSONObject payload = getPayload(token);
			if (payload != null && payload.has(claimKey)) {
				return payload.getString(claimKey);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the subject ({@code sub} claim) from the JWT payload.
	 *
	 * @param token a valid JWT string; must not be {@code null}
	 * @return the subject string, or {@code null} if the claim is absent or the token is invalid
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.2">RFC 7519 §4.1.2 — Subject Claim</a>
	 */
	public static String getSubject(String token) {
		return getClaim(token, "sub");
	}

	/**
	 * Returns the expiration time ({@code exp} claim) of the JWT as a Unix timestamp in seconds.
	 *
	 * @param token a valid JWT string; must not be {@code null}
	 * @return the expiration Unix timestamp in seconds, or {@code -1} if the claim is absent or the token is invalid
	 * @see #isExpired(String)
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4">RFC 7519 §4.1.4 — Expiration Time Claim</a>
	 */
	public static long getExpiration(String token) {
		try {
			JSONObject payload = getPayload(token);
			if (payload != null && payload.has("exp")) {
				return payload.getLong("exp");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	// =============================================================================================
	// Validation
	// =============================================================================================

	/**
	 * Returns {@code true} if the JWT has expired based on the {@code exp} claim.
	 *
	 * <p>A token with no {@code exp} claim is considered non-expired.
	 *
	 * @param token a valid JWT string; must not be {@code null}
	 * @return {@code true} if the current time is past the token's expiration time, {@code false} otherwise
	 * @see #getExpiration(String)
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4">RFC 7519 §4.1.4 — Expiration Time Claim</a>
	 */
	public static boolean isExpired(String token) {
		long exp = getExpiration(token);
		if (exp < 0) return false;
		return System.currentTimeMillis() / 1000 > exp;
	}
}