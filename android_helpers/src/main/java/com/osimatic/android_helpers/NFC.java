package com.osimatic.android_helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NFC {
	private static final String TAG = Config.START_TAG+"NFC";
	
	/**
	 * Creates a custom MIME type encapsulated in an NDEF record for a given payload
	 * @param mimeType String
	 * @param payload byte[]
	 * @return NdefRecord
	 */
	public static NdefRecord createRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(StandardCharsets.US_ASCII);
		NdefRecord mimeRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
		return mimeRecord;
	}

	public static NdefRecord createRecordUri(String uri) {
		byte[] uriBytes = new byte[0];
		uriBytes = uri.getBytes(StandardCharsets.UTF_8);
		byte[] payload = new byte[1 + uriBytes.length];

		// set prefix byte (see URI RTD for possibile values, we just use 0 indicating no prefix for now)
		payload[0] = 0;

		// copy uriBytes into payload
		System.arraycopy(uriBytes, 0, payload, 1, uriBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, null, payload);
	}

	/**
	 * Creates an Ndef message
	 * @param mimeType String
	 * @param payload byte[]
	 * @return NdefMessage
	 */
	public static NdefMessage createMessage(String mimeType, byte[] payload) {
		// Min API Level of 14 requires an array as the argument
		return new NdefMessage(new NdefRecord[] { createRecord(mimeType, payload) });
	}

	/**
	 * Write an NDEF message to a Tag
	 * @param message NdefMessage
	 * @param tag Tag
	 * @return true if successful, false if not written to
	 */
	public static boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					Log.e(TAG, "Not writing to tag- tag is not writable");
					return false;
				}
				if (ndef.getMaxSize() < size) {
					Log.e(TAG, "Not writing to tag- message exceeds the max tag size of " + ndef.getMaxSize());
					return false;
				}
				ndef.writeNdefMessage(message);
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						return true;
					} catch (IOException e) {
						Log.e(TAG, "Not writing to tag", e);
						return false;
					}
				} else {
					Log.e(TAG, "Not writing to tag- undefined format");
					return false;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Not writing to tag", e);
			return false;
		}
	}

	/**
	 * Parse an intent for non-empty strings within an NDEF message
	 * @param intent intent
	 * @return an empty list if the payload is empty
	 */
	public static List<String> getStringsFromNfcIntent(Intent intent) {
		List<String> payloadStrings = new ArrayList<String>();

		for (NdefMessage message : getMessagesFromIntent(intent)) {
			for (NdefRecord record : message.getRecords()) {
				byte[] payload = record.getPayload();
				String payloadString = new String(payload);

				if (!TextUtils.isEmpty(payloadString))
					payloadStrings.add(payloadString);
			}
		}

		return payloadStrings;
	}

	/**
	 * Parses an intent for NDEF messages, returns all that are found
	 * @param intent intent
	 * @return an empty list if there are no NDEF messages found
	 */
	public static List<NdefMessage> getMessagesFromIntent(Intent intent) {
		List<NdefMessage> intentMessages = new ArrayList<NdefMessage>();
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Log.i(TAG, "Reading from NFC " + action);
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				for (Parcelable msg : rawMsgs) {
					if (msg instanceof NdefMessage) {
						intentMessages.add((NdefMessage) msg);
					}
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				final NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				final NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				intentMessages = new ArrayList<NdefMessage>() {
					{
						add(msg);
					}
				};
			}
		}
		return intentMessages;
	}

	/**
	 * A pending intent is required to enable foreground NDEF dispatch
	 * @param context context
	 * @return PendingIntent
	 */
	public static PendingIntent getPendingIntent(Activity context) {
		return PendingIntent.getActivity(context, 0,
				new Intent(context, context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}
}
