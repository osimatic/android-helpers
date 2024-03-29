package com.osimatic.android_helpers.fingerprint;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.osimatic.android_helpers.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintAuthenticationDialogFragment extends DialogFragment implements TextView.OnEditorActionListener, FingerprintUI.Callback {
	public interface SubmitListener {
		void onSubmit(String fingerprintStatus);
	}

	private Button mCancelButton;
	private Button mSecondDialogButton;
	private View mFingerprintContent;
	private View mBackupContent;
	private EditText mPassword;
	private CheckBox mUseFingerprintFutureCheckBox;
	private TextView mPasswordDescriptionTextView;
	private TextView mNewFingerprintEnrolledTextView;

	private Stage mStage = Stage.FINGERPRINT;

	private Activity mActivity;
	private SubmitListener submitListener;
	private int hintColor;
	private FingerprintManager.CryptoObject mCryptoObject;
	private FingerprintUI mFingerprintUiHelper;

	private InputMethodManager mInputMethodManager;
	private SharedPreferences mSharedPreferences;

	public FingerprintAuthenticationDialogFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do not create a new Fragment when the Activity is re-created such as orientation changes.
		setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.fingerprint_dialog_title));
		View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
		mCancelButton = v.findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				if (null != submitListener) {
					submitListener.onSubmit(FingerprintStatus.CANCELED);
				}
			}
		});

		mSecondDialogButton = v.findViewById(R.id.second_dialog_button);
		mSecondDialogButton.setVisibility(View.GONE);
		mSecondDialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mStage == Stage.FINGERPRINT) {
					goToBackup();
				} else {
					verifyPassword();
				}
			}
		});

		mFingerprintContent = v.findViewById(R.id.fingerprint_container);
		mBackupContent = v.findViewById(R.id.backup_container);
		mPassword = v.findViewById(R.id.password);
		mPassword.setOnEditorActionListener(this);
		mPasswordDescriptionTextView = v.findViewById(R.id.password_description);
		mUseFingerprintFutureCheckBox = v.findViewById(R.id.use_fingerprint_in_future_check);
		mNewFingerprintEnrolledTextView = v.findViewById(R.id.new_fingerprint_enrolled_description);
		mFingerprintUiHelper = new FingerprintUI(
				mActivity.getSystemService(FingerprintManager.class),
				(ImageView) v.findViewById(R.id.fingerprint_icon),
				(TextView) v.findViewById(R.id.fingerprint_status), this);
		mFingerprintUiHelper.setHintColor(hintColor);
		updateStage();

		// If fingerprint authentication is not available, switch immediately to the backup (password) screen.
		if (!mFingerprintUiHelper.isFingerprintAuthAvailable()) {
			//goToBackup();
			if (null != submitListener) {
				submitListener.onSubmit(FingerprintStatus.HARDWARE_MISSING);
			}
		}
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mStage == Stage.FINGERPRINT) {
			mFingerprintUiHelper.startListening(mCryptoObject);
		}
	}

	public void setStage(Stage stage) {
		mStage = stage;
	}

	@Override
	public void onPause() {
		super.onPause();
		mFingerprintUiHelper.stopListening();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mInputMethodManager = context.getSystemService(InputMethodManager.class);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Sets the crypto object to be passed in when authenticating with fingerprint.
	 */
	public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
		mCryptoObject = cryptoObject;
	}

	/**
	 * Switches to backup (password) screen. This either can happen when fingerprint is not
	 * available or the user chooses to use the password authentication method by pressing the
	 * button. This can also happen when the user had too many fingerprint attempts.
	 */
	private void goToBackup() {
		mStage = Stage.PASSWORD;
		updateStage();
		mPassword.requestFocus();

		// Show the keyboard.
		mPassword.postDelayed(mShowKeyboardRunnable, 500);

		// Fingerprint is not used anymore. Stop listening for it.
		mFingerprintUiHelper.stopListening();
	}

	/**
	 * Checks whether the current entered password is correct, and dismisses the the dialog and
	 * let's the activity know about the result.
	 */
	private void verifyPassword() {
		if (!checkPassword(mPassword.getText().toString())) {
			dismiss();
			if (null != submitListener) {
				submitListener.onSubmit(FingerprintStatus.PASSWORD_NOT_VALID);
			}
		}
		if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
					mUseFingerprintFutureCheckBox.isChecked());
			editor.apply();

			if (mUseFingerprintFutureCheckBox.isChecked()) {
				// Re-create the key so that fingerprints including new ones are validated.
				//mActivity.createKey(MainActivity.DEFAULT_KEY_NAME, true);
				mStage = Stage.FINGERPRINT;
			}
		}
		mPassword.setText("");

		// fingerprint ok by password
		dismiss();
		if (null != submitListener) {
			submitListener.onSubmit(FingerprintStatus.PASSWORD_OK);
		}
	}

	/**
	 * @return true if {@code password} is correct, false otherwise
	 */
	private boolean checkPassword(String password) {
		// Assume the password is always correct.
		// In the real world situation, the password needs to be verified in the server side.
		return password.length() > 0;
	}

	private final Runnable mShowKeyboardRunnable = new Runnable() {
		@Override
		public void run() {
			mInputMethodManager.showSoftInput(mPassword, 0);
		}
	};

	private void updateStage() {
		switch (mStage) {
			case FINGERPRINT:
				mCancelButton.setText(R.string.cancel);
				mSecondDialogButton.setText(R.string.fingerprint_use_password);
				mFingerprintContent.setVisibility(View.VISIBLE);
				mBackupContent.setVisibility(View.GONE);
				break;
			case NEW_FINGERPRINT_ENROLLED:
				// Intentional fall through
			case PASSWORD:
				mCancelButton.setText(R.string.cancel);
				mSecondDialogButton.setText(R.string.ok);
				mFingerprintContent.setVisibility(View.GONE);
				mBackupContent.setVisibility(View.VISIBLE);
				if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
					mPasswordDescriptionTextView.setVisibility(View.GONE);
					mNewFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
					mUseFingerprintFutureCheckBox.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_GO) {
			verifyPassword();
			return true;
		}
		return false;
	}

	@Override
	public void onAuthenticated() {
		// Callback from FingerprintUiHelper. Let the activity know that authentication was successful.

		// fingerprint ok
		//mActivity.pointageFin(true /* withFingerprint */, mCryptoObject);
		dismiss();
		if (null != submitListener) {
			submitListener.onSubmit(FingerprintStatus.OK);
		}
	}

	@Override
	public void onError() {
		dismiss();
		if (null != submitListener) {
			submitListener.onSubmit(FingerprintStatus.NOT_VALID);
		}
		//goToBackup();
	}

	/**
	 * Enumeration to indicate which authentication method the user is trying to authenticate with.
	 */
	public enum Stage {
		FINGERPRINT,
		NEW_FINGERPRINT_ENROLLED,
		PASSWORD
	}

	public void setActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public void setSubmitListener(SubmitListener submitListener) {
		this.submitListener = submitListener;
	}

	public void setHintColor(int hintColor) {
		this.hintColor = hintColor;
	}
}
