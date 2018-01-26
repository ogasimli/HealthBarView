package org.ogasimli.healthbarview.model;

import android.graphics.Typeface;

import java.text.DecimalFormat;

/**
 * Abstract POJO class holding configurations of value, minValue and maxValue
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
public abstract class BaseValue extends BaseTextElement {

    //----------------------------------
    // Member variables of the class

    private String mSuffix;

    private DecimalFormat mDecimalFormat;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    BaseValue(boolean isVisible, int textColor, int textSize, float value,
              String suffix, Typeface font, DecimalFormat decimalFormat) {
        super(isVisible, textColor, textSize, value, font);
        mSuffix = suffix;
        mDecimalFormat = decimalFormat;
    }

    // endregion constructors
    //----------------------------------

    //----------------------------------
    // Setter & getters

    public String getSuffix() {
        return mSuffix;
    }

    public void setSuffix(String suffix) {
        mSuffix = suffix;
    }

    public DecimalFormat getDecimalFormat() {
        return mDecimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        mDecimalFormat = decimalFormat;
    }

    // endregion setter & getters
    //----------------------------------
}
