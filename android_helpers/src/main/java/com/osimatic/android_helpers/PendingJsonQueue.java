package com.osimatic.android_helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Queue générique de JSONObject persistée dans SharedPreferences.
 * Conçue pour mettre en attente des opérations hors-ligne et les rejouer au retour du réseau.
 *
 * <p>Chaque instance est liée à un nom de queue distinct, ce qui permet
 * de gérer plusieurs queues indépendantes dans la même application.</p>
 *
 * <pre>
 *   PendingJsonQueue queue = new PendingJsonQueue(context, "pending_clockings");
 *   queue.enqueue(item);
 *   List&lt;JSONObject&gt; items = queue.getAll();
 *   queue.removeFirst();
 * </pre>
 */
public class PendingJsonQueue {

	private static final String TAG = Config.START_TAG + "PendingJsonQueue";
	private static final String KEY_QUEUE = "queue";

	private final Context context;
	private final String name;

	/**
	 * @param context contexte Android
	 * @param name    identifiant unique de cette queue (utilisé comme nom de SharedPreferences)
	 */
	public PendingJsonQueue(Context context, String name) {
		this.context = context.getApplicationContext();
		this.name = name;
	}

	/** Ajoute un item à la queue. */
	public void enqueue(JSONObject item) {
		JSONArray queue = load();
		queue.put(item);
		save(queue);
		Log.d(TAG, "[" + name + "] item ajouté (total : " + queue.length() + ")");
	}

	/** Retourne le nombre d'items en attente. */
	public int count() {
		return load().length();
	}

	/** Retourne true si la queue est vide. */
	public boolean isEmpty() {
		return count() == 0;
	}

	/** Retourne tous les items en attente. */
	public List<JSONObject> getAll() {
		JSONArray queue = load();
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < queue.length(); i++) {
			try {
				list.add(queue.getJSONObject(i));
			} catch (JSONException e) {
				Log.e(TAG, "[" + name + "] erreur lecture item " + i + " : " + e.getMessage());
			}
		}
		return list;
	}

	/** Supprime le premier item (après traitement réussi). */
	public void removeFirst() {
		JSONArray queue = load();
		if (queue.length() == 0) return;
		JSONArray newQueue = new JSONArray();
		for (int i = 1; i < queue.length(); i++) {
			try {
				newQueue.put(queue.get(i));
			} catch (JSONException e) {
				Log.e(TAG, "[" + name + "] erreur purge queue : " + e.getMessage());
			}
		}
		save(newQueue);
	}

	/** Vide entièrement la queue. */
	public void clearAll() {
		save(new JSONArray());
	}

	// --- Helpers ---

	private JSONArray load() {
		SharedPreferences prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String raw = prefs.getString(KEY_QUEUE, "[]");
		try {
			return new JSONArray(raw);
		} catch (JSONException e) {
			Log.e(TAG, "[" + name + "] queue corrompue, réinitialisation : " + e.getMessage());
			return new JSONArray();
		}
	}

	private void save(JSONArray queue) {
		SharedPreferences prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		prefs.edit().putString(KEY_QUEUE, queue.toString()).apply();
	}
}