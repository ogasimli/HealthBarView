/*
 * Copyright (c) 2018 Orkhan Gasimli - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This file is proprietary and confidential.
 * Written by Orkhan Gasimli <orkhan.gasimli@gmail.com> in 2018.
 */

package org.ogasimli.healthbarview;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * Class holding configurations of bar stroke element
 *
 * @author Orkhan Gasimli on 26.01.2018.
 */
class Stroke {

    //----------------------------------
    // Static fields used as default config values

    static final int DEFAULT_WIDTH = Util.dpToPx(1);

    static final int DEFAULT_COLOR = 0xff009688;

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final Paint mPaint;

    private int mWidth;

    private int mColor;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    Stroke(View view, Context context, int width, int color) {
        mWidth = width;
        mColor = color;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Stroke(View view, Context context) {
        mWidth = DEFAULT_WIDTH;
        mColor = DEFAULT_COLOR;
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
    private Paint setupPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mWidth);
        paint.setColor(mColor);
        return paint;
    }

    // endregion helper methods
    //----------------------------------

    //----------------------------------
    // Setter & getters

    View getView() {
        return mView;
    }

    Paint getPaint() {
        return mPaint;
    }

    int getWidth() {
        return mWidth;
    }

    void setWidth(int width) {
        mWidth = width;
        mPaint.setStrokeWidth(width);
        mView.requestLayout();
    }

    int getColor() {
        return mColor;
    }

    void setColor(int color) {
        mColor = Util.colorSetter(mContext, color);
        mPaint.setColor(color);
        mView.invalidate();
    }
}
