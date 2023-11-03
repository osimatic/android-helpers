package com.osimatic.android_helpers;

import android.app.DownloadManager;
import android.content.res.Resources;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

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

	public static void downloadFile(Context context, String url, HashMap<String, String> httpHeaders, String fileName, String description, String mimeType) {
		DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(url));
		if (null != description) {
			request1.setDescription(description); //appears the same in Notification bar while downloading
		}
		request1.setTitle(fileName);
		request1.setVisibleInDownloadsUi(false);
		request1.allowScanningByMediaScanner();
		if (null != mimeType) {
			request1.setMimeType(mimeType);
		}

		if (null != httpHeaders) {
			for (String headerName: httpHeaders.keySet()) {
				request1.addRequestHeader(headerName, httpHeaders.get(headerName));
			}
		}

		request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

		DownloadManager manager1 = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		Objects.requireNonNull(manager1).enqueue(request1);
	}

	public static void writeFileOnInternalStorage(Context context, String directory, String fileName, String data) {
		try {
			java.io.File dir = new java.io.File(context.getFilesDir(), directory);
			if (!dir.exists()) {
				dir.mkdir();
			}

			String filePath = context.getFilesDir()+"/"+directory+fileName;
			File.writeFile(filePath, data);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void writeFile(String filePath, String data) {
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.append(data);
			writer.flush();
			writer.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void writeFile(String filePath, byte[] data) {
		try {
			try (FileOutputStream output = new FileOutputStream(filePath)) {
				output.write(data);
				output.flush();
				output.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
