package org.ogasimli.healthbarview;


import org.ogasimli.healthbarview.library.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Custom health bar like view
 *
 * @author Orkhan Gasimli on 27.12.2017.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public class HealthBarView extends View {

    private static final String LOG_TAG = HealthBarView.class.getSimpleName();

    // Context
    private Context mContext;

    // Extra padding to avoid overflow of text
    private static final int EXTRA_PADDING = Util.dpToPx(2);

    // Default bar height (if used wrap_content)
    private static final int BAR_FILL_DEFAULT_HEIGHT = Util.dpToPx(25);

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
    private final Stroke mStroke;
    // Fill fields
    private final Fill mFill;

    // Indicator fields
    private final Indicator mIndicator;

    // Value field
    private final Value mValue;

    // Label fields
    private final Label mLabel;

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

        // Init elements
        mMinValue = new MinValue(this, context);
        mMaxValue = new MaxValue(this, context);
        mStroke = new Stroke(this, context);
        mFill = new Fill(this, context);
        mIndicator = new Indicator(this, context);
        mValue = new Value(this, context);
        mLabel = new Label(this, context);

        // Init view
        init(context, null);
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

        // Init elements
        mMinValue = new MinValue(this, context);
        mMaxValue = new MaxValue(this, context);
        mStroke = new Stroke(this, context);
        mFill = new Fill(this, context);
        mIndicator = new Indicator(this, context);
        mValue = new Value(this, context);
        mLabel = new Label(this, context);

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
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        if (attrs != null) {
            parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.HealthBarView));
        }
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
                    Stroke.DEFAULT_WIDTH));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_strokeColor)) {
            setStrokeColor(a.getInt(R.styleable.HealthBarView_hbv_strokeColor,
                    Stroke.DEFAULT_COLOR));
        }

        /* -------------- End of bar stroke attributes -------------- */

        /* -------------- Bar fill attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorFrom)) {
            setStartColor(a.getInt(R.styleable.HealthBarView_hbv_colorFrom,
                    Fill.DEFAULT_START_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorTo)) {
            setEndColor(a.getInt(R.styleable.HealthBarView_hbv_colorTo, Fill.DEFAULT_END_COLOR));
        }

        /* -------------- End of bar fill attributes -------------- */

        /* -------------- Indicator attributes -------------- */

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorWidth)) {
            setIndicatorWidth(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_indicatorWidth,
                    Indicator.DEFAULT_WIDTH));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorColor)) {
            setIndicatorColor(a.getInt(R.styleable.HealthBarView_hbv_indicatorColor,
                    Indicator.DEFAULT_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorTopOverflow)) {
            setIndicatorTopOverflow(a.getDimensionPixelSize
                    (R.styleable.HealthBarView_hbv_indicatorTopOverflow,
                            Indicator.DEFAULT_TOP_OVERFLOW));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorBottomOverflow)) {
            setIndicatorBottomOverflow(a.getDimensionPixelSize
                    (R.styleable.HealthBarView_hbv_indicatorBottomOverflow,
                            Indicator.DEFAULT_BOTTOM_OVERFLOW));
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
            setShowLabel(a.getBoolean(R.styleable.HealthBarView_hbv_showLabel,
                    Label.DEFAULT_VISIBILITY));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labelTextColor)) {
            setLabelTextColor(a.getInt(R.styleable.HealthBarView_hbv_labelTextColor,
                    Label.DEFAULT_TEXT_COLOR));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_labelTextSize)) {
            setLabelTextSize(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_labelTextSize,
                    Label.DEFAULT_TEXT_SIZE));
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
                    mIndicator.getTopOverflow() - mLabelHeight - mStroke.getWidth() / 2) -
                    (((mViewHeight - paddingBottom() - mIndicator.getTopOverflow() -
                            mLabelHeight - mStroke.getWidth() / 2) - (paddingTop() +
                            mIndicator.getBottomOverflow() + mValueHeight +
                            mStroke.getWidth() / 2)) / 2) + (mMinValueHeight / 2.5));
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
                    mIndicator.getTopOverflow() - mLabelHeight - mStroke.getWidth() / 2) -
                    (((mViewHeight - paddingBottom() - mIndicator.getTopOverflow() -
                            mLabelHeight - mStroke.getWidth() / 2) - (paddingTop() +
                            mIndicator.getBottomOverflow() + mValueHeight +
                            mStroke.getWidth() / 2)) / 2) + (mMinValueHeight / 2.5));
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
            mBarStrokeLeft = (int) (mMinValueRight + mStroke.getWidth() / 2 + MIN_HORIZONTAL_PADDING);
        } else {
            mBarStrokeLeft = getPaddingLeft() + mStroke.getWidth() / 2;
        }
        mBarStrokeTop = paddingTop() + mIndicator.getTopOverflow() + mValueHeight
                + mStroke.getWidth() / 2;
        if (isShowMaxValue()) {
            mBarStrokeRight = (int) (mMaxValueLeft - mStroke.getWidth() / 2 - MAX_HORIZONTAL_PADDING);
        } else {
            mBarStrokeRight = mViewWidth - getPaddingRight() - mStroke.getWidth() / 2;
        }
        mBarStrokeBottom = mViewHeight - paddingBottom() - mIndicator.getBottomOverflow() -
                mLabelHeight - mStroke.getWidth() / 2;
        assignRectBounds(mBarStrokeRec, mBarStrokeLeft, mBarStrokeTop, mBarStrokeRight,
                mBarStrokeBottom);

        // Draw
        canvas.drawRect(mBarStrokeRec, mStroke.getPaint());
    }

    /**
     * Determine bounds and draw bar fill
     *
     * @param canvas the canvas object
     */
    private void drawBarFill(Canvas canvas) {
        // Setup RectF for BarFill
        mBarFillLeft = mBarStrokeLeft + mStroke.getWidth();
        mBarFillTop = mBarStrokeTop + mStroke.getWidth();
        mBarFillRight = mBarStrokeRight - mStroke.getWidth();
        mBarFillBottom = mBarStrokeBottom - mStroke.getWidth();
        assignRectBounds(mBarFillRec, mBarFillLeft, mBarFillTop, mBarFillRight,
                mBarFillBottom);
        mFill.setPainShader(mBarFillLeft, mBarFillTop, mBarFillRight, mBarFillBottom);

        // Draw
        canvas.drawRect(mBarFillRec, mFill.getPaint());
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
        mIndicatorTop = mBarStrokeTop - mIndicator.getTopOverflow() - mStroke.getWidth() / 2;
        mIndicatorRight = mIndicatorLeft + mIndicator.getWidth();
        mIndicatorBottom = mBarStrokeBottom + mIndicator.getBottomOverflow() + mStroke.getWidth() / 2;
        assignRectBounds(mIndicatorRec, mIndicatorLeft, mIndicatorTop, mIndicatorRight,
                mIndicatorBottom);

        // Draw
        canvas.drawRect(mIndicatorRec, mIndicator.getPaint());
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
            mLabel.setLabelToDraw(mValue.getValueToDraw(), mMinValue.getValue(),
                    mMaxValue.getValue());
            // Determine width and height of text
            determineLabelWidth(mLabel.getLabelToDraw(), mLabel.getPaint(), isShowLabel());
            determineLabelHeight(mLabel.getPaint(), isShowLabel());
            // Determine x and y coordinates for label
            float labelX = (mIndicatorRight + mIndicatorLeft) / 2 - mLabelWidth / 2;
            labelX = Math.max(labelX, mBarStrokeLeft);
            if (labelX + mLabelWidth > mBarStrokeRight) {
                labelX = labelX - (labelX + mLabelWidth - mBarStrokeRight);
            }
            float labelY = mIndicatorBottom + mLabelHeight - 5;

            // Draw
            canvas.drawText(mLabel.getLabelToDraw(), labelX, labelY, mLabel.getPaint());
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
            desiredWidth += mStroke.getWidth() * 2;

            // Sum up extra space between stroke and fill
            desiredWidth += mStroke.getWidth();

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
            mLabel.setLabelToDraw(mValue.getValueToDraw(), mMinValue.getValue(),
                    mMaxValue.getValue());
            // Determine width of label
            int maxLength = 0;
            String longestLabel = null;
            for (String label : mLabel.getLabels()) {
                if (label.length() > maxLength) {
                    maxLength = label.length();
                    longestLabel = label;
                }
            }
            determineLabelWidth(longestLabel, mLabel.getPaint(), isShowLabel());

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
            desiredHeight += mStroke.getWidth() * 2;

            // Sum up minimum required height for bar
            desiredHeight += BAR_FILL_DEFAULT_HEIGHT;

            // Sum up extra space between stroke and fill
            desiredHeight += mStroke.getWidth();

            // Determine and sum up height of value
            determineValueHeight(mValue.getPaint(), isShowValue());
            desiredHeight += mValueHeight;

            // Determine and sum up height of label
            determineLabelHeight(mLabel.getPaint(), isShowLabel());
            desiredHeight += mLabelHeight;

            // Sum up the length of indicator that will overflow from top and bottom of the bar
            desiredHeight += mIndicator.getTopOverflow(); // additional height for indicator
            desiredHeight += mIndicator.getBottomOverflow(); // additional height for indicator

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

    //endregion helper
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
        mMinValue.setTextSize(valueTextSize);
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
        mMinValue.setFont(valueFont);
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
        mMaxValue.setTextSize(valueTextSize);
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
        mMaxValue.setFont(valueFont);
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
        return mStroke.getWidth();
    }

    private void setStrokeWidth(int strokeWidth) {
        mStroke.setWidth(strokeWidth);
    }

    public void setStrokeWidth(float strokeWidth) {
        setStrokeWidth(Util.dpToPx(strokeWidth));
    }

    public int getStrokeColor() {
        return mStroke.getColor();
    }

    public void setStrokeColor(int strokeColor) {
        mStroke.setColor(strokeColor);
    }

    /* -------------- End of bar stroke attributes -------------- */

    /* -------------- Bar fill attributes -------------- */

    @Deprecated
    public int getColorFrom() {
        return mFill.getStartColor();
    }

    @Deprecated
    public void setColorFrom(int colorFrom) {
        mFill.setStartColor(colorFrom);
    }

    @Deprecated
    public int getColorTo() {
        return mFill.getEndColor();
    }

    @Deprecated
    public void setColorTo(int colorTo) {
        mFill.setEndColor(colorTo);
    }

    public int getStartColor() {
        return mFill.getStartColor();
    }

    public void setStartColor(int startColor) {
        mFill.setStartColor(startColor);
    }

    public int getEndColor() {
        return mFill.getEndColor();
    }

    public void setEndColor(int endColor) {
        mFill.setEndColor(endColor);
    }

    /* -------------- End of bar fill attributes -------------- */

    /* -------------- Indicator attributes -------------- */

    public int getIndicatorWidth() {
        return mIndicator.getWidth();
    }

    private void setIndicatorWidth(int indicatorWidth) {
        mIndicator.setWidth(indicatorWidth);
    }

    public void setIndicatorWidth(float indicatorWidth) {
        mIndicator.setWidth(indicatorWidth);
    }

    public int getIndicatorColor() {
        return mIndicator.getColor();
    }

    public void setIndicatorColor(int indicatorColor) {
        mIndicator.setColor(indicatorColor);
    }

    public int getIndicatorTopOverflow() {
        return mIndicator.getTopOverflow();
    }

    public void setIndicatorTopOverflow(int top) {
        mIndicator.setTopOverflow(top);
    }

    public int getIndicatorBottomOverflow() {
        return mIndicator.getBottomOverflow();
    }

    public void setIndicatorBottomOverflow(int bottom) {
        mIndicator.setBottomOverflow(bottom);
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
        mValue.setTextSize(valueTextSize);
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
        mValue.setFont(valueFont);
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
        return mLabel.isVisible();
    }

    public void setShowLabel(boolean showLabel) {
        mLabel.setVisible(showLabel);
    }

    public int getLabelTextColor() {
        return mLabel.getTextColor();
    }

    public void setLabelTextColor(int labelTextColor) {
        mLabel.setTextColor(labelTextColor);
    }

    public float getLabelTextSize() {
        return mLabel.getTextSize();
    }

    private void setLabelTextSize(int labelTextSize) {
        mLabel.setTextSize(labelTextSize);
    }

    public void setLabelTextSize(float labelTextSize) {
        mLabel.setTextSize(labelTextSize);
    }

    public String[] getLabels() {
        return mLabel.getLabels();
    }

    public void setLabels(String[] labels) {
        mLabel.setLabels(labels);
    }

    public void setLabels(String labelsString, String regex) {
        mLabel.setLabels(labelsString, regex);
    }

    public String getLabel() {
        return mLabel.getLabelToDraw();
    }

    private void setLabel(String label) {
        mLabel.setLabelToDraw(label);
    }

    public Typeface getLabelFont() {
        return mLabel.getFont();
    }

    public void setLabelFont(Typeface labelFont) {
        mLabel.setFont(labelFont);
    }

    public void setLabelFont(@FontRes int labelFont) {
        mLabel.setFont(labelFont);
    }

    /* -------------- End of label attributes -------------- */

    //endregion getter/setter
    //----------------------------------
}