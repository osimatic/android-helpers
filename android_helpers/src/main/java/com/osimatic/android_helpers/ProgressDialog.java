package com.osimatic.android_helpers;

import android.app.Activity;

public class ProgressDialog {
	public static android.app.ProgressDialog getProgressDialog(final Activity activity) {
		if (null == activity) {
			return null;
		}
		android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(activity);
		progressDialog.setMessage(activity.getResources().getString(R.string.loading));
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		return progressDialog;
	}

	public static void showProgressDialog(final Activity activity, android.app.ProgressDialog progressDialog) {
		if (null == activity || null == progressDialog) {
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
		if (null == activity || null == progressDialog) {
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
