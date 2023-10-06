package com.osimatic.android_helpers.confirmation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.osimatic.android_helpers.R;
import com.osimatic.android_helpers.listener.SubmitListener;

public class ConfirmationFragment extends DialogFragment {
	SubmitListener submitListener;
	String title;
	String confirmationMessage;
	String buttonText;
	int buttonTextColor = 0;

	String successMessage;
	DialogFragment dialogFragment;
	Boolean dismissDialogFragmentAfterSubmit = false;

	public ConfirmationFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(title);
		View v = inflater.inflate(R.layout.confirmation_fragment, container, false);

		((TextView) v.findViewById(R.id.confirmation_message)).setText(confirmationMessage);

		if (null != buttonText) {
			((TextView) v.findViewById(R.id.submit_button)).setText(buttonText);
		}
		if (0 != buttonTextColor) {
			((TextView) v.findViewById(R.id.cancel_button)).setTextColor(buttonTextColor);
			((TextView) v.findViewById(R.id.submit_button)).setTextColor(buttonTextColor);
		}

		v.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		v.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						submitListener.onSubmit();

						if (null != successMessage && null != getActivity()) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if (null != getActivity()) {
										Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
									}
								}
							});
						}

						if (dismissDialogFragmentAfterSubmit && null != dialogFragment && null != getActivity()) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									if (null != getActivity()) {
										dialogFragment.dismiss();
									}
								}
							});
						}

						dismiss();
					}
				}).start();
			}
		});

		return v;
	}

	public SubmitListener getSubmitListener() {
		return submitListener;
	}

	public void setSubmitListener(SubmitListener submitListener) {
		this.submitListener = submitListener;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getConfirmationMessage() {
		return confirmationMessage;
	}

	public void setConfirmationMessage(String confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public int getButtonTextColor() {
		return buttonTextColor;
	}

	public void setButtonTextColor(int buttonTextColor) {
		this.buttonTextColor = buttonTextColor;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public void dismissDialogFragmentAfterSubmit(DialogFragment dialogFragment) {
		this.dismissDialogFragmentAfterSubmit = true;
		this.dialogFragment = dialogFragment;
	}

}
