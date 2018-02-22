/*
 * Copyright (c) 2018 Orkhan Gasimli - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This file is proprietary and confidential.
 * Written by Orkhan Gasimli <orkhan.gasimli@gmail.com> in 2018.
 */

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

    private double mValue;

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

    double getValue() {
        return mValue;
    }

    void setValue(double value) {
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
