package com.osimatic.android_helpers;

import android.content.res.Resources;

import java.text.DecimalFormat;

public class File {

	public static String formatFileSize(long size, Resources r) {
		if (size <= 0) {
			return "0";
		}

		final String[] units = new String[] { r.getString(R.string.file_size_bytes), r.getString(R.string.file_size_kilobytes), r.getString(R.string.file_size_megabytes), r.getString(R.string.file_size_gigabytes), r.getString(R.string.file_size_terrabytes) };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

}
