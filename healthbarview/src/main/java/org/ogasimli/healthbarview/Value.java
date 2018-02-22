/*
 * Copyright (c) 2018 Orkhan Gasimli - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This file is proprietary and confidential.
 * Written by Orkhan Gasimli <orkhan.gasimli@gmail.com> in 2018.
 */

package org.ogasimli.healthbarview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Class holding configurations of value element
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
class Value extends BaseValue {

    //----------------------------------
    // Static fields used as default config values

    static final boolean DEFAULT_VISIBILITY = true;

    static final int DEFAULT_TEXT_COLOR = 0xff009688;

    static final int DEFAULT_TEXT_SIZE = Util.spToPx(16);

    static final int DEFAULT_VALUE = 0;

    private static final String DEFAULT_SUFFIX = "";

    private static final Typeface DEFAULT_FONT = Typeface.MONOSPACE;

    private static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0");

    static final boolean DEFAULT_ANIMATION = false;

    static final long DEFAULT_ANIMATION_DURATION = 4000L;

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final TextPaint mPaint;

    private ValueAnimator mAnimator;

    private boolean mIsAnimated;

    private double mValueToDraw; //for use during animation

    private long mAnimationDuration; //default duration

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    Value(View view, Context context, boolean isVisible, int valueTextColor,
                 int valueTextSize, float value, String valueSuffix, Typeface valueFont,
                 DecimalFormat decimalFormat, boolean isAnimated, long animationDuration) {
        super(isVisible,
                valueTextColor,
                valueTextSize,
                value,
                valueSuffix,
                valueFont,
                decimalFormat);
        mIsAnimated = isAnimated;
        mValueToDraw = DEFAULT_VALUE;
        mAnimationDuration = animationDuration;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Value(View view, Context context) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                DEFAULT_VALUE,
                DEFAULT_SUFFIX,
                DEFAULT_FONT,
                DEFAULT_DECIMAL_FORMAT);
        mIsAnimated = DEFAULT_ANIMATION;
        mValueToDraw = DEFAULT_VALUE;
        mAnimationDuration = DEFAULT_ANIMATION_DURATION;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Value(View view, Context context, float value) {
        super(DEFAULT_VISIBILITY,
                DEFAULT_TEXT_COLOR,
                DEFAULT_TEXT_SIZE,
                value,
                DEFAULT_SUFFIX,
                DEFAULT_FONT,
                DEFAULT_DECIMAL_FORMAT);
        mIsAnimated = DEFAULT_ANIMATION;
        mValueToDraw = DEFAULT_VALUE;
        mAnimationDuration = DEFAULT_ANIMATION_DURATION;
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

    String getTextToDraw() {
        return Util.formatValueText(getValueToDraw(), getSuffix(), getDecimalFormat());
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
    void setValue(double value) {
        super.setValue(value);
    }

    void setValue(double value, double mMinValue, double mMaxValue) {
        double previousValue = getValue();
        if (Util.isBetween(value, mMinValue, mMaxValue)) {
            setValue(value);
        } else {
           setValue(mMinValue);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        if (mIsAnimated) {
            mAnimator = ValueAnimator.ofFloat((float) previousValue, (float) getValue());
            //animationDuration specifies how long it should take to animate the entire graph, so the
            //actual value to use depends on how much the value needs to change
            double changeInValue = Math.abs(getValue() - previousValue);
            long durationToUse = (long) (mAnimationDuration * (changeInValue
                    / Math.max(mMinValue, mMaxValue)));
            mAnimator.setDuration(durationToUse);

            mAnimator.addUpdateListener(valueAnimator -> {
                mValueToDraw = (float) valueAnimator.getAnimatedValue();
                mView.invalidate();
            });

            mAnimator.start();
        } else {
            mValueToDraw = getValue();
        }
        mView.invalidate();
    }

    @Override
    void setSuffix(String suffix) {
        if (suffix != null) {
            super.setSuffix(suffix);
            mView.requestLayout();
        }
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

    @Override
    void setDecimalFormat(DecimalFormat decimalFormat) {
        if (decimalFormat != null) {
            super.setDecimalFormat(decimalFormat);
            mView.requestLayout();
        }
    }

    boolean isAnimated() {
        return mIsAnimated;
    }

    void setAnimated(boolean animated) {
        mIsAnimated = animated;
    }

    double getValueToDraw() {
        return mValueToDraw;
    }

    long getAnimationDuration() {
        return mAnimationDuration;
    }

    void setAnimationDuration(long animationDuration) {
        mAnimationDuration = animationDuration;
    }
}
