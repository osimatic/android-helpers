package com.osimatic.android_helpers;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class Audio {
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
