package com.osimatic.android_helpers.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.osimatic.android_helpers.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUI extends FingerprintManager.AuthenticationCallback {

	private static final long ERROR_TIMEOUT_MILLIS = 1600;
	private static final long SUCCESS_DELAY_MILLIS = 1300;

	private final FingerprintManager mFingerprintManager;
	private final ImageView mIcon;
	private final TextView mErrorTextView;
	private final Callback mCallback;
	private CancellationSignal mCancellationSignal;

	private int hintColor;
	private boolean mSelfCancelled;

	/**
	 * Constructor for {@link FingerprintUI}.
	 */
	public FingerprintUI(FingerprintManager fingerprintManager, ImageView icon, TextView errorTextView, Callback callback) {
		mFingerprintManager = fingerprintManager;
		mIcon = icon;
		mErrorTextView = errorTextView;
		mCallback = callback;
		hintColor = mErrorTextView.getResources().getColor(R.color.black);
	}

	public boolean isFingerprintAuthAvailable() {
		// The line below prevents the false positive inspection from Android Studio
		// noinspection ResourceType
		return mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints();
	}

	public void startListening(FingerprintManager.CryptoObject cryptoObject) {
		if (!isFingerprintAuthAvailable()) {
			return;
		}
		mCancellationSignal = new CancellationSignal();
		mSelfCancelled = false;
		// The line below prevents the false positive inspection from Android Studio
		// noinspection ResourceType
		mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
		mIcon.setImageResource(R.drawable.ic_fp);
	}

	public void stopListening() {
		if (mCancellationSignal != null) {
			mSelfCancelled = true;
			mCancellationSignal.cancel();
			mCancellationSignal = null;
		}
	}

	@Override
	public void onAuthenticationError(int errMsgId, CharSequence errString) {
		if (!mSelfCancelled) {
			showError(errString);
			mIcon.postDelayed(new Runnable() {
				@Override
				public void run() {
					mCallback.onError();
				}
			}, ERROR_TIMEOUT_MILLIS);
		}
	}

	@Override
	public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
		showError(helpString);
	}

	@Override
	public void onAuthenticationFailed() {
		showError(mIcon.getResources().getString(
				R.string.fingerprint_not_recognized));
	}

	@Override
	public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
		mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
		mIcon.setImageResource(R.drawable.ic_success);
		mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.success_color, null));
		mErrorTextView.setText(mErrorTextView.getResources().getString(R.string.fingerprint_success));
		mIcon.postDelayed(new Runnable() {
			@Override
			public void run() {
				mCallback.onAuthenticated();
			}
		}, SUCCESS_DELAY_MILLIS);
	}

	private void showError(CharSequence error) {
		mIcon.setImageResource(R.drawable.ic_error);
		mErrorTextView.setText(error);
		mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.warning_color, null));
		mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
		mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
	}

	private Runnable mResetErrorTextRunnable = new Runnable() {
		@Override
		public void run() {
			mErrorTextView.setTextColor(hintColor);
			mErrorTextView.setText(mErrorTextView.getResources().getString(R.string.fingerprint_hint));
			mIcon.setImageResource(R.drawable.ic_fp);
		}
	};

	public interface Callback {
		void onAuthenticated();

		void onError();
	}

	public void setHintColor(int hintColor) {
		this.hintColor = hintColor;
	}
}
