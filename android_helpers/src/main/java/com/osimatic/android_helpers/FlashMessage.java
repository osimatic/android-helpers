package com.osimatic.android_helpers;

import android.app.Activity;
import android.widget.Toast;

public class FlashMessage {
	public static void display(Activity activity, String message) {
		if (null != activity && null != message) {
			activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show());
		}
	}
}
