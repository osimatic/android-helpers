package com.osimatic.android_helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;

import java.io.IOException;

/**
 * Utility class providing helper methods for audio playback, ringer mode checks, and device vibration.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see android.media.AudioManager
 * @see android.media.MediaPlayer
 * @see android.os.Vibrator
 */
public class Audio {

	private static final String TAG = Config.START_TAG + "Audio";

	private Audio() {}

	// =============================================================================================
	// Ringer mode
	// =============================================================================================

	/**
	 * Returns {@code true} if the device ringer mode is currently set to silent.
	 *
	 * @param context the application context; must not be {@code null}
	 * @return {@code true} if ringer mode is {@link AudioManager#RINGER_MODE_SILENT}
	 * @see AudioManager#getRingerMode()
	 */
	public static boolean isSilent(Context context) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return am != null && am.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
	}

	/**
	 * Returns {@code true} if the device ringer mode is currently set to vibrate.
	 *
	 * @param context the application context; must not be {@code null}
	 * @return {@code true} if ringer mode is {@link AudioManager#RINGER_MODE_VIBRATE}
	 * @see AudioManager#getRingerMode()
	 */
	public static boolean isVibrateMode(Context context) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return am != null && am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
	}

	// =============================================================================================
	// Volume
	// =============================================================================================

	/**
	 * Returns the current volume for the given audio stream.
	 *
	 * @param context    the application context; must not be {@code null}
	 * @param streamType the audio stream type (e.g. {@link AudioManager#STREAM_MUSIC})
	 * @return the current volume, or {@code -1} if the {@link AudioManager} is unavailable
	 * @see AudioManager#getStreamVolume(int)
	 * @see <a href="https://developer.android.com/reference/android/media/AudioManager">AudioManager — Android Docs</a>
	 */
	public static int getVolume(Context context, int streamType) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return am != null ? am.getStreamVolume(streamType) : -1;
	}

	/**
	 * Returns the maximum volume for the given audio stream.
	 *
	 * @param context    the application context; must not be {@code null}
	 * @param streamType the audio stream type (e.g. {@link AudioManager#STREAM_MUSIC})
	 * @return the maximum volume, or {@code -1} if the {@link AudioManager} is unavailable
	 * @see AudioManager#getStreamMaxVolume(int)
	 */
	public static int getMaxVolume(Context context, int streamType) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return am != null ? am.getStreamMaxVolume(streamType) : -1;
	}

	// =============================================================================================
	// Playback
	// =============================================================================================

	/**
	 * Plays the given sound resource, respecting the current ringer mode.
	 *
	 * <p>The sound is played only if the ringer mode is {@link AudioManager#RINGER_MODE_NORMAL}. The {@link MediaPlayer} is automatically released once playback completes.
	 *
	 * @param context the application context; must not be {@code null}
	 * @param resId   the raw resource ID of the sound to play
	 * @see #playSound(Context, int, boolean)
	 */
	public static void playSound(Context context, int resId) {
		playSound(context, resId, false);
	}

	/**
	 * Plays the given sound resource or vibrates, respecting the current ringer mode.
	 *
	 * <p>Behavior by ringer mode:
	 * <ul>
	 *   <li>{@link AudioManager#RINGER_MODE_SILENT}: does nothing</li>
	 *   <li>{@link AudioManager#RINGER_MODE_VIBRATE}: vibrates for 400 ms if {@code vibrateIfVibrateMode} is {@code true}</li>
	 *   <li>{@link AudioManager#RINGER_MODE_NORMAL}: plays the sound; the {@link MediaPlayer} is released after completion</li>
	 * </ul>
	 *
	 * @param context              the application context; must not be {@code null}
	 * @param resId                the raw resource ID of the sound to play
	 * @param vibrateIfVibrateMode if {@code true}, vibrates when the ringer is in vibrate mode
	 * @see #playSound(Context, int)
	 * @see #vibrate(Context, long)
	 */
	public static void playSound(Context context, int resId, boolean vibrateIfVibrateMode) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (am == null) return;
		switch (am.getRingerMode()) {
			case AudioManager.RINGER_MODE_SILENT:
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				if (vibrateIfVibrateMode) {
					vibrate(context, 400);
				}
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				MediaPlayer mp = MediaPlayer.create(context, resId);
				if (mp == null) return;
				mp.setOnCompletionListener(MediaPlayer::release);
				mp.start();
				break;
		}
	}

	/**
	 * Plays the audio file at the given path.
	 *
	 * <p>The {@link MediaPlayer} is automatically released once playback completes. If an error occurs during preparation, the player is released immediately and playback does not start.
	 *
	 * @param filePath the absolute path to the audio file to play; must not be {@code null}
	 */
	public static void playSound(String filePath) {
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(filePath);
			mp.prepare();
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			Log.e(TAG, "Failed to prepare MediaPlayer: " + e.getMessage());
			mp.release();
			return;
		}
		mp.setOnCompletionListener(MediaPlayer::release);
		mp.start();
	}

	// =============================================================================================
	// Vibration
	// =============================================================================================

	/**
	 * Vibrates the device for the given duration in milliseconds.
	 *
	 * <p>Uses {@link VibrationEffect#createOneShot(long, int)} on API 26+, and falls back to {@link Vibrator#vibrate(long)} on older devices. On API 31+, uses {@link VibratorManager} to obtain the default vibrator.
	 *
	 * <p>Requires the {@code android.permission.VIBRATE} permission.
	 *
	 * @param context    the application context; must not be {@code null}
	 * @param durationMs the vibration duration in milliseconds; must be &gt; 0
	 * @see VibrationEffect#createOneShot(long, int)
	 * @see <a href="https://developer.android.com/reference/android/os/VibrationEffect">VibrationEffect — Android Docs</a>
	 */
	@SuppressWarnings("deprecation")
	public static void vibrate(Context context, long durationMs) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Vibrator v;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				VibratorManager vm = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
				v = vm != null ? vm.getDefaultVibrator() : null;
			} else {
				v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			}
			if (v != null) {
				v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
			}
		} else {
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			if (v != null) {
				v.vibrate(durationMs);
			}
		}
	}
}