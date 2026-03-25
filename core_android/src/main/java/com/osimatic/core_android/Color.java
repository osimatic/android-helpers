package com.osimatic.core_android;

/**
 * Utility class providing helper methods for color manipulation, conversion, and analysis.
 *
 * <p>This class is not instantiable. All methods are static.
 *
 * @see android.graphics.Color
 * @see <a href="https://developer.android.com/reference/android/graphics/Color">android.graphics.Color</a>
 */
public class Color {

	private Color() {}

	// =============================================================================================
	// Constants
	// =============================================================================================

	/** Saturation multiplier applied when darkening a color for white text readability. */
	private static final float SATURATION_ADJUST = 1.3f;

	/** Value (brightness) multiplier applied when darkening a color for white text readability. */
	private static final float INTENSITY_ADJUST = 0.8f;

	/** Alpha value used to blend a declined calendar event color with white. */
	public static final int DECLINED_EVENT_ALPHA = 0x66;

	/** Alpha value used for text displayed on top of a declined calendar event. */
	public static final int DECLINED_EVENT_TEXT_ALPHA = 0xC0;

	// =============================================================================================
	// Color parsing
	// =============================================================================================

	/**
	 * Parses a color string and returns the corresponding ARGB integer, or {@code defaultColor} if the string is null or invalid.
	 *
	 * <pre>
	 * parseOrDefault("#FF0000", Color.GRAY)  = 0xFFFF0000
	 * parseOrDefault("red",     Color.GRAY)  = 0xFFFF0000
	 * parseOrDefault("nope",    Color.GRAY)  = Color.GRAY
	 * parseOrDefault(null,      Color.GRAY)  = Color.GRAY
	 * </pre>
	 *
	 * @param color        the color string to parse (e.g. {@code "#FF0000"}, {@code "red"}); may be null
	 * @param defaultColor the color to return if {@code color} is null or cannot be parsed
	 * @return the parsed ARGB color, or {@code defaultColor} on failure
	 * @see android.graphics.Color#parseColor(String)
	 */
	public static int parseOrDefault(String color, int defaultColor) {
		if (color == null) {
			return defaultColor;
		}
		try {
			return android.graphics.Color.parseColor(color);
		} catch (IllegalArgumentException e) {
			return defaultColor;
		}
	}

	// =============================================================================================
	// Color conversion / formatting
	// =============================================================================================

	/**
	 * Returns the hex string representation of the given color.
	 *
	 * <pre>
	 * toHexString(0xFF0000FF, false)  = "#0000ff"
	 * toHexString(0xFF0000FF, true)   = "#ff0000ff"
	 * </pre>
	 *
	 * @param color     the color to convert
	 * @param withAlpha {@code true} to include the alpha channel (8-digit hex), {@code false} for RGB only (6-digit hex)
	 * @return a hex color string prefixed with {@code #}
	 */
	public static String toHexString(int color, boolean withAlpha) {
		if (withAlpha) {
			return String.format("#%08x", color & 0xffffffff);
		}
		return String.format("#%06x", color & 0x00ffffff);
	}

	/**
	 * Returns the 6-digit hex string representation of the given color, without the alpha channel.
	 *
	 * @param color the color to convert
	 * @return a hex color string prefixed with {@code #}, e.g. {@code "#ff0000"}
	 * @see #toHexString(int, boolean)
	 */
	public static String toHexString(int color) {
		return toHexString(color, false);
	}

	// =============================================================================================
	// Color manipulation
	// =============================================================================================

	/**
	 * Darkens the given color to ensure that white text is clearly visible on top of it, by increasing saturation and reducing brightness in HSV space.
	 *
	 * @param color the color to darken
	 * @return the darkened color as an ARGB integer
	 * @see android.graphics.Color#colorToHSV
	 * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSL and HSV — Wikipedia</a>
	 */
	public static int darkenForWhiteText(int color) {
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		hsv[1] = Math.min(hsv[1] * SATURATION_ADJUST, 1.0f);
		hsv[2] = hsv[2] * INTENSITY_ADJUST;
		return android.graphics.Color.HSVToColor(hsv);
	}

	/** @deprecated Use {@link #darkenForWhiteText(int)} instead. */
	@Deprecated
	public static int getDisplayColorFromColor(int color) {
		return darkenForWhiteText(color);
	}

