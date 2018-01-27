package org.ogasimli.healthbarview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Class holding view related static methods
 *
 * @author Orkhan Gasimli on 25.12.2017.
 */
final class Util {

    private static final String LOG_TAG = Util.class.getSimpleName();

    /**
     * Convert dp to pixel
     *
     * @param dp the density independent pixel value
     * @return the pixel value
     */
    static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    /**
     * Convert sp to pixel
     *
     * @param sp the scaled pixel value
     * @return the pixel value
     */
    static int spToPx(float sp) {
        float density = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return Math.round(sp * density);
    }

    /**
     * Determine if value is between v1 and v2
     *
     * @param value the argument which should be checked if it is between v1 and v2
     * @param v1    the first number
     * @param v2    the second number
     */
    static boolean isBetween(double value, double v1, double v2) {
        // Determine the minimum of these two numbers
        double min = Math.min(v1, v2);
        // Determine the maximum of these two numbers
        double max = Math.max(v1, v2);
        return value >= min && value <= max;
    }

    /**
     * Format value with decimal formatter and append suffix
     *
     * @param value the string value which should be formatted
     * @param suffix the suffix to be append to the end of the value
     * @param decimalFormat the decimal format
     * @return the formatted value
     */
    static @NonNull
    String formatValueText(double value, String suffix, DecimalFormat decimalFormat) {
        String result = decimalFormat.format(value);
        if (suffix != null) result += suffix;
        return result;
    }

    /**
     * Extract color int from color resource id and return it
     *
     * @param color the int value representing either color int, or color resource id
     * @return the color
     */
    static int colorSetter(Context context, int color) {
        try {
            return ContextCompat.getColor(context, color);
        } catch (Resources.NotFoundException e) {
            Log.d(LOG_TAG, "Color resource not found.");
            return color;
        }
    }

    /**
     * Calculate width of the text
     *
     * @param text  the string to be drawn
     * @param paint the paint object that will draw the text
     */
    static int determineTextWidth(String text, Paint paint, boolean isVisible) {
        if (isVisible) {
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            return (int) ((paint.measureText(text) + bounds.width()) / 2);
        }
        return 0;
    }

    /**
     * Calculate height of the text
     *
     * @param paint the paint object that will draw the text
     */
    static int determineTextHeight(Paint paint, boolean isVisible) {
        if (isVisible) {
            // Get height from font metrics
            Paint.FontMetrics fm = paint.getFontMetrics();
            return (int) (fm.descent - fm.ascent);
        }
        return 0;
    }
}
