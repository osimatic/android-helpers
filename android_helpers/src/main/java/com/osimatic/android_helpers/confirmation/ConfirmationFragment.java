package com.osimatic.android_helpers.confirmation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.osimatic.android_helpers.R;

public class ConfirmationFragment extends DialogFragment {
	public interface SubmitListener {
		default void onSubmit() {

		}
		default void onSubmit(String message) {

		}
	}

	boolean withMessageField = false;
	SubmitListener submitListener;
	String title;
	String confirmationMessage;
	String enteredMessageLabel;
	EditText enteredMessageEditText;
	String buttonText;
	int buttonTextColor = 0;

	String successMessage;
	DialogFragment dialogFragment;
	Boolean dismissDialogFragmentAfterSubmit = false;

	public ConfirmationFragment() {
		super();
	}

	public ConfirmationFragment(boolean withMessageField) {
		super();

		this.withMessageField = withMessageField;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(title);
		View v = inflater.inflate(R.layout.confirmation_fragment, container, false);

		View enteredMessageLayout = v.findViewById(R.id.entered_message_layout);
		enteredMessageLayout.setVisibility(withMessageField ? View.VISIBLE : View.GONE);
		enteredMessageEditText = v.findViewById(R.id.entered_message_edit_text);
		if (withMessageField && null != enteredMessageLabel) {
			((TextView) v.findViewById(R.id.entered_message_label)).setText(enteredMessageLabel);
		}

		((TextView) v.findViewById(R.id.confirmation_message)).setText(confirmationMessage);

		if (null != buttonText) {
			((Button) v.findViewById(R.id.submit_button)).setText(buttonText);
		}
		if (0 != buttonTextColor) {
			((Button) v.findViewById(R.id.cancel_button)).setTextColor(buttonTextColor);
			((Button) v.findViewById(R.id.submit_button)).setTextColor(buttonTextColor);
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
						String message = enteredMessageEditText.getText().toString().trim();
						submitListener.onSubmit(message);

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

	public ConfirmationFragment.SubmitListener getSubmitListener() {
		return submitListener;
	}

	public void setSubmitListener(ConfirmationFragment.SubmitListener submitListener) {
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

	public String getEnteredMessageLabel() {
		return enteredMessageLabel;
	}

	public void setEnteredMessageLabel(String enteredMessageLabel) {
		this.enteredMessageLabel = enteredMessageLabel;
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

