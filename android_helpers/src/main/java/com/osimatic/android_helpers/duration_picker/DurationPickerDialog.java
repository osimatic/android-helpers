package com.osimatic.android_helpers.duration_picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.osimatic.android_helpers.R;

/**
 * A simple dialog containing a {@link DurationPicker}.
 * See the <a href="https://developer.android.com/guide/topics/ui/controls/pickers.html">Pickers</a> guide in the
 * android documentation of how to use this.
 * @see DurationPicker
 */
public class DurationPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {
	private static final String DURATION = "duration";
	private final DurationPicker durationInputView;
	private final OnDurationSetListener durationSetListener;

	/**
	 * The callback used to indicate the user is done entering the duration.
	 */
	public interface OnDurationSetListener {
		/**
		 * Called when the user leaves the dialog using the OK button.
		 * @param view the picker view.
		 * @param duration the duration that was entered.
		 */
		void onDurationSet(DurationPicker view, long duration);
	}

	/**
	 * Creates a time duration picker dialog.
	 * @param context the context for the dialog
	 * @param listener the listener to be informed about entered duration
	 * @param duration the initial duration to be shown in the dialog
	 */
	public DurationPickerDialog(Context context, OnDurationSetListener listener, long duration) {
		this(context, listener, duration, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog);
	}

	/**
	 * Creates a time duration picker dialog.
	 * @param context the context for the dialog
	 * @param listener the listener to be informed about entered duration
	 * @param duration the initial duration to be shown in the dialog
	 */
	public DurationPickerDialog(Context context, OnDurationSetListener listener, long duration, int themeId) {
		super(context, themeId);
		durationSetListener = listener;

		final LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.duration_picker_dialog, null);
		setView(view);
		setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this);

		durationInputView = (DurationPicker) view;
		durationInputView.setDuration(duration);
	}

	/**
	 * Creates a time duration picker dialog.
	 * @param context the context for the dialog
	 * @param listener the listener to be informed about entered duration
	 * @param duration the initial duration to be shown in the dialog
	 * @param timeUnits the units of time to display
	 */
	public DurationPickerDialog(Context context, OnDurationSetListener listener, long duration, int themeId, int timeUnits) {
		this(context, listener, duration, themeId);
		durationInputView.setTimeUnits(timeUnits);
	}

	/**
	 * Gets the current entered duration.
	 * @return the current duration in seconds.
	 */
	public DurationPicker getDurationInput() {
		return durationInputView;
	}

	/**
	 * Sets the duration to be shown in the dialog.
	 * @param duration duration in seconds.
	 */
	public void setDuration(long duration) {
		durationInputView.setDuration(duration);
	}

	//
	// internal stuff
	//

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case BUTTON_POSITIVE:
				if (durationSetListener != null) {
					durationSetListener.onDurationSet(durationInputView, durationInputView.getDuration());
				}
				break;
			case BUTTON_NEGATIVE:
				cancel();
				break;
		}
	}

	@NonNull
	@Override
	public Bundle onSaveInstanceState() {
		final Bundle state = super.onSaveInstanceState();
		state.putLong(DURATION, durationInputView.getDuration());
		return state;
	}

	@Override
	public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		final long duration = savedInstanceState.getLong(DURATION);
		durationInputView.setDuration(duration);
	}
}