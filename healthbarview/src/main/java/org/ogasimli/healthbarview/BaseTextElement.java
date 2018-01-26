package org.ogasimli.healthbarview;

import android.graphics.Typeface;

/**
 * Abstract POJO class holding configurations of text elements
 *
 * @author Orkhan Gasimli on 24.01.2018.
 */
abstract class BaseTextElement {

    //----------------------------------
    // Member variables of the class

    private boolean mIsVisible;

    private int mTextColor;

    private int mTextSize;

    private Typeface mFont;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    BaseTextElement(boolean isVisible, int textColor, int textSize, Typeface font) {
        mIsVisible = isVisible;
        mTextColor = textColor;
        mTextSize = textSize;
        mFont = font;
    }

    // endregion constructors
    //----------------------------------

    //----------------------------------
    // Setter & getters

    boolean isVisible() {
        return mIsVisible;
    }

    void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    int getTextColor() {
        return mTextColor;
    }

    void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    int getTextSize() {
        return mTextSize;
    }

    void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    Typeface getFont() {
        return mFont;
    }

    void setFont(Typeface font) {
        mFont = font;
    }

    // endregion setter & getters
    //----------------------------------
}
