package org.ogasimli.healthbarview;

import android.graphics.Typeface;

import java.text.DecimalFormat;

/**
 * Abstract POJO class holding configurations of value, minValue and maxValue
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
abstract class BaseValue extends BaseTextElement {

    //----------------------------------
    // Member variables of the class

    private float mValue;

    private String mSuffix;

    private DecimalFormat mDecimalFormat;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    BaseValue(boolean isVisible, int textColor, int textSize, float value,
              String suffix, Typeface font, DecimalFormat decimalFormat) {
        super(isVisible, textColor, textSize, font);
        mValue = value;
        mSuffix = suffix;
        mDecimalFormat = decimalFormat;
    }

    // endregion constructors
    //----------------------------------

    //----------------------------------
    // Setter & getters

    float getValue() {
        return mValue;
    }

    void setValue(float value) {
        mValue = value;
    }

    String getSuffix() {
        return mSuffix;
    }

    void setSuffix(String suffix) {
        mSuffix = suffix;
    }

    DecimalFormat getDecimalFormat() {
        return mDecimalFormat;
    }

    void setDecimalFormat(DecimalFormat decimalFormat) {
        mDecimalFormat = decimalFormat;
    }

    // endregion setter & getters
    //----------------------------------
}