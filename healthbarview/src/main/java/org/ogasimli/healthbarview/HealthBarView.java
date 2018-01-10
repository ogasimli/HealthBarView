package org.ogasimli.healthbarview;


import org.ogasimli.healthbarview.library.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
 * Created by Orkhan Gasimli on 27.12.2017.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class HealthBarView extends View {

    private static final String LOG_TAG = HealthBarView.class.getSimpleName();

    // Extra padding to avoid cutting of paint
    private static final int EXTRA_PADDING = 10;

    private Context mContext;

    // Label fields
    private boolean mShowLabel = true;
    private int mLabelTextColor = 0xff009688;
    private int mLabelTextSize = 40;
    private Typeface mLabelFont = Typeface.MONOSPACE;
    private String[] mLabels = {"Poor", "Below Average", "Average", "Above Average", "Good",
            "Excellent"};
    private String mLabel;

    // Value fields
    private boolean mShowValue = true;
    private int mValueTextColor = 0xff009688;
    private int mValueTextSize = 40;
    private float mMinValue = 0;
    private float mMaxValue = 100;
    private float mValue = 0;
    private String mValueSuffix = "";
    private Typeface mValueFont = Typeface.MONOSPACE;

    // Indicator fields
    private int mIndicatorWidth = 2;
    private int mIndicatorColor = 0xff009688;

    // Bar
    // Stroke fields
    private int mStrokeWidth = 3;
    private int mStrokeColor = 0xff009688;
    // Fill fields
    private int mColorFrom = 0xffffc200;
    private int mColorTo = 0xff7bfbaf;

    // Formatter object for formatting values
    private DecimalFormat mDecimalFormat = new DecimalFormat("0");

    // Animation
    private boolean mAnimated = false;
    private float mValueToDraw = 0; //for use during an mAnimator
    private long mAnimationDuration = 4000L; //default duration
    ValueAnimator mAnimator = null;

    // Paints
    private Paint mBarFillPaint = new Paint();
    private Paint mBarStrokePaint = new Paint();
    private TextPaint mLabelPaint = new TextPaint();
    private TextPaint mValuePaint = new TextPaint();
    private Paint mIndicatorPaint = new Paint();

    // Rectangles
    private RectF mBarFillRec = new RectF();
    private RectF mBarStrokeRec = new RectF();
    private RectF mIndicatorRec = new RectF();

    private String mValueText;
    private int mLabelHeight;
    private int mLabelWidth;
    private int mValueHeight;
    private int mValueWidth;
    private int mIndicatorOverflowLength = 40;
    private int mViewWidth;
    private int mViewHeight;
    private int mBarFillLeft;
    private int mBarFillTop;
    private int mBarFillRight;
    private int mBarFillBottom;
    private int mBarStrokeLeft;
    private int mBarStrokeBottom;
    private int mBarStrokeTop;
    private int mBarStrokeRight;
    private int mIndicatorLeft;
    private int mIndicatorTop;
    private int mIndicatorRight;
    private int mIndicatorBottom;

    /**
     * The constructor for the HealthBarView
     *
     * @param context The context.
     */
    public HealthBarView(Context context) {
        super(context);
        mContext = context;
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
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.HealthBarView));
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

        if (a.hasValue(R.styleable.HealthBarView_hbv_showValue)) {
            setShowValue(a.getBoolean(R.styleable.HealthBarView_hbv_showValue, mShowValue));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueTextColor)) {
            setValueTextColor(a.getInt(R.styleable.HealthBarView_hbv_valueTextColor, mValueTextColor));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueTextSize)) {
            setValueTextSize(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_valueTextSize,
                    mValueTextSize));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_minValue)) {
            setMinValue(a.getFloat(R.styleable.HealthBarView_hbv_minValue, mMinValue));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_maxValue)) {
            setMaxValue(a.getFloat(R.styleable.HealthBarView_hbv_maxValue, mMaxValue));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_animated)) {
            setAnimated(a.getBoolean(R.styleable.HealthBarView_hbv_animated, mAnimated));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_animationDuration)) {
            setAnimationDuration(a.getInt(R.styleable.HealthBarView_hbv_animationDuration,
                    (int) mAnimationDuration));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_value)) {
            setValue(a.getFloat(R.styleable.HealthBarView_hbv_value, mValue));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueSuffix)) {
            setValueSuffix(a.getString(R.styleable.HealthBarView_hbv_valueSuffix));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_valueFont)) {
            setValueFont(a.getResourceId(R.styleable.HealthBarView_hbv_valueFont, -1));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorWidth)) {
            setIndicatorWidth(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_indicatorWidth,
                    mIndicatorWidth));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_indicatorColor)) {
            setIndicatorColor(a.getInt(R.styleable.HealthBarView_hbv_indicatorColor, mIndicatorColor));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_strokeWidth)) {
            setStrokeWidth(a.getDimensionPixelSize(R.styleable.HealthBarView_hbv_strokeWidth,
                    mStrokeWidth));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_strokeColor)) {
            setStrokeColor(a.getInt(R.styleable.HealthBarView_hbv_strokeColor, mStrokeColor));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorFrom)) {
            setColorFrom(a.getInt(R.styleable.HealthBarView_hbv_colorFrom, mColorFrom));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_colorTo)) {
            setColorTo(a.getInt(R.styleable.HealthBarView_hbv_colorTo, mColorTo));
        }

        if (a.hasValue(R.styleable.HealthBarView_hbv_decimalFormat)) {
            try {
                String pattern = a.getString(R.styleable.HealthBarView_hbv_decimalFormat);
                setDecimalFormat(new DecimalFormat(pattern));
            } catch (Exception exception) {
                Log.w(LOG_TAG, exception.getMessage());
            }
        }

        // Recycle
        a.recycle();
    }

    /**
     * Determine bounds and draw bar stroke
     * P.S. Consider that stroke will be drawn center.
     *
     * @param canvas the attributes canvas object
     */
    private void drawBarStroke(Canvas canvas) {
        // Setup RectF for BarStroke
        mBarStrokeLeft = getPaddingLeft() + mStrokeWidth / 2;
        mBarStrokeTop = paddingTop() + (mIndicatorOverflowLength / 2) + mValueHeight
                + mStrokeWidth / 2;
        mBarStrokeRight = mViewWidth - getPaddingRight() - mStrokeWidth / 2;
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
     * @param canvas the attributes canvas object
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
     * @param canvas the attributes canvas object
     */
    private void drawIndicator(Canvas canvas) {
        // Setup RectF for Indicator
        mIndicatorLeft = (int) (mBarFillLeft + (mValueToDraw - mMinValue)
                * ((mBarFillRight - mBarFillLeft) / (mMaxValue - mMinValue)));
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
     * @param canvas the attributes canvas object
     */
    private void drawValue(Canvas canvas) {
        if (isShowValue()) {
            setValueText(formatValueText(mValueToDraw, mValueSuffix, mDecimalFormat));
            // Determine width and height of value text
            determineValueWidth(mValueText, mValuePaint, isShowValue());
            determineValueHeight(mValuePaint, isShowValue());
            // Determine x and y coordinates for value
            float valueX = (mIndicatorRight + mIndicatorLeft) / 2 - mValueWidth / 2;
            valueX = Math.max(valueX, mBarStrokeLeft);
            if (valueX + mValueWidth > mBarStrokeRight) {
                valueX = valueX - (valueX + mValueWidth - mBarStrokeRight);
            }
            float valueY = mIndicatorTop - 10;

            // Draw
            canvas.drawText(mValueText, valueX, valueY, mValuePaint);
        }
    }

    /**
     * Determine bounds and draw label
     *
     * @param canvas the attributes canvas object
     */
    private void drawLabel(Canvas canvas) {
        if (isShowLabel()) {
            setLabel(determineLabel(mValueToDraw, mMinValue, mMaxValue));
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

            // Set value to be drawn
            setValueText(formatValueText(mValueToDraw, mValueSuffix, mDecimalFormat));
            // Determine width of value text
            determineValueWidth(mValueText, mValuePaint, isShowValue());

            // Set label to be drawn
            setLabel(determineLabel(mValueToDraw, mMinValue, mMaxValue));
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
     * Calculate width of the value
     */
    private void determineValueWidth(String text, Paint paint, boolean isVisible) {
        mValueWidth = determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate width of the label
     */
    private void determineLabelWidth(String text, Paint paint, boolean isVisible) {
        mLabelWidth = determineTextWidth(text, paint, isVisible);
    }

    /**
     * Calculate width of the text
     *
     * @param text  the string to be drawn
     * @param paint the paint object that will draw the text
     */
    private int determineTextWidth(String text, Paint paint, boolean isVisible) {
        if (isVisible) {
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            return (int) ((paint.measureText(text) + bounds.width()) / 2);
        }
        return 0;
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
            int barFillMinHeight = 70;
            desiredHeight += barFillMinHeight;

            // Sum up extra space between stroke and fill
            desiredHeight += mStrokeWidth;

            // Determine and sum up height of value
            determineValueHeight(mValuePaint, isShowValue());
            desiredHeight += mValueHeight;

            // Determine and sum up height of label
            determineLabelHeight(mLabelPaint, isShowLabel());
            desiredHeight += mLabelHeight;

            // Sum up the length of indicator that will overflow from top and bottom of the bar
            desiredHeight += mIndicatorOverflowLength; // additional height for indicator

            return resolveSizeAndState(desiredHeight, measureSpec, 0);
        }
    }

    /**
     * Calculate heigh of the value
     */
    private void determineValueHeight(Paint paint, boolean isVisible) {
        mValueHeight = determineTextHeight(paint, isVisible);
    }

    /**
     * Calculate height of the label
     */
    private void determineLabelHeight(Paint paint, boolean isVisible) {
        mLabelHeight = determineTextHeight(paint, isVisible);
    }

    /**
     * Calculate height of the text
     *
     * @param paint the paint object that will draw the text
     */
    private int determineTextHeight(Paint paint, boolean isVisible) {
        if (isVisible) {
            // Get height from font metrics
            Paint.FontMetrics fm = paint.getFontMetrics();
            return (int) (fm.descent - fm.ascent);
        }
        return 0;
    }

    /**
     * Determine if value is between v1 and v2
     *
     * @param value the argument which should be checked if it is between v1 and v2
     * @param v1    the first number
     * @param v2    the second number
     */
    private boolean isBetween(float value, float v1, float v2) {
        // Determine the minimum of these two numbers
        float min = Math.min(v1, v2);
        // Determine the maximum of these two numbers
        float max = Math.max(v1, v2);
        return value >= min && value <= max;
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
        for (int i = 1; i <= labels.length; i++) {
            if (value <= fraction * i) {
                return labels[i - 1];
            }
        }
        return labels[0];
    }

    protected @NonNull
    String formatValueText(float value, String suffix, DecimalFormat decimalFormat) {
        String result = decimalFormat.format(value);
        if (suffix != null) result += suffix;
        return result;
    }

    /**
     * Extract color int from color resource id and return it
     *
     * @param color the int value representing either color int, or color resource id
     * @return the color
     */
    private int colorSetter(int color) {
        try {
            return ContextCompat.getColor(mContext, color);
        } catch (Resources.NotFoundException e) {
            Log.d(LOG_TAG, "Color resource not found.");
            return color;
        }
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
        setupBarFillPaint();
        setupBarStrokePaint();
        setupLabelPaint();
        setupValuePaint();
        setupIndicatorPaint();
    }

    private void setupBarFillPaint() {
        mBarFillPaint.setAntiAlias(true);
        mBarFillPaint.setStyle(Paint.Style.FILL);
    }

    private void setupBarStrokePaint() {
        mBarStrokePaint.setAntiAlias(true);
        mBarStrokePaint.setStyle(Paint.Style.STROKE);
        mBarStrokePaint.setStrokeWidth(mStrokeWidth);
        mBarStrokePaint.setColor(mStrokeColor);
    }

    private void setupLabelPaint() {
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setTypeface(mLabelFont);
        mLabelPaint.setColor(mLabelTextColor);
        mLabelPaint.setTextSize(mLabelTextSize);
    }

    private void setupValuePaint() {
        mValuePaint.setAntiAlias(true);
        mValuePaint.setTypeface(mValueFont);
        mValuePaint.setColor(mValueTextColor);
        mValuePaint.setTextSize(mValueTextSize);
    }

    private void setupIndicatorPaint() {
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(mIndicatorWidth);
    }

    //endregion setting painter
    //----------------------------------

    //----------------------------------
    //region getter/setter

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
        mLabelTextColor = colorSetter(labelTextColor);
        mLabelPaint.setColor(mLabelTextColor);
        invalidate();
    }

    public float getLabelTextSize() {
        return mLabelTextSize;
    }

    public void setLabelTextSize(int labelTextSize) {
        mLabelTextSize = labelTextSize;
        mLabelPaint.setTextSize(mLabelTextSize);
        requestLayout();
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

    public boolean isShowValue() {
        return mShowValue;
    }

    public void setShowValue(boolean showValue) {
        mShowValue = showValue;
        requestLayout();
    }

    public int getValueTextColor() {
        return mValueTextColor;
    }

    public void setValueTextColor(int valueTextColor) {
        mValueTextColor = colorSetter(valueTextColor);
        mValuePaint.setColor(mValueTextColor);
        invalidate();
    }

    public int getValueTextSize() {
        return mValueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        mValueTextSize = valueTextSize;
        mValuePaint.setTextSize(mValueTextSize);
        requestLayout();
    }

    public float getMinValue() {
        return mMinValue;
    }

    public void setMinValue(float minValue) {
        mMinValue = minValue;
        invalidate();
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
        invalidate();
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        float previousValue = mValue;
        if (isBetween(value, mMinValue, mMaxValue)) {
            mValue = value;
        } else {
            mValue = mMinValue;
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        if (mAnimated) {
            mAnimator = ValueAnimator.ofFloat(previousValue, mValue);
            //mAnimationDuration specifies how long it should take to animate the entire graph, so the
            //actual value to use depends on how much the value needs to change
            float changeInValue = Math.abs(mValue - previousValue);
            long durationToUse = (long) (mAnimationDuration * (changeInValue
                    / Math.max(mMinValue, mMaxValue)));
            mAnimator.setDuration(durationToUse);

            mAnimator.addUpdateListener(valueAnimator -> {
                mValueToDraw = (float) valueAnimator.getAnimatedValue();
                invalidate();
            });

            mAnimator.start();
        } else {
            mValueToDraw = mValue;
        }
        invalidate();
    }

    public String getValueSuffix() {
        return mValueSuffix;
    }

    public void setValueSuffix(String valueSuffix) {
        if (valueSuffix != null) {
            mValueSuffix = valueSuffix;
            requestLayout();
        }
    }

    public Typeface getValueFont() {
        return mValueFont;
    }

    public void setValueFont(Typeface valueFont) {
        mValueFont = valueFont;
        mValuePaint.setTypeface(mValueFont);
        requestLayout();
    }

    public void setValueFont(@FontRes int valueFont) {
        mValueFont = ResourcesCompat.getFont(mContext, valueFont);
        mValuePaint.setTypeface(mValueFont);
        requestLayout();
    }

    public int getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorPaint.setStrokeWidth(mIndicatorWidth);
        requestLayout();
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        mIndicatorColor = colorSetter(indicatorColor);
        mIndicatorPaint.setColor(mIndicatorColor);
        invalidate();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mBarStrokePaint.setStrokeWidth(mStrokeWidth);
        requestLayout();
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = colorSetter(strokeColor);
        mBarStrokePaint.setColor(mStrokeColor);
        invalidate();
    }

    public int getColorFrom() {
        return mColorFrom;
    }

    public void setColorFrom(int colorFrom) {
        mColorFrom = colorSetter(colorFrom);
        invalidate();
    }

    public int getColorTo() {
        return mColorTo;
    }

    public void setColorTo(int colorTo) {
        mColorTo = colorSetter(colorTo);
        invalidate();
    }

    public DecimalFormat getDecimalFormat() {
        return mDecimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        if (decimalFormat != null) {
            mDecimalFormat = decimalFormat;
            requestLayout();
        }
    }

    private void setValueText(String valueText) {
        mValueText = valueText;
    }

    public boolean isAnimated() {
        return mAnimated;
    }

    public void setAnimated(boolean animated) {
        this.mAnimated = animated;
    }

    public long getAnimationDuration() {
        return mAnimationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    //endregion getter/setter
    //----------------------------------
}
