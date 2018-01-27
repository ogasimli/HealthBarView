package org.ogasimli.healthbarview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class holding configurations of label element
 *
 * @author Orkhan Gasimli on 26.01.2018.
 */
class Label  extends BaseTextElement {

    //----------------------------------
    // Static fields used as default config values

    static final boolean DEFAULT_VISIBILITY = true;

    static final int DEFAULT_TEXT_COLOR = 0xff009688;

    static final int DEFAULT_TEXT_SIZE = Util.spToPx(16);

    private static final Typeface DEFAULT_FONT = Typeface.MONOSPACE;

    private static final String[] DEFAULT_LABEL_SET = {"E", "D", "C", "B", "A"};

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final TextPaint mPaint;

    private String[] mLabels;

    private String mLabelToDraw;

    private double[] mLabelsRange;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    Label(View view, Context context, boolean isVisible, int valueTextColor,
                    int valueTextSize, Typeface valueFont, String[] labels) {
        super(isVisible,
                valueTextColor,
                valueTextSize,
                valueFont);
        mLabels = labels;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Label(View view, Context context) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                DEFAULT_FONT);
        mLabels = DEFAULT_LABEL_SET;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Label(View view, Context context, String[] labels) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                DEFAULT_FONT);
        mLabels = labels;
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

    View getView() {
        return mView;
    }

    TextPaint getPaint() {
        return mPaint;
    }

    @Override
    void setVisible(boolean visible) {
        super.setVisible(visible);
        mView.requestLayout();
    }

    @Override
    void setTextColor(int textColor) {
        int color = Util.colorSetter(mContext, textColor);
        super.setTextColor(color);
        mPaint.setColor(color);
        mView.invalidate();
    }

    @Override
    void setTextSize(int textSize) {
        super.setTextSize(textSize);
        mPaint.setTextSize(textSize);
        mView.requestLayout();
    }

    void setTextSize(float textSize) {
        setTextSize(Util.spToPx(textSize));
    }

    @Override
    void setFont(Typeface font) {
        super.setFont(font);
        mPaint.setTypeface(font);
        mView.requestLayout();
    }

    void setFont(@FontRes int font) {
        try {
            setFont(ResourcesCompat.getFont(mContext, font));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    String[] getLabels() {
        return mLabels;
    }

    void setLabels(String[] labels) {
        mLabels = labels;
        mView.requestLayout();
    }

    void setLabels(String labelsString, String regex) {
        List<String> labels = Arrays.asList(labelsString.split(regex));
        setLabels((String[]) labels.toArray());
    }

    String getLabelToDraw() {
        return mLabelToDraw;
    }

    void setLabelToDraw(String labelToDraw) {
        mLabelToDraw = labelToDraw;
    }

    double[] getLabelsRange() {
        return mLabelsRange;
    }

    void setLabelsRange(double[] labelsRange) {
        mLabelsRange = labelsRange;
        mView.invalidate();
    }

    void setLabelsRange(String labelsString, String regex) {
        List<String> ranges = Arrays.asList(labelsString.split(regex));
        mLabelsRange = new double[ranges.size()];
        for (int i = 0; i < ranges.size(); i++) {
            mLabelsRange[i] = Double.parseDouble(ranges.get(i));
        }
        mView.invalidate();
    }

    /**
     * Determine the label corresponding the value within the range from minValue to maxValue
     *
     * @param value    the actual value
     * @param minValue the starting point of range
     * @param maxValue the end point of range
     */
    void setLabelToDraw(double value, double minValue, double maxValue) {
        if (mLabelsRange == null || mLabelsRange.length != mLabels.length) {
            if (minValue > maxValue) Collections.reverse(Arrays.asList(mLabels));
            double fraction = Math.abs(maxValue - minValue) / mLabels.length;
            int index = (int) (Math.abs(value - minValue) / fraction);
            index = Math.min(index, mLabels.length - 1);
            mLabelToDraw = mLabels[index];
        } else {
            for (int i = 0; i < mLabelsRange.length; i++) {
                if (value <= mLabelsRange[i]) {
                    mLabelToDraw = mLabels[i];
                    break;
                }
            }
        }
    }
}
