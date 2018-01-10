package org.ogasimli.healthbarview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Class holding view related static methods
 *
 * Created by Orkhan Gasimli on 25.12.2017.
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
    static boolean isBetween(float value, float v1, float v2) {
        // Determine the minimum of these two numbers
        float min = Math.min(v1, v2);
        // Determine the maximum of these two numbers
        float max = Math.max(v1, v2);
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
    String formatValueText(float value, String suffix, DecimalFormat decimalFormat) {
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
}
