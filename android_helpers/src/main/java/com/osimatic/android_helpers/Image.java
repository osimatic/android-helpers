package com.osimatic.android_helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

public class Image {
	private static String TAG = Config.START_TAG+"Image";

	public static Bitmap getImageBitmap(Intent intent, Context context) {
		if (intent == null) {
			return null;
		}

		if (intent.getData() != null) {
			try {
				Uri uri = intent.getData();
				return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		if (intent.getExtras() != null) {
			try {
				return (Bitmap) intent.getExtras().get("data");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return null;
	}

	public static Bitmap getImageBitmap(String url) {
		return getImageBitmap(url, new Hashtable<String, String>());
	}

	public static Bitmap getImageBitmap(String url, Hashtable<String, String> data) {
		Log.d(TAG, "getImageBitmap");
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			url += (url.contains("?")?"":"?") + HTTPRequest.buildQueryString(data);
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
		}
		catch (IOException e) {
			Log.e("getImageBitmap", "Error getting bitmap", e);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			}
			try{
				if (bis != null) {
					is.close();
				}
			}
			catch(Exception e) {
				Log.d(TAG, e.getMessage());
			}
		}
		return bm;
	}
}
