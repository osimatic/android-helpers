package com.osimatic.android_helpers;

import android.app.Activity;
import android.util.Log;

public class ProgressDialog {
	private static final String TAG = Config.START_TAG+"ProgressDialog";

	public static android.app.ProgressDialog getProgressDialog(final Activity activity) {
		if (null == activity) {
			Log.e(TAG, "activity null");
			return null;
		}
		android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(activity);
		progressDialog.setMessage(activity.getResources().getString(R.string.loading));
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		return progressDialog;
	}

	public static void showProgressDialog(final Activity activity, android.app.ProgressDialog progressDialog) {
		if (null == activity) {
			Log.e(TAG, "activity null");
			return;
		}
		if (null == progressDialog) {
			Log.e(TAG, "progressDialog null");
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (!activity.isFinishing() && !progressDialog.isShowing()) {
					progressDialog.show();
				}
			}
		});
	}

	public static void hideProgressDialog(final Activity activity, android.app.ProgressDialog progressDialog) {
		if (null == activity) {
			Log.e(TAG, "activity null");
			return;
		}
		if (null == progressDialog) {
			Log.e(TAG, "progressDialog null");
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (!activity.isFinishing() && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		});
	}
}
