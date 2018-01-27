package org.ogasimli.healthbarview;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;

/**
 * Class holding configurations of indicator element
 *
 * @author Orkhan Gasimli on 26.01.2018.
 */
class Indicator {

    //----------------------------------
    // Static fields used as default config values

    static final int DEFAULT_WIDTH = Util.dpToPx(0.5f);

    static final int DEFAULT_COLOR = 0xff009688;

    static final int DEFAULT_TOP_OVERFLOW = Util.dpToPx(5);

    static final int DEFAULT_BOTTOM_OVERFLOW = Util.dpToPx(5);

    // endregion static fields
    //----------------------------------

    //----------------------------------
    // Member variables of the class

    private final View mView;

    private final Context mContext;

    private final Paint mPaint;

    private int mWidth;

    private int mColor;

    private int mTopOverflow;

    private int mBottomOverflow;

    // endregion member variables
    //----------------------------------

    //----------------------------------
    // Constructors

    Indicator(View view, Context context, int width, int color, int topOverflow,
                     int bottomOverflow) {
        mWidth = width;
        mColor = color;
        mTopOverflow = topOverflow;
        mBottomOverflow = bottomOverflow;
        mView = view;
        mContext = context;
        mPaint = setupPaint();
    }

    Indicator(View view, Context context) {
        mWidth = DEFAULT_WIDTH;
        mColor = DEFAULT_COLOR;
        mTopOverflow = DEFAULT_TOP_OVERFLOW;
        mBottomOverflow = DEFAULT_BOTTOM_OVERFLOW;
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
        paint.setColor(mColor);
        paint.setStrokeWidth(mWidth);
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

    void setWidth(float width) {
        setWidth(Util.dpToPx(width));
    }

    int getColor() {
        return mColor;
    }

    void setColor(int color) {
        mColor = Util.colorSetter(mContext, color);
        mPaint.setColor(color);
        mView.invalidate();
    }

    int getTopOverflow() {
        return mTopOverflow;
    }

    void setTopOverflow(int topOverflow) {
        mTopOverflow = topOverflow;
        mView.requestLayout();
    }

    void setTopOverflow(float topOverflow) {
        setTopOverflow(Util.dpToPx(topOverflow));
    }

    int getBottomOverflow() {
        return mBottomOverflow;
    }

    void setBottomOverflow(int bottomOverflow) {
        mBottomOverflow = bottomOverflow;
        mView.requestLayout();
    }

    void setBottomOverflow(float bottomOverflow) {
        setBottomOverflow(Util.dpToPx(bottomOverflow));
    }
}
