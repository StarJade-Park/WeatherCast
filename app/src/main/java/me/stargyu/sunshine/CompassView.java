package me.stargyu.sunshine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CompassView extends View {
    private final static String LOG_TAG = CompassView.class.getSimpleName();

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }

    private void initCompassView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 부드럽게(안티먹임)
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hSpecMode == MeasureSpec.EXACTLY) {
            mHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            Log.v(LOG_TAG, "onMeasure: hSpecMode" + hSpecMode);
        }

        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wSpecMode == MeasureSpec.EXACTLY) {
            mWidth = wSpecSize;
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            Log.v(LOG_TAG, "onMeasure: wSpecMode" + wSpecMode);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = Math.min(mWidth, mHeight) / 2;

        // center circle in compass
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2, mHeight/2, 20, mPaint);

        // border of compass
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(mWidth/2, mHeight/2, radius-5, mPaint);

        // needle of compass
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        canvas.drawLine(mWidth/2, mHeight/2, mWidth/2, 5, mPaint);

        invalidate();
    }
}

