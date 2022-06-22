package com.osimatic.android_helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import java.io.IOException;

public class Audio {

	public static void playSound(Context context, int file) {
		playSound(context, file, false);
	}

	public static void playSound(Context context, int file, boolean vibrateIfVibrateMode) {
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (am != null) {
			switch (am.getRingerMode()) {
				case AudioManager.RINGER_MODE_SILENT:
					break;
				case AudioManager.RINGER_MODE_VIBRATE:
					if (vibrateIfVibrateMode) {
						((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(400);
					}
					break;
				case AudioManager.RINGER_MODE_NORMAL:
					MediaPlayer.create(context, file).start();
					break;
			}
		}
	}

	public static void playSound(String file) {
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(file);
		} catch (IllegalArgumentException e) {
			Log.e("setDataSource", "IllegalArgumentException " + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e("setDataSource", "IllegalStateException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("setDataSource", "IOException " + e.getMessage());
			e.printStackTrace();
		}
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			Log.e("prepare", "IllegalStateException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("prepare", "IOException " + e.getMessage());
			e.printStackTrace();
		}
		mp.start();

		/*
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				btStart.setEnabled(true);
				btStart.setImageResource(R.drawable.magneto_rec);
				btPlay.setEnabled(true);
			}
		});
		*/
	}
}
