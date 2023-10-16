package com.osimatic.android_helpers.duration_picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.osimatic.android_helpers.R;

/**
 * A preference that allows the user to pick a time duration using a {@link DurationPicker}.
 * <p>
 * Use this like every other preference (for example a {@code EditTextPreference}) in your preference XML file, but
 * be aware of the following:
 * <ol>
 * <li>The {@code android:defaultValue} specifies the default duration in seconds.
 * <li>You can use one of the {@code PLACEHOLDER_*} strings in your summary which will be replaced by the duration.
 * For example a summary could look like {@code "Remind me in ${m:ss} minute(s)."}
 * </ol>
 *
 * @see DurationPicker
 * @see DurationPickerDialog
 */
public class DurationPickerPreference extends DialogPreference {
	/**
	 * Placeholder in the summary that will be replaced by the current duration value.
	 */
	public static final String PLACEHOLDER_HOURS_MINUTES_SECONDS = "${h:mm:ss}";
	/**
	 * Placeholder in the summary that will be replaced by the current duration value.
	 */
	public static final String PLACEHOLDER_MINUTES_SECONDS = "${m:ss}";
	/**
	 * Placeholder in the summary that will be replaced by the current duration value.
	 */
	public static final String PLACEHOLDER_SECONDS = "${s}";

	private long duration = 0;
	private DurationPicker picker = null;
	private String summaryTemplate;

	public DurationPickerPreference(Context context) {
		this(context, null);
	}

	public DurationPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	/**
	 * Set the current duration.
	 *
	 * @param duration duration in seconds
	 */
	public void setDuration(long duration) {
		this.duration = duration;
		persistLong(duration);
		notifyDependencyChange(shouldDisableDependents());
		notifyChanged();
	}

	/**
	 * Get the current duration.
	 *
	 * @return duration in seconds.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Gets the {@link DurationPicker} used by this dialog.
	 *
	 * @return the picker used by this dialog.
	 */
	public DurationPicker getDurationPicker() {
		return picker;
	}

	//
	// internal stuff
	//

	private void updateDescription() {
		if (summaryTemplate == null) {
			summaryTemplate = getSummary().toString();
		}
		final String summary = summaryTemplate
				.replace(PLACEHOLDER_HOURS_MINUTES_SECONDS, DurationUtils.formatHoursMinutesSeconds(duration))
				.replace(PLACEHOLDER_MINUTES_SECONDS, DurationUtils.formatMinutesSeconds(duration)
						.replace(PLACEHOLDER_SECONDS, DurationUtils.formatSeconds(duration)));
		setSummary(summary);
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder.setTitle(null).setIcon(null));
	}

	@Override
	protected View onCreateDialogView() {
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		picker = initPicker((DurationPicker) inflater.inflate(R.layout.duration_picker_dialog, null));
		return picker;
	}

	protected DurationPicker initPicker(DurationPicker timePicker) {
		return timePicker;
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		picker.setDuration(duration);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			final long newDuration = picker.getDuration();

			if (!callChangeListener(newDuration)) {
				return;
			}

			// persist
			setDuration(newDuration);
			updateDescription();
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (long) a.getInt(index, 0);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		final long duration;
		if (restorePersistedValue)
			duration = getPersistedLong(0);
		else
			duration = Long.parseLong(defaultValue.toString());

		// need to persist here for default value to work
		setDuration(duration);
		updateDescription();
	}
}