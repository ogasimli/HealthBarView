package org.ogasimli.healthbarview;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/**
 * Class holding configurations of bar fill element
 *
 * @author Orkhan Gasimli on 26.01.2018.
 */
class Fill {

    //----------------------------------
    // Static fields used as default config values

    static final int DEFAULT_START_COLOR = 0xffffc200;

    static final int DEFAULT_END_COLOR = 0xff7bfbaf;

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final Paint mPaint;

    private int mStartColor;

    private int mEndColor;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    Fill(View view, Context context, int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Fill(View view, Context context) {
        mStartColor = DEFAULT_START_COLOR;
        mEndColor = DEFAULT_END_COLOR;
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
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    /**
     * Set gradient shader to paint object
     *
     * @param left      The left-coordinate for the start of the gradient line
     * @param top       The top-coordinate for the start of the gradient line
     * @param right     The right-coordinate for the end of the gradient line
     * @param bottom    The bottom-coordinate for the end of the gradient line
     */
    void setPainShader(float left, float top, float right, float bottom) {
        mPaint.setShader(new LinearGradient(left, top, right, bottom, mStartColor, mEndColor,
                Shader.TileMode.CLAMP));
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

    int getStartColor() {
        return mStartColor;
    }

    void setStartColor(int startColor) {
        mStartColor = Util.colorSetter(mContext, startColor);
        mPaint.setColor(startColor);
        mView.invalidate();
    }

    int getEndColor() {
        return mEndColor;
    }

    void setEndColor(int endColor) {
        mEndColor = Util.colorSetter(mContext, endColor);
        mPaint.setColor(endColor);
        mView.invalidate();
    }
}