	/**
	 * Blends the given color with white using {@link #DECLINED_EVENT_ALPHA} as the opacity, producing the color used for declined calendar events.
	 *
	 * @param color the source color to blend with white
	 * @return the blended ARGB color with full opacity
	 * @see #DECLINED_EVENT_ALPHA
	 * @see <a href="https://en.wikipedia.org/wiki/Alpha_compositing">Alpha compositing — Wikipedia</a>
	 */
	public static int blendWithWhite(int color) {
		int bg = 0xffffffff;
		int a = DECLINED_EVENT_ALPHA;
		int r = (((color & 0x00ff0000) * a) + ((bg & 0x00ff0000) * (0xff - a))) & 0xff000000;
		int g = (((color & 0x0000ff00) * a) + ((bg & 0x0000ff00) * (0xff - a))) & 0x00ff0000;
		int b = (((color & 0x000000ff) * a) + ((bg & 0x000000ff) * (0xff - a))) & 0x0000ff00;
		return (0xff000000) | ((r | g | b) >> 8);
	}

	/** @deprecated Use {@link #blendWithWhite(int)} instead. */
	@Deprecated
	public static int getDeclinedColorFromColor(int color) {
		return blendWithWhite(color);
	}

	/**
	 * Returns a copy of the given color with the specified alpha channel.
	 *
	 * @param color the source color
	 * @param alpha the alpha value in the range [0, 255]; 0 is fully transparent, 255 is fully opaque
	 * @return the color with the new alpha value
	 */
	public static int withAlpha(int color, int alpha) {
		return (color & 0x00ffffff) | ((alpha & 0xff) << 24);
	}

	/**
	 * Lightens the given color by the specified factor in HSV space.
	 *
	 * @param color  the color to lighten
	 * @param factor a value in the range (0, 1]; 1.0 returns the original color, values closer to 0 produce lighter results
	 * @return the lightened color as an ARGB integer
	 * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSL and HSV — Wikipedia</a>
	 */
	public static int lighten(int color, float factor) {
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		hsv[2] = Math.min(hsv[2] / factor, 1.0f);
		return android.graphics.Color.HSVToColor(hsv);
	}

	/**
	 * Darkens the given color by the specified factor in HSV space.
	 *
	 * @param color  the color to darken
	 * @param factor a value in the range (0, 1]; 1.0 returns the original color, values closer to 0 produce darker results
	 * @return the darkened color as an ARGB integer
	 * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">HSL and HSV — Wikipedia</a>
	 */
	public static int darken(int color, float factor) {
		float[] hsv = new float[3];
		android.graphics.Color.colorToHSV(color, hsv);
		hsv[2] = Math.max(hsv[2] * factor, 0.0f);
		return android.graphics.Color.HSVToColor(hsv);
	}

	/**
	 * Blends two ARGB colors together using the given ratio.
	 *
	 * @param color1 the first color (returned as-is when {@code ratio} is 0.0)
	 * @param color2 the second color (returned as-is when {@code ratio} is 1.0)
	 * @param ratio  the blend ratio in the range [0.0, 1.0]; 0.0 returns {@code color1}, 1.0 returns {@code color2}
	 * @return the blended ARGB color
	 * @see <a href="https://en.wikipedia.org/wiki/Alpha_compositing">Alpha compositing — Wikipedia</a>
	 */
	public static int blend(int color1, int color2, float ratio) {
		float inverse = 1.0f - ratio;
		int a = (int) (((color1 >> 24 & 0xff) * inverse) + ((color2 >> 24 & 0xff) * ratio));
		int r = (int) (((color1 >> 16 & 0xff) * inverse) + ((color2 >> 16 & 0xff) * ratio));
		int g = (int) (((color1 >> 8  & 0xff) * inverse) + ((color2 >> 8  & 0xff) * ratio));
		int b = (int) (((color1       & 0xff) * inverse) + ((color2       & 0xff) * ratio));
		return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}

	// =============================================================================================
	// Color analysis
	// =============================================================================================

	/**
	 * Returns {@code true} if the given color is considered dark.
	 *
	 * <p>Luminance is computed using the ITU-R BT.601 formula: {@code 0.299R + 0.587G + 0.114B}. A color is considered dark if its luminance is below 0.5.
	 *
	 * @param color the color to evaluate
	 * @return {@code true} if the color is dark, {@code false} otherwise
	 * @see <a href="https://en.wikipedia.org/wiki/Luma_(video)">Luma (video) — Wikipedia</a>
	 */
	public static boolean isDark(int color) {
		double r = (color >> 16 & 0xff) / 255.0;
		double g = (color >> 8  & 0xff) / 255.0;
		double b = (color       & 0xff) / 255.0;
		double luminance = 0.299 * r + 0.587 * g + 0.114 * b;
		return luminance < 0.5;
	}

	/**
	 * Returns the best foreground text color (black or white) for readable contrast against the given background color.
	 *
	 * @param backgroundColor the background color to evaluate
	 * @return {@link android.graphics.Color#WHITE} if the background is dark, {@link android.graphics.Color#BLACK} otherwise
	 * @see #isDark(int)
	 */
	public static int getContrastingTextColor(int backgroundColor) {
		return isDark(backgroundColor) ? android.graphics.Color.WHITE : android.graphics.Color.BLACK;
	}
}