package com.osimatic.android_helpers;

import android.app.DownloadManager;
import android.content.res.Resources;
import android.content.Context;

import java.io.FileWriter;
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

	/*public void downloadFile() {
		String DownloadUrl = audio1;
		DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
		request1.setDescription("Sample Music File");   //appears the same in Notification bar while downloading
		request1.setTitle("File1.mp3");
		request1.setVisibleInDownloadsUi(false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request1.allowScanningByMediaScanner();
			request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
		}
		request1.setDestinationInExternalFilesDir(getApplicationContext(), "/File", "Question1.mp3");

		DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		Objects.requireNonNull(manager1).enqueue(request1);
		if (DownloadManager.STATUS_SUCCESSFUL == 8) {
			DownloadSuccess();
		}
	}*/

	public static void writeFileOnInternalStorage(Context context, String directory, String fileName, String data) {
		try {
			java.io.File dir = new java.io.File(context.getFilesDir(), directory);
			if (!dir.exists()) {
				dir.mkdir();
			}

			java.io.File gpxfile = new java.io.File(dir, fileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(data);
			writer.flush();
			writer.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void writeFileOnInternalStorage(Context context, String directory, String fileName, byte[] data) {
		String strData = "";
		for (byte b: data) {
			strData += Byte.toString(b);
		}
		File.writeFileOnInternalStorage(context, directory, fileName, strData);
	}

}
