package org.ogasimli.healthbarview;


import org.ogasimli.healthbarview.library.R;
import org.ogasimli.healthbarview.model.MaxValue;
import org.ogasimli.healthbarview.model.MinValue;
import org.ogasimli.healthbarview.model.Value;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Custom health bar like view
 *
 * @author Orkhan Gasimli on 27.12.2017.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class HealthBarView extends View {

    private static final String LOG_TAG = HealthBarView.class.getSimpleName();

    // Context
    private Context mContext;

    // Extra padding to avoid cutting of paint
    private static final int EXTRA_PADDING = Util.dpToPx(2);

    // Minimal bar height
    private static final int BAR_FILL_MIN_HEIGHT = Util.dpToPx(25);

    // Minimal padding between bar and minValue
    private static final int MIN_HORIZONTAL_PADDING = Util.dpToPx(3);

    // Minimal padding between bar and maxValue
    private static final int MAX_HORIZONTAL_PADDING = Util.dpToPx(2);

    // MinValue field
    private final MinValue mMinValue;

    // MaxValue field
    private final MaxValue mMaxValue;

    // Bar
    // Stroke fields
    private int mStrokeWidth = Util.dpToPx(1);
    private int mStrokeColor = 0xff009688;
    // Fill fields
    private int mColorFrom = 0xffffc200;
    private int mColorTo = 0xff7bfbaf;

    // Indicator fields
    private int mIndicatorWidth = Util.dpToPx(0.5f);
    private int mIndicatorColor = 0xff009688;

    // Value field
    private final Value mValue;

    // Label fields
    private boolean mShowLabel = true;
    private int mLabelTextColor = 0xff009688;
    private int mLabelTextSize = Util.spToPx(16);
    private Typeface mLabelFont = Typeface.MONOSPACE;
    private String[] mLabels = {"Poor", "Below Average", "Average", "Above Average", "Good",
            "Excellent"};
    private String mLabel;

    // Paints
    private Paint mBarStrokePaint = new Paint();
    private Paint mBarFillPaint = new Paint();
    private Paint mIndicatorPaint = new Paint();
    private TextPaint mLabelPaint = new TextPaint();

    // Rectangles
    private RectF mBarStrokeRec = new RectF();
    private RectF mBarFillRec = new RectF();
    private RectF mIndicatorRec = new RectF();

    // Dimensions and coordinates of elements
    // View
    private int mViewWidth;
    private int mViewHeight;
    // MinValue
    private float mMinValueLeft;
    private float mMinValueRight;
    private float mMinValueBottom;
    private float mMinValueTop;
    private int mMinValueWidth;
    private int mMinValueHeight;
    // MaxValue
    private float mMaxValueLeft;
    private float mMaxValueRight;
    private float mMaxValueBottom;
    private float mMaxValueTop;
    private int mMaxValueWidth;
    private int mMaxValueHeight;
    // Bar
    // Stroke
    private int mBarStrokeLeft;
    private int mBarStrokeBottom;
    private int mBarStrokeTop;
    private int mBarStrokeRight;
    // Fill
    private int mBarFillLeft;
    private int mBarFillTop;
    private int mBarFillRight;
    private int mBarFillBottom;
    // Indicator
    private int mIndicatorOverflowLength = Util.dpToPx(10);
    private int mIndicatorLeft;
    private int mIndicatorTop;
    private int mIndicatorRight;
    private int mIndicatorBottom;
    // Value
    private int mValueHeight;
    private int mValueWidth;
    // Label
    private int mLabelHeight;
    private int mLabelWidth;

    /**
     * The constructor for the HealthBarView
     *
     * @param context The context.
     */
    public HealthBarView(Context context) {
        super(context);
        mContext = context;

        // Init value
        mMinValue = new MinValue(this, context);
        mMaxValue = new MaxValue(this, context);
        mValue = new Value(this, context);

        // Init view
        init();
    }

    /**
     * The constructor for the HealthBarView
     *
     * @param context The context.
     * @param attrs   The attributes.
     */
    public HealthBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        // Init value
        mValue = new Value(this, context);
        mMinValue = new MinValue(this, context);
        mMaxValue = new MaxValue(this, context);

        // Init view
        init(context, attrs);
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT and
     * WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMinValue(canvas);
        drawMaxValue(canvas);
        drawBarStroke(canvas);
        drawBarFill(canvas);
        drawIndicator(canvas);
        drawValue(canvas);
        drawLabel(canvas);
    }

    //----------------------------------
    // region helper

    /**
     * The method to initialize view
     *
     * @param context The context.
     * @param attrs   The attributes.
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.HealthBarView));
        init();
    }

    /**
     * The method to initialize view
     */
    private void init() {
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        setupPaints();
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {

        /* -------------- MinValue attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValue)) {
            setMinValue(a.getFloat(R.styleable.HealthBarView_hbv_minValue, MinValue.DEFAULT_VALUE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_showMinValue)) {
            setShowMinValue(a.getBoolean(R.styleable.HealthBarView_hbv_showMinValue,
                    MinValue.DEFAULT_VISIBILITY));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValueTextColor)) {
            setMinValueTextColor(a.getInt(R.styleable.HealthBarView_hbv_minValueTextColor,
                    MinValue.DEFAULT_TEXT_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValueTextSize)) {
            setMinValueTextSize(a.getDimensionPixelSize(R.styleable
                            .HealthBarView_hbv_minValueTextSize,
                    MinValue.DEFAULT_TEXT_SIZE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValueSuffix)) {
            setMinValueSuffix(a.getString(R.styleable.HealthBarView_hbv_minValueSuffix));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValueFont)) {
            setMinValueFont(a.getResourceId(R.styleable.HealthBarView_hbv_minValueFont, -1));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValueDecimalFormat)) {
            try {
                String pattern = a.getString(R.styleable.HealthBarView_hbv_minValueDecimalFormat);
                setMinValueDecimalFormat(new DecimalFormat(pattern));
            } catch (Exception exception) {
                Log.w(LOG_TAG, exception.getMessage());
            }
        }

        /* -------------- End of minValue attributes -------------- */

        /* -------------- MaxValue attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValue)) {
            setMaxValue(a.getFloat(R.styleable.HealthBarView_hbv_maxValue, MaxValue.DEFAULT_VALUE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_showMaxValue)) {
            setShowMaxValue(a.getBoolean(R.styleable.HealthBarView_hbv_showMaxValue,
                    MaxValue.DEFAULT_VISIBILITY));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValueTextColor)) {
            setMaxValueTextColor(a.getInt(R.styleable.HealthBarView_hbv_maxValueTextColor,
                    MaxValue.DEFAULT_TEXT_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValueTextSize)) {
            setMaxValueTextSize(a.getDimensionPixelSize(R.styleable
                            .HealthBarView_hbv_maxValueTextSize,
                    MaxValue.DEFAULT_TEXT_SIZE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValueSuffix)) {
            setMaxValueSuffix(a.getString(R.styleable.HealthBarView_hbv_maxValueSuffix));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValueFont)) {
            setMaxValueFont(a.getResourceId(R.styleable.HealthBarView_hbv_maxValueFont, -1));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValueDecimalFormat)) {
            try {
                String pattern = a.getString(R.styleable.HealthBarView_hbv_maxValueDecimalFormat);
                setMaxValueDecimalFormat(new DecimalFormat(pattern));
            } catch (Exception exception) {
                Log.w(LOG_TAG, exception.getMessage());
            }
        }

        /* -------------- End of maxValue attributes -------------- */

        /* -------------- Bar stroke attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_strokeWidth)) {
            setStrokeWidth(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_strokeWidth,
                    mStrokeWidth));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_strokeColor)) {
            setStrokeColor(a.getInt(R.styleable.HealthBarView_hbv_strokeColor, mStrokeColor));
        }

        /* -------------- End of bar stroke attributes -------------- */

        /* -------------- Bar fill attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorFrom)) {
            setColorFrom(a.getInt(R.styleable.HealthBarView_hbv_colorFrom, mColorFrom));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorTo)) {
            setColorTo(a.getInt(R.styleable.HealthBarView_hbv_colorTo, mColorTo));
        }

        /* -------------- End of bar fill attributes -------------- */

        /* -------------- Indicator attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorWidth)) {
            setIndicatorWidth(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_indicatorWidth,
                    mIndicatorWidth));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorColor)) {
            setIndicatorColor(a.getInt(R.styleable.HealthBarView_hbv_indicatorColor, mIndicatorColor));
        }

        /* -------------- End of indicator attributes -------------- */

        /* -------------- Value attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_value)) {
            setValue(a.getFloat(R.styleable.HealthBarView_hbv_value, Value.DEFAULT_VALUE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_showValue)) {
            setShowValue(a.getBoolean(R.styleable.HealthBarView_hbv_showValue,
                    Value.DEFAULT_VISIBILITY));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueTextColor)) {
            setValueTextColor(a.getInt(R.styleable.HealthBarView_hbv_valueTextColor,
                    Value.DEFAULT_TEXT_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueTextSize)) {
            setValueTextSize(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_valueTextSize,
                    Value.DEFAULT_TEXT_SIZE));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_animated)) {
            setAnimated(a.getBoolean(R.styleable.HealthBarView_hbv_animated,
                    Value.DEFAULT_ANIMATION));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_animationDuration)) {
            setAnimationDuration(a.getInt(R.styleable.HealthBarView_hbv_animationDuration,
                    (int) Value.DEFAULT_ANIMATION_DURATION));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueSuffix)) {
            setValueSuffix(a.getString(R.styleable.HealthBarView_hbv_valueSuffix));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueFont)) {
            setValueFont(a.getResourceId(R.styleable.HealthBarView_hbv_valueFont, -1));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueDecimalFormat)) {
            try {
                String pattern = a.getString(R.styleable.HealthBarView_hbv_valueDecimalFormat);
                setValueDecimalFormat(new DecimalFormat(pattern));
            } catch (Exception exception) {
                Log.w(LOG_TAG, exception.getMessage());
            }
        }

        /* -------------- End of value attributes -------------- */

        /* -------------- Label attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_showLabel)) {
            setShowLabel(a.getBoolean(R.styleable.HealthBarView_hbv_showLabel, mShowLabel));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labelTextColor)) {
            setLabelTextColor(a.getInt(R.styleable.HealthBarView_hbv_labelTextColor, mLabelTextColor));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labelTextSize)) {
            setLabelTextSize(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_labelTextSize,
                    mLabelTextSize));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labels)) {
            setLabels(a.getString(R.styleable.HealthBarView_hbv_labels), Pattern.quote(","));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labelFont)) {
            setLabelFont(a.getResourceId(R.styleable.HealthBarView_hbv_labelFont, -1));
        }

        /* -------------- End of label attributes -------------- */

        // Recycle
        a.recycle();
    }

    /**
     * Determine bounds and draw minValue
     *
     * @param canvas the canvas object
     */
    private void drawMinValue(Canvas canvas) {
        if (isShowMinValue()) {
            // Determine width and height of value text
            determineMinValueWidth(mMinValue.getTextToDraw(), mMinValue.getPaint(), isShowValue());
            determineMinValueHeight(mMinValue.getPaint(), isShowValue());
            // Determine x and y coordinates for value
            mMinValueLeft = getPaddingLeft();
            mMinValueRight = mMinValueLeft + mMinValueWidth;
            mMinValueBottom = (float) ((mViewHeight - paddingBottom() -
                    (mIndicatorOverflowLength / 2) - mLabelHeight - mStrokeWidth / 2) -
                    (((mViewHeight - paddingBottom() - (mIndicatorOverflowLength / 2) -
                            mLabelHeight - mStrokeWidth / 2) - (paddingTop() +
                            (mIndicatorOverflowLength / 2) + mValueHeight + mStrokeWidth / 2)) / 2)
                    + (mMinValueHeight / 2.0));
            mMinValueTop = mMinValueBottom - mMinValueHeight;

            // Draw
            canvas.drawText(mMinValue.getTextToDraw(), mMinValueLeft, mMinValueBottom,
                    mMinValue.getPaint());
        }
    }

    /**
     * Determine bounds and draw maxValue
     *
     * @param canvas the canvas object
     */
    private void drawMaxValue(Canvas canvas) {
        if (isShowMaxValue()) {
            // Determine width and height of value text
            determineMaxValueWidth(mMaxValue.getTextToDraw(), mMaxValue.getPaint(), isShowValue());
            determineMaxValueHeight(mMaxValue.getPaint(), isShowValue());
            // Determine x and y coordinates for value
            mMaxValueLeft = mViewWidth - getPaddingRight() - mMaxValueWidth;
            mMaxValueRight = mMaxValueLeft + mMaxValueWidth;
            mMaxValueBottom = (float) ((mViewHeight - paddingBottom() -
                    (mIndicatorOverflowLength / 2.0) - mLabelHeight - mStrokeWidth / 2.0) -
                    (((mViewHeight - paddingBottom() - (mIndicatorOverflowLength / 2.0) -
                            mLabelHeight - mStrokeWidth / 2.0) - (paddingTop() +
                            (mIndicatorOverflowLength / 2.0) + mValueHeight + mStrokeWidth / 2.0))
                            / 2)
                    + (mMaxValueHeight / 2.0));
            mMaxValueTop = mMaxValueBottom - mMaxValueHeight;

            // Draw
            canvas.drawText(mMaxValue.getTextToDraw(), mMaxValueLeft, mMaxValueBottom,
                    mMaxValue.getPaint());
        }
    }

    /**
     * Determine bounds and draw bar stroke
     * P.S. Consider that stroke will be drawn center.
     *
     * @param canvas the canvas object
     */
    private void drawBarStroke(Canvas canvas) {
        // Setup RectF for BarStroke
        if (isShowMinValue()) {
            mBarStrokeLeft = (int) (mMinValueRight + mStrokeWidth / 2 + MIN_HORIZONTAL_PADDING);
        } else {
            mBarStrokeLeft = getPaddingLeft() + mStrokeWidth / 2;
        }
        mBarStrokeTop = paddingTop() + (mIndicatorOverflowLength / 2) + mValueHeight
                + mStrokeWidth / 2;
        if (isShowMaxValue()) {
            mBarStrokeRight = (int) (mMaxValueLeft - mStrokeWidth / 2 - MAX_HORIZONTAL_PADDING);
        } else {
            mBarStrokeRight = mViewWidth - getPaddingRight() - mStrokeWidth / 2;
        }
        mBarStrokeBottom = mViewHeight - paddingBottom() - (mIndicatorOverflowLength / 2) -
                mLabelHeight - mStrokeWidth / 2;
        assignRectBounds(mBarStrokeRec, mBarStrokeLeft, mBarStrokeTop, mBarStrokeRight,
                mBarStrokeBottom);

        // Draw
        canvas.drawRect(mBarStrokeRec, mBarStrokePaint);
    }

    /**
     * Determine bounds and draw bar fill
     *
     * @param canvas the canvas object
     */
    private void drawBarFill(Canvas canvas) {
        // Setup RectF for BarFill
        mBarFillLeft = mBarStrokeLeft + mStrokeWidth;
        mBarFillTop = mBarStrokeTop + mStrokeWidth;
        mBarFillRight = mBarStrokeRight - mStrokeWidth;
        mBarFillBottom = mBarStrokeBottom - mStrokeWidth;
        assignRectBounds(mBarFillRec, mBarFillLeft, mBarFillTop, mBarFillRight,
                mBarFillBottom);
        mBarFillPaint.setShader(new LinearGradient(mBarFillLeft, mBarFillTop, mBarFillRight,
                mBarFillBottom, mColorFrom, mColorTo, Shader.TileMode.CLAMP));

        // Draw
        canvas.drawRect(mBarFillRec, mBarFillPaint);
    }

    /**
     * Determine bounds and draw indicator
     *
     * @param canvas the canvas object
     */
    private void drawIndicator(Canvas canvas) {
        // Setup RectF for Indicator
        mIndicatorLeft = (int) (mBarFillLeft + (mValue.getValueToDraw() - mMinValue.getValue())
                * ((mBarFillRight - mBarFillLeft) / (mMaxValue.getValue() - mMinValue.getValue())));
        mIndicatorTop = mBarStrokeTop - mIndicatorOverflowLength / 2 - mStrokeWidth / 2;
        mIndicatorRight = mIndicatorLeft + mIndicatorWidth;
        mIndicatorBottom = mBarStrokeBottom + mIndicatorOverflowLength / 2 + mStrokeWidth / 2;
        assignRectBounds(mIndicatorRec, mIndicatorLeft, mIndicatorTop, mIndicatorRight,
                mIndicatorBottom);

        // Draw
        canvas.drawRect(mIndicatorRec, mIndicatorPaint);
    }

    /**
     * Determine bounds and draw value
     *
     * @param canvas the canvas object
     */
    private void drawValue(Canvas canvas) {
        if (isShowValue()) {
            // Determine width and height of value text
            determineValueWidth(mValue.getTextToDraw(), mValue.getPaint(), isShowValue());
            determineValueHeight(mValue.getPaint(), isShowValue());
            // Determine x and y coordinates for value
            float valueX = (mIndicatorRight + mIndicatorLeft) / 2 - mValueWidth / 2;
            valueX = Math.max(valueX, mBarStrokeLeft);
            if (valueX + mValueWidth > mBarStrokeRight) {
                valueX = valueX - (valueX + mValueWidth - mBarStrokeRight);
            }
            float valueY = mIndicatorTop - 10;

            // Draw
            canvas.drawText(mValue.getTextToDraw(), valueX, valueY, mValue.getPaint());
        }
    }

    /**
     * Determine bounds and draw label
     *
     * @param canvas the canvas object
     */
    private void drawLabel(Canvas canvas) {
        if (isShowLabel()) {
            setLabel(determineLabel(mValue.getValueToDraw(), mMinValue.getValue(),
                    mMaxValue.getValue()));
            // Determine width and height of text
            determineLabelWidth(mLabel, mLabelPaint, isShowLabel());
            determineLabelHeight(mLabelPaint, isShowLabel());
            // Determine x and y coordinates for label
            float labelX = (mIndicatorRight + mIndicatorLeft) / 2 - mLabelWidth / 2;
            labelX = Math.max(labelX, mBarStrokeLeft);
            if (labelX + mLabelWidth > mBarStrokeRight) {
                labelX = labelX - (labelX + mLabelWidth - mBarStrokeRight);
            }
            float labelY = mIndicatorBottom + mLabelHeight - 5;

            // Draw
            canvas.drawText(mLabel, labelX, labelY, mLabelPaint);
        }
    }

    /**
     * Determine top padding considering the extra padding in case user sets 0 padding
     */
    private int paddingTop() {
        int paddingTop = getPaddingTop();
        if (paddingTop <= 0) {
            return EXTRA_PADDING;
        }
        return paddingTop;
    }

    /**
     * Determine bottom padding considering the extra padding in case user sets 0 padding
     */
    private int paddingBottom() {
        int paddingBottom = getPaddingBottom();
        if (paddingBottom <= 0) {
            return EXTRA_PADDING;
        }
        return paddingBottom;
    }

    /**
     * Assign bounds to RectF
     *
     * @param rect   the RectF object
     * @param left   the left bound
     * @param top    the top bound
     * @param right  the right bound
     * @param bottom the bottom bound
     */
    private void assignRectBounds(RectF rect, int left, int top, int right, int bottom) {
        rect.set(left, top, right, bottom);
    }

    /**
     * Calculate the width of the HealthBarView
     *
     * @param measureSpec Measure specifications delivered by the system
     */
    private int measureWidth(int measureSpec) {
        int widthMode = MeasureSpec.getMode(measureSpec);
        int widthSize = MeasureSpec.getSize(measureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            return widthSize;
        } else {
            // Sum up front padding
            int desiredWidth = getPaddingLeft() + getPaddingRight();

            // Sum up stroke width
            desiredWidth += mStrokeWidth * 2;

            // Sum up extra space between stroke and fill
            desiredWidth += mStrokeWidth;

            // Determine width of minValue text
            determineMinValueWidth(mMinValue.getTextToDraw(), mMinValue.getPaint(), isShowValue());
            // Sum up minValue width
            desiredWidth += mMinValueWidth;
            // Add additional padding if minValue is visible
            if (isShowMinValue()) desiredWidth += MIN_HORIZONTAL_PADDING;

            // Determine width of maxValue text
            determineMaxValueWidth(mMaxValue.getTextToDraw(), mMaxValue.getPaint(), isShowValue());
            // Sum up maxValue width
            desiredWidth += mMaxValueWidth;
            // Add additional padding if maxValue is visible
            if (isShowMaxValue()) desiredWidth += MAX_HORIZONTAL_PADDING;

            // Determine width of value text
            determineValueWidth(mValue.getTextToDraw(), mValue.getPaint(), isShowValue());

            // Set label to be drawn
            setLabel(determineLabel(mValue.getValueToDraw(), mMinValue.getValue(),
                    mMaxValue.getValue()));
            // Determine width of label
            int maxLength = 0;
            String longestLabel = null;
            for (String label : mLabels) {
                if (label.length() > maxLength) {
                    maxLength = label.length();
                    longestLabel = label;
                }
            }
            determineLabelWidth(longestLabel, mLabelPaint, isShowLabel());

            // Width of the view should be equal at least to the largest of below components
            desiredWidth = Math.max(desiredWidth, Math.max(mLabelWidth, mValueWidth));

            return resolveSizeAndState(desiredWidth, measureSpec, 0);
        }
    }

    /**
     * Calculate width of the minValue
     */
    private void determineMinValueWidth(String text, Paint paint, boolean isVisible) {
        mMinValueWidth = Util.determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate width of the maxValue
     */
    private void determineMaxValueWidth(String text, Paint paint, boolean isVisible) {
        mMaxValueWidth = Util.determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate width of the value
     */
    private void determineValueWidth(String text, Paint paint, boolean isVisible) {
        mValueWidth = Util.determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate width of the label
     */
    private void determineLabelWidth(String text, Paint paint, boolean isVisible) {
        mLabelWidth = Util.determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate the height of the HealthBarView
     *
     * @param measureSpec Measure specifications delivered by the system
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private int measureHeight(int measureSpec) {
        int heightMode = MeasureSpec.getMode(measureSpec);
        int heightSize = MeasureSpec.getSize(measureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            return heightSize;
        } else {
            // Sum up top & bottom padding
            int desiredHeight = paddingTop() + paddingBottom();

            // Sum up stroke width
            desiredHeight += mStrokeWidth * 2;

            // Sum up minimum required height for bar
            desiredHeight += BAR_FILL_MIN_HEIGHT;

            // Sum up extra space between stroke and fill
            desiredHeight += mStrokeWidth;

            // Determine and sum up height of value
            determineValueHeight(mValue.getPaint(), isShowValue());
            desiredHeight += mValueHeight;

            // Determine and sum up height of label
            determineLabelHeight(mLabelPaint, isShowLabel());
            desiredHeight += mLabelHeight;

            // Sum up the length of indicator that will overflow from top and bottom of the bar
            desiredHeight += mIndicatorOverflowLength; // additional height for indicator

            // Determine and sum up height of minValue
            determineMinValueHeight(mMinValue.getPaint(), isShowValue());

            // Determine and sum up height of minValue
            determineMaxValueHeight(mMaxValue.getPaint(), isShowValue());

            // Width of the view should be equal at least to the largest of below components
            desiredHeight = Math.max(desiredHeight, Math.max(mMinValueHeight, mMaxValueHeight));

            return resolveSizeAndState(desiredHeight, measureSpec, 0);
        }
    }

    /**
     * Calculate height of the minValue
     */
    private void determineMinValueHeight(Paint paint, boolean isVisible) {
        mMinValueHeight = Util.determineTextHeight(paint, isVisible);
    }

    /**
     * Calculate height of the maxValue
     */
    private void determineMaxValueHeight(Paint paint, boolean isVisible) {
        mMaxValueHeight = Util.determineTextHeight(paint, isVisible);
    }

    /**
     * Calculate height of the value
     */
    private void determineValueHeight(Paint paint, boolean isVisible) {
        mValueHeight = Util.determineTextHeight(paint, isVisible);
    }

    /**
     * Calculate height of the label
     */
    private void determineLabelHeight(Paint paint, boolean isVisible) {
        mLabelHeight = Util.determineTextHeight(paint, isVisible);
    }

    /**
     * Determine the label corresponding the value within the range from minValue to maxValue
     *
     * @param value    the actual value
     * @param minValue the starting point of range
     * @param maxValue the end point of range
     * @return the label which corresponds to the given value in the range
     */
    protected @Nullable
    String determineLabel(float value, float minValue, float maxValue) {
        String[] labels = Arrays.copyOf(mLabels, mLabels.length);
        if (minValue > maxValue) Collections.reverse(Arrays.asList(labels));
        float fraction = Math.abs(maxValue - minValue) / labels.length;
        int index = (int) (Math.abs(value - minValue) / fraction);
        index = Math.min(index, labels.length - 1);
        return labels[index];
    }

    //endregion helper
    //----------------------------------

    //----------------------------------
    //region setting painters

    /**
     * Setup all paints.
     * Call only if changes to color or size properties are not visible.
     */
    private void setupPaints() {
        setupBarStrokePaint();
        setupBarFillPaint();
        setupIndicatorPaint();
        setupLabelPaint();
    }

    /**
     * Setup paint object for bar stroke
     * Call only if changes to color or size properties are not visible.
     */
    private void setupBarStrokePaint() {
        mBarStrokePaint.setAntiAlias(true);
        mBarStrokePaint.setStyle(Paint.Style.STROKE);
        mBarStrokePaint.setStrokeWidth(mStrokeWidth);
        mBarStrokePaint.setColor(mStrokeColor);
    }

    /**
     * Setup paint object for bar fill
     * Call only if changes to color or size properties are not visible.
     */
    private void setupBarFillPaint() {
        mBarFillPaint.setAntiAlias(true);
        mBarFillPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Setup paint object for indicator
     * Call only if changes to color or size properties are not visible.
     */
    private void setupIndicatorPaint() {
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(mIndicatorWidth);
    }

    /**
     * Setup paint object for label
     * Call only if changes to color or size properties are not visible.
     */
    private void setupLabelPaint() {
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setTypeface(mLabelFont);
        mLabelPaint.setColor(mLabelTextColor);
        mLabelPaint.setTextSize(mLabelTextSize);
    }

    //endregion setting painter
    //----------------------------------

    //----------------------------------
    //region getter/setter

    /* -------------- MinValue attributes -------------- */

    public float getMinValue() {
        return mMinValue.getValue();
    }

    public void setMinValue(float minValue) {
        mMinValue.setValue(minValue);
        mValue.setValue(minValue);
    }

    public boolean isShowMinValue() {
        return mMinValue.isVisible();
    }

    public void setShowMinValue(boolean showValue) {
        mMinValue.setVisible(showValue);
    }

    public int getMinValueTextColor() {
        return mMinValue.getTextColor();
    }

    public void setMinValueTextColor(int valueTextColor) {
        mMinValue.setTextColor(valueTextColor);
    }

    public int getMinValueTextSize() {
        return mMinValue.getTextSize();
    }

    private void setMinValueTextSize(int valueTextSize) {
        mMinValue.setTextSize(valueTextSize);
    }

    public void setMinValueTextSize(float valueTextSize) {
        setMinValueTextSize(Util.spToPx(valueTextSize));
    }

    public String getMinValueSuffix() {
        return mMinValue.getSuffix();
    }

    public void setMinValueSuffix(String valueSuffix) {
        mMinValue.setSuffix(valueSuffix);
    }

    public Typeface getMinValueFont() {
        return mMinValue.getFont();
    }

    public void setMinValueFont(Typeface valueFont) {
        mMinValue.setFont(valueFont);
    }

    public void setMinValueFont(@FontRes int valueFont) {
        try {
            mMinValue.setFont(ResourcesCompat.getFont(mContext, valueFont));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public DecimalFormat getMinValueDecimalFormat() {
        return mMinValue.getDecimalFormat();
    }

    public void setMinValueDecimalFormat(DecimalFormat valueDecimalFormat) {
        mMinValue.setDecimalFormat(valueDecimalFormat);
    }

    /* -------------- End of minValue attributes -------------- */

    /* -------------- MaxValue attributes -------------- */

    public float getMaxValue() {
        return mMaxValue.getValue();
    }

    public void setMaxValue(float maxValue) {
        mMaxValue.setValue(maxValue);
    }


    public boolean isShowMaxValue() {
        return mMaxValue.isVisible();
    }

    public void setShowMaxValue(boolean showValue) {
        mMaxValue.setVisible(showValue);
    }

    public int getMaxValueTextColor() {
        return mMaxValue.getTextColor();
    }

    public void setMaxValueTextColor(int valueTextColor) {
        mMaxValue.setTextColor(valueTextColor);
    }

    public int getMaxValueTextSize() {
        return mMaxValue.getTextSize();
    }

    private void setMaxValueTextSize(int valueTextSize) {
        mMaxValue.setTextSize(valueTextSize);
    }

    public void setMaxValueTextSize(float valueTextSize) {
        setMaxValueTextSize(Util.spToPx(valueTextSize));
    }

    public String getMaxValueSuffix() {
        return mMaxValue.getSuffix();
    }

    public void setMaxValueSuffix(String valueSuffix) {
        mMaxValue.setSuffix(valueSuffix);
    }

    public Typeface getMaxValueFont() {
        return mMaxValue.getFont();
    }

    public void setMaxValueFont(Typeface valueFont) {
        mMaxValue.setFont(valueFont);
    }

    public void setMaxValueFont(@FontRes int valueFont) {
        try {
            mMaxValue.setFont(ResourcesCompat.getFont(mContext, valueFont));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public DecimalFormat getMaxValueDecimalFormat() {
        return mMaxValue.getDecimalFormat();
    }

    public void setMaxValueDecimalFormat(DecimalFormat valueDecimalFormat) {
        mMaxValue.setDecimalFormat(valueDecimalFormat);
    }

    /* -------------- End of maxValue attributes -------------- */

    /* -------------- Bar stroke attributes -------------- */

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    private void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mBarStrokePaint.setStrokeWidth(mStrokeWidth);
        requestLayout();
    }

    public void setStrokeWidth(float strokeWidth) {
        setStrokeWidth(Util.dpToPx(strokeWidth));
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = Util.colorSetter(mContext, strokeColor);
        mBarStrokePaint.setColor(mStrokeColor);
        invalidate();
    }

    /* -------------- End of bar stroke attributes -------------- */

    /* -------------- Bar fill attributes -------------- */

    public int getColorFrom() {
        return mColorFrom;
    }

    public void setColorFrom(int colorFrom) {
        mColorFrom = Util.colorSetter(mContext, colorFrom);
        invalidate();
    }

    public int getColorTo() {
        return mColorTo;
    }

    public void setColorTo(int colorTo) {
        mColorTo = Util.colorSetter(mContext, colorTo);
        invalidate();
    }

    /* -------------- End of bar fill attributes -------------- */

    /* -------------- Indicator attributes -------------- */

    public int getIndicatorWidth() {
        return mIndicatorWidth;
    }

    private void setIndicatorWidth(int indicatorWidth) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorPaint.setStrokeWidth(mIndicatorWidth);
        requestLayout();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        setIndicatorWidth(Util.dpToPx(indicatorWidth));
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        mIndicatorColor = Util.colorSetter(mContext, indicatorColor);
        mIndicatorPaint.setColor(mIndicatorColor);
        invalidate();
    }

    /* -------------- End of indicator attributes -------------- */

    /* -------------- Value attributes -------------- */

    public float getValue() {
        return mValue.getValue();
    }

    public void setValue(float value) {
        mValue.setValue(value, mMinValue.getValue(), mMaxValue.getValue());
    }

    public boolean isShowValue() {
        return mValue.isVisible();
    }

    public void setShowValue(boolean showValue) {
        mValue.setVisible(showValue);
    }

    public int getValueTextColor() {
        return mValue.getTextColor();
    }

    public void setValueTextColor(int valueTextColor) {
        mValue.setTextColor(valueTextColor);
    }

    public int getValueTextSize() {
        return mValue.getTextSize();
    }

    private void setValueTextSize(int valueTextSize) {
        mValue.setTextSize(valueTextSize);
    }

    public void setValueTextSize(float valueTextSize) {
        setValueTextSize(Util.spToPx(valueTextSize));
    }

    public String getValueSuffix() {
        return mValue.getSuffix();
    }

    public void setValueSuffix(String valueSuffix) {
        mValue.setSuffix(valueSuffix);
    }

    public Typeface getValueFont() {
        return mValue.getFont();
    }

    public void setValueFont(Typeface valueFont) {
        mValue.setFont(valueFont);
    }

    public void setValueFont(@FontRes int valueFont) {
        try {
            mValue.setFont(ResourcesCompat.getFont(mContext, valueFont));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public DecimalFormat getValueDecimalFormat() {
        return mValue.getDecimalFormat();
    }

    public void setValueDecimalFormat(DecimalFormat valueDecimalFormat) {
        mValue.setDecimalFormat(valueDecimalFormat);
    }

    public boolean isAnimated() {
        return mValue.isAnimated();
    }

    public void setAnimated(boolean animated) {
        mValue.setAnimated(animated);
    }

    public long getAnimationDuration() {
        return mValue.getAnimationDuration();
    }

    public void setAnimationDuration(long animationDuration) {
        mValue.setAnimationDuration(animationDuration);
    }

    /* -------------- End of value attributes -------------- */

    /* -------------- Label attributes -------------- */

    public boolean isShowLabel() {
        return mShowLabel;
    }

    public void setShowLabel(boolean showLabel) {
        mShowLabel = showLabel;
        requestLayout();
    }

    public int getLabelTextColor() {
        return mLabelTextColor;
    }

    public void setLabelTextColor(int labelTextColor) {
        mLabelTextColor = Util.colorSetter(mContext, labelTextColor);
        mLabelPaint.setColor(mLabelTextColor);
        invalidate();
    }

    public float getLabelTextSize() {
        return mLabelTextSize;
    }

    private void setLabelTextSize(int labelTextSize) {
        mLabelTextSize = labelTextSize;
        mLabelPaint.setTextSize(mLabelTextSize);
        requestLayout();
    }

    public void setLabelTextSize(float labelTextSize) {
        setLabelTextSize(Util.spToPx(labelTextSize));
    }

    public String[] getLabels() {
        return mLabels;
    }

    public void setLabels(String[] labels) {
        mLabels = labels;
        requestLayout();
    }

    public void setLabels(String labelString, String regex) {
        List<String> labels = Arrays.asList(labelString.split(regex));
        mLabels = (String[]) labels.toArray();
        requestLayout();
    }

    public String getLabel() {
        return mLabel;
    }

    private void setLabel(String label) {
        mLabel = label;
    }

    public Typeface getLabelFont() {
        return mLabelFont;
    }

    public void setLabelFont(Typeface labelFont) {
        mLabelFont = labelFont;
        mLabelPaint.setTypeface(mLabelFont);
        requestLayout();
    }

    public void setLabelFont(@FontRes int labelFont) {
        mLabelFont = ResourcesCompat.getFont(mContext, labelFont);
        mLabelPaint.setTypeface(mLabelFont);
        requestLayout();
    }

    /* -------------- End of label attributes -------------- */

    //endregion getter/setter
    //----------------------------------
}