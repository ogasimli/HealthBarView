package org.ogasimli.healthbarview.model;

import org.ogasimli.healthbarview.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

import java.text.DecimalFormat;

/**
 * POJO class holding configurations of maxValue
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
public class MaxValue extends BaseValue {

    //----------------------------------
    // Static fields used as default config values

    public static final boolean DEFAULT_VISIBILITY = false;

    public static final int DEFAULT_TEXT_COLOR = 0xff009688;

    public static final int DEFAULT_TEXT_SIZE = Util.spToPx(16);

    public static final int DEFAULT_VALUE = 100;

    private static final String DEFAULT_SUFFIX = "";

    private static final Typeface DEFAULT_FONT = Typeface.MONOSPACE;

    private static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0");

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final TextPaint mPaint;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    public MaxValue(View view, Context context, boolean showValue, int valueTextColor,
                    int valueTextSize, float value, String valueSuffix, Typeface valueFont,
                    DecimalFormat decimalFormat) {
        super(showValue,
                valueTextColor,
                valueTextSize,
                value,
                valueSuffix,
                valueFont,
                decimalFormat);
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    public MaxValue(View view, Context context) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                DEFAULT_VALUE,
                DEFAULT_SUFFIX,
                DEFAULT_FONT,
                DEFAULT_DECIMAL_FORMAT);
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    public MaxValue(View view, Context context, float value) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                value,
                DEFAULT_SUFFIX,
                DEFAULT_FONT,
                DEFAULT_DECIMAL_FORMAT);
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    // endregion constructors
    //----------------------------------

    //----------------------------------
    // Helper methods

    /**
     * Setup paint object
     * Call only if changes to color or size properties are not visible.
     *
     * @return setup and return TextPaint object
     */
    private TextPaint setupPaint() {
        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTypeface(getFont());
        paint.setColor(getTextColor());
        paint.setTextSize(getTextSize());
        return paint;
    }

    // endregion helper methods
    //----------------------------------

    //----------------------------------
    // Setter & getters

    public View getView() {
        return mView;
    }

    public TextPaint getPaint() {
        return mPaint;
    }

    public String getTextToDraw() {
        return Util.formatValueText(getValue(), getSuffix(), getDecimalFormat());
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        mView.invalidate();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        mView.requestLayout();
    }

    @Override
    public void setTextColor(int textColor) {
        int color = Util.colorSetter(mContext, textColor);
        super.setTextColor(color);
        mPaint.setColor(color);
        mView.invalidate();
    }

    @Override
    public void setTextSize(int textSize) {
        super.setTextSize(textSize);
        mPaint.setTextSize(textSize);
        mView.requestLayout();
    }

    @Override
    public void setSuffix(String suffix) {
        if (suffix != null) {
            super.setSuffix(suffix);
            mView.requestLayout();
        }
    }

    @Override
    public void setFont(Typeface font) {
        super.setFont(font);
        mPaint.setTypeface(font);
        mView.requestLayout();
    }

    @Override
    public void setDecimalFormat(DecimalFormat decimalFormat) {
        if (decimalFormat != null) {
            super.setDecimalFormat(decimalFormat);
            mView.requestLayout();
        }
    }
}
