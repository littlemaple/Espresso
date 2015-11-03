package com.rainbow.blue.espresso.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by blue on 2015/10/30.
 */
public class GradientView extends View {
    private Paint paint = new Paint();
    private Paint textPaint = new Paint();
    private Paint triPaint = new Paint();
    private Path path = new Path();
    private LinearGradient linearGradient;
    private PointF pointF = new PointF();
    private int[] colors = new int[]{Color.parseColor("#1ba7c1"), Color.parseColor("#2ccddc"), Color.parseColor("#60d5af"), Color.parseColor("#7bc36d"), Color.parseColor("#c3c23b"), Color.parseColor("#ffb300"), Color.parseColor("#ff1801")};
    private float[] positions = new float[]{0, 1.0f / 6, 2.0f / 6, 3.0f / 6, 4.0f / 6, 5.0f / 6, 1};
    private PathEffect pathEffect = new DashPathEffect(new float[]{8, 8, 8, 8}, 8);
    private float curValue;

    private float min, max, norMin, norMax;

    public GradientView(Context context) {
        super(context);
        initPaintComponent();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintComponent();
    }

    public void setValue(float value) {
        curValue = value;
        if (value >= norMin && value <= norMax) {
            pointF.x = getMeasuredWidth() / 3 + (value - norMin) / (norMax - norMin) * getMeasuredWidth() / 3;
            pointF.y = getMeasuredHeight() / 3 - 10;
        } else if (value > norMax) {
            pointF.x = value >= max ? getMeasuredWidth() : getMeasuredWidth() * 2 / 3 + (value - norMax) / (max - norMax) * getMeasuredWidth() / 3;
            pointF.y = getMeasuredHeight() / 3 - 10;
        } else {
            pointF.x = value <= min ? 0 : (value - min) / (norMin - min) * getMeasuredWidth() / 3;
            pointF.y = getMeasuredHeight() / 3 - 10;
        }
        postInvalidate();
    }

    public void initData(float min, float norMin, float norMax, float max) {
        norMin = norMin;
        norMax = norMax;
        min = min;
        max = max;
    }

    private void initPaintComponent() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#7b7b7b"));
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setTextSize(40);
        textPaint.setColor(Color.parseColor("#7b7b7b"));
        triPaint = new Paint();
        triPaint.setStrokeWidth(3);
        triPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        triPaint.setColor(Color.parseColor("#7b7b7b"));
        triPaint.setAntiAlias(true);
        norMin = 1.9f;
        norMax = 2.5f;
        min = 0;
        max = 6;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (linearGradient == null)
            linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, colors, positions, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg:
        {
            paint.setShader(linearGradient);
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            paint.setStyle(Paint.Style.FILL);
            path.moveTo(20, getMeasuredHeight() / 3);
            path.lineTo(getMeasuredWidth() - 20, getMeasuredHeight() / 3);
            path.quadTo(getMeasuredWidth(), getMeasuredHeight() / 3, getMeasuredWidth(), getMeasuredHeight() / 3 + 20);
            path.lineTo(getMeasuredWidth(), getMeasuredHeight() * 2 / 3 - 20);
            path.quadTo(getMeasuredWidth(), getMeasuredHeight() * 2 / 3, getMeasuredWidth() - 20, getMeasuredHeight() * 2 / 3);
            path.lineTo(20, getMeasuredHeight() * 2 / 3);
            path.quadTo(0, getMeasuredHeight() * 2 / 3, 0, getMeasuredHeight() * 2 / 3 - 20);
            path.lineTo(0, getMeasuredHeight() / 3 + 20);
            path.quadTo(0, getMeasuredHeight() / 3, 20, getMeasuredHeight() / 3);
            canvas.drawPath(path, paint);
        }
        drawLeft:
        {
            paint.setShader(null);
            paint.setPathEffect(pathEffect);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            path.reset();
            path.moveTo(getMeasuredWidth() / 3, 100);
            path.lineTo(getMeasuredWidth() / 3, getMeasuredHeight() / 2);
            path.lineTo(getMeasuredWidth() / 3, getMeasuredHeight());
            canvas.drawText("" + norMin + "kg", getMeasuredWidth() / 3 - textPaint.measureText("" + norMin + "kg") / 2, 80, textPaint);
            canvas.drawPath(path, paint);
        }

        drawRight:
        {
            paint.setShader(null);
            paint.setPathEffect(pathEffect);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            path.reset();
            path.moveTo(getMeasuredWidth() * 2 / 3, 100);
            path.lineTo(getMeasuredWidth() * 2 / 3, getMeasuredHeight() / 2);
            path.lineTo(getMeasuredWidth() * 2 / 3, getMeasuredHeight());
            canvas.drawPath(path, paint);
            canvas.drawText("" + norMax + "kg", getMeasuredWidth() * 2 / 3 - textPaint.measureText("" + norMax + "kg") / 2, 80, textPaint);
        }

        drawLow:
        {
            float textLength = textPaint.measureText("偏低");
            float start = getMeasuredWidth() / 6 - textLength / 2;
            canvas.drawText("偏低", start, getMeasuredHeight() * 5 / 6, textPaint);
        }

        drawNor:
        {
            float textLength = textPaint.measureText("正常");
            float start = getMeasuredWidth() / 2 - textLength / 2;
            canvas.drawText("正常", start, getMeasuredHeight() * 5 / 6, textPaint);
        }

        drawHigh:
        {
            float textLength = textPaint.measureText("偏高");
            float start = getMeasuredWidth() * 5 / 6 - textLength / 2;
            canvas.drawText("偏高", start, getMeasuredHeight() * 5 / 6, textPaint);
        }

        drawTriangle:
        {
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(pointF.x, pointF.y);
            path.lineTo(pointF.x + 40, pointF.y - 45);
            path.lineTo(pointF.x - 40, pointF.y - 45);
            path.lineTo(pointF.x, pointF.y);
            if (curValue >= norMin && curValue <= norMax) {
                triPaint.setColor(Color.parseColor("#00c1e3"));
            } else {
                triPaint.setColor(Color.parseColor("#ea360a"));
            }
            canvas.drawPath(path, triPaint);
        }


    }


}
