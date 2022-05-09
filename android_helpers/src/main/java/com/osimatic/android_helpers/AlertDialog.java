package com.osimatic.android_helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;

public class AlertDialog {
	private static final String TAG = Config.START_TAG+"AlertDialog";

	public static void showAlert(final Activity activity, final String message) {
		showAlert(activity, message, null);
	}

	public static void showAlert(final Activity activity, final String message, DialogInterface.OnClickListener onClickListener) {
		if (activity == null) {
			Log.e(TAG, "activity null");
			return;
		}

		activity.runOnUiThread(new Runnable() {
			public void run() {
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
				//android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity.getApplicationContext());
				builder.setTitle(activity.getResources().getString(R.string.error));
				builder.setMessage(message);
				builder.setCancelable(false);
				builder.setPositiveButton(activity.getResources().getString(R.string.ok), null != onClickListener ? onClickListener : new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});

				android.app.AlertDialog alert = builder.create();
				try {
					alert.show();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
