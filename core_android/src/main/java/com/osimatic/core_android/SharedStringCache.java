package com.osimatic.core_android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Cache clé/valeur de chaînes persisté dans SharedPreferences.
 * Conçu pour mettre en cache des réponses JSON d'API afin de les
 * réutiliser en mode hors-ligne.
 *
 * <pre>
 *   SharedStringCache cache = new SharedStringCache(context, "clocking_params_cache");
 *   cache.save("emp123_application", jsonResponse);
 *   String cached = cache.load("emp123_application");
 * </pre>
 */
public class SharedStringCache {

	private final Context context;
	private final String name;

	/**
	 * @param context contexte Android
	 * @param name    identifiant unique de ce cache (utilisé comme nom de SharedPreferences)
	 */
	public SharedStringCache(Context context, String name) {
		this.context = context.getApplicationContext();
		this.name = name;
	}

	/** Sauvegarde une valeur pour la clé donnée. */
	public void save(String key, String value) {
		prefs().edit().putString(key, value).apply();
	}

	/**
	 * Charge la valeur associée à la clé.
	 * @return la valeur, ou {@code null} si absente.
	 */
	public String load(String key) {
		return prefs().getString(key, null);
	}

	/** Retourne true si une valeur existe pour cette clé. */
	public boolean has(String key) {
		return load(key) != null;
	}

	/** Supprime l'entrée pour cette clé. */
	public void remove(String key) {
		prefs().edit().remove(key).apply();
	}

	/** Vide entièrement le cache. */
	public void clearAll() {
		prefs().edit().clear().apply();
	}

	private SharedPreferences prefs() {
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
}