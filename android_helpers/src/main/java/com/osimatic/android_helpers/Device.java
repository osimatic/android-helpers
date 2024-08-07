package com.osimatic.android_helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Device {
	public static UUID getDeviceUniqueID(Context context) {
		/*if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			return null;
		}

		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		return new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());*/

		//String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
		//return new UUID(m_szDevIDShort.hashCode(), Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).hashCode());

		return UUID.nameUUIDFromBytes(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(StandardCharsets.UTF_8));
	}

	public static String getDeviceName() {
		return Build.MANUFACTURER + " " + Build.MODEL;
	}

	public static String getOsName() {
		return "ANDROID";
	}

	public static String getOsVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String getAppVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			return manager.getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	public static String getUniqueDeviceId() {
		private String android_id = Secure.getString(getContext().getContentResolver(),
	}

	public static String getIpAddress(Context context) {
		WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
	}
	*/

	public static String getIpAddress() {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						//boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':')<0;

						if (isIPv4) {
							return sAddr;
						}

						int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
						return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();

						/*
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
								return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
							}
						}
						*/
					}
				}
			}
		}
		catch (Exception ignored) { } // for now eat exceptions
		return "";
	}

	public static boolean isDeviceOnline(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**
	 * Returns whether the SDK is the Jellybean release or later.
	 */
	public static boolean isJellybeanOrLater() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * Returns whether the SDK is the Marshmallow release or later.
	 */
	public static boolean isMarshmallowOrLater() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}
}
