package com.osimatic.android_helpers;

import android.util.Log;

public class Color {
	private static final float SATURATION_ADJUST = 1.3f;
	private static final float INTENSITY_ADJUST = 0.8f;
	public static final int DECLINED_EVENT_ALPHA = 0x66;
	public static final int DECLINED_EVENT_TEXT_ALPHA = 0xC0;

	/**
	 * For devices with Jellybean or later, darkens the given color to ensure that white text is
	 * clearly visible on top of it.  For devices prior to Jellybean, does nothing, as the
	 * sync adapter handles the color change.
	 *
	 * @param color Couleur
	 */
	public static int getDisplayColorFromColor(int color) {
		if (!Device.isJellybeanOrLater()) {
			return color;
		}

		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		hsv[1] = Math.min(hsv[1] * SATURATION_ADJUST, 1.0f);
		hsv[2] = hsv[2] * INTENSITY_ADJUST;
		return android.graphics.Color.HSVToColor(hsv);
	}

	public static String getHexaColor(int color) {
		return getHexaColor(color, false);
	}
	public static String getHexaColor(int color, boolean withAlpha) {
		if (withAlpha) {
			return String.format("#%08x", color & 0xffffffff);
		}
		return String.format("#%06x", color & 0x00ffffff);
	}

	// This takes a color and computes what it would look like blended with
	// white. The result is the color that should be used for declined events.
	public static int getDeclinedColorFromColor(int color) {
		int bg = 0xffffffff;
		int a = DECLINED_EVENT_ALPHA;
		int r = (((color & 0x00ff0000) * a) + ((bg & 0x00ff0000) * (0xff - a))) & 0xff000000;
		int g = (((color & 0x0000ff00) * a) + ((bg & 0x0000ff00) * (0xff - a))) & 0x00ff0000;
		int b = (((color & 0x000000ff) * a) + ((bg & 0x000000ff) * (0xff - a))) & 0x0000ff00;
		return (0xff000000) | ((r | g | b) >> 8);
	}
}
