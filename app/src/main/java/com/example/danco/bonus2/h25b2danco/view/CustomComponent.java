package com.example.danco.bonus2.h25b2danco.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.danco.bonus2.R;


/**
 * Created by costd035 on 1/31/15.
 */
public class CustomComponent extends ImageView {
    private static final String TAG = CustomComponent.class.getSimpleName();

    private int leftColor;
    private int rightColor;
    private int paintSize;
    private int rightColorPercentage = 50;
    private int arcSweepSize;
    private String scaleType;

    RectF oval = new RectF();

    private Paint paint;
    private int centerWidth;
    private int centerHeight;
    private int startHorizontal;
    private int endHorizontal;
    private int topVertical;
    private int bottomVertical;
    private int height;
    private int width;

    public CustomComponent(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomAttributes, defStyle, 0);

        rightColor = a.getColor(
                R.styleable.CustomAttributes_rightColor,
                rightColor);

        leftColor = a.getColor(
                R.styleable.CustomAttributes_leftColor,
                leftColor);

        paintSize = a.getDimensionPixelSize(
                R.styleable.CustomAttributes_paintSize,
                paintSize);

        rightColorPercentage = a.getInt(R.styleable.CustomAttributes_rightColorPercentage, 50);
        arcSweepSize = convertToDegrees(rightColorPercentage);

        scaleType = a.getString(R.styleable.CustomAttributes_android_scaleType);

        Log.v(TAG, "arcSweepDegrees=" + arcSweepSize);

        a.recycle();

        // Set up a default TextPaint object
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // only painting lines so indicate that so lines drawn correctly.
        paint.setStyle(Paint.Style.STROKE);

        // Update TextPaint and text measurements from attributes
        invalidatePaintAndMeasurements(false);
    }


    public void setRightColorPercentage(int rightColorPercentage) {
        this.rightColorPercentage = rightColorPercentage;
        arcSweepSize = convertToDegrees(rightColorPercentage);
        invalidatePaintAndMeasurements(true);
    }


    private int convertToDegrees(double arcSweepSize) {
        return (int) (360 * (arcSweepSize / 100));
    }


    private void invalidatePaintAndMeasurements(boolean invalidateView) {
        paint.setStrokeWidth(paintSize);

        centerHeight = height / 2;
        centerWidth = width / 2;

        int scale = Math.min(width, height);
        int horizontalOffset = 0;
        int verticalOffset = 0;

        // handling the scaleType this way is ugly, but I couldn't get
        //  ScaleType.valueOf(scaleType) to work...
        if (scaleType != null && scaleType.equals("7")) {
            if (width > height) {
                horizontalOffset = (width - height) / 2;
            } else if (height > width) {
                verticalOffset = (height - width) / 2;
            }
        }
        startHorizontal = ViewCompat.getPaddingStart(this) + horizontalOffset;
        endHorizontal = scale - ViewCompat.getPaddingEnd(this) + horizontalOffset;
        topVertical = getPaddingTop() + verticalOffset;
        bottomVertical = scale - getPaddingBottom() + verticalOffset;

        oval = new RectF(startHorizontal + paintSize/2, topVertical + paintSize/2,
                endHorizontal - paintSize/2, bottomVertical - paintSize/2);

        if (invalidateView) {
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = h;
        width = w;

        invalidatePaintAndMeasurements(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(-90, centerWidth, centerHeight);
        paint.setColor(rightColor);
        canvas.drawArc(oval, 0, arcSweepSize, false, paint);
        paint.setColor(leftColor);
        canvas.drawArc(oval, arcSweepSize, 360 - arcSweepSize, false, paint);
        canvas.restore();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getLeftColor() {
        return leftColor;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getRightColor() {
        return rightColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setLeftColor(int exampleColor) {
        leftColor = exampleColor;
        invalidatePaintAndMeasurements(true);
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setRightColor(int exampleColor) {
        rightColor = exampleColor;
        invalidatePaintAndMeasurements(true);
    }

    public int getPaintSize() {
        return paintSize;
    }

    public void setPaintSize(int paintSize) {
        this.paintSize = paintSize;
        invalidatePaintAndMeasurements(true);
    }

}
