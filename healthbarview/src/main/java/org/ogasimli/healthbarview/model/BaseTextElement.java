package org.ogasimli.healthbarview.model;

import android.graphics.Typeface;

/**
 * Abstract POJO class holding configurations of text elements
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
public abstract class BaseTextElement {

    //----------------------------------
    // Member variables of the class

    private boolean mIsVisible;

    private int mTextColor;

    private int mTextSize;

    private float mValue;

    private Typeface mFont;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    BaseTextElement(boolean isVisible, int textColor, int textSize, float value, Typeface font) {
        mIsVisible = isVisible;
        mTextColor = textColor;
        mTextSize = textSize;
        mValue = value;
        mFont = font;
    }

    // endregion constructors
    //----------------------------------

    //----------------------------------
    // Setter & getters

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        mValue = value;
    }

    public Typeface getFont() {
        return mFont;
    }

    public void setFont(Typeface font) {
        mFont = font;
    }

    // endregion setter & getters
    //----------------------------------
}
