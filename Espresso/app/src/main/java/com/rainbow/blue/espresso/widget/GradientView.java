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
import android.support.annotation.NonNull;
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
    private float curVal = 0;


    private Range range;

    public GradientView(Context context) {
        super(context);
        initPaintComponent();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintComponent();
    }

    public Range getRange() {
        if (range == null) {
            range = new Range();
            range.desc = new String[]{"偏低", "正常", "偏高"};
            range.values = new float[]{0f, 1.9f, 2.5f, 6f};
            range.unit = "kg";
            range.position = new float[]{0, 1f / 3, 2f / 3, 1};
        }
        return range;
    }

    /**
     * @param range
     */
    public void setRange(@NonNull Range range) {
        this.range = range;
        postInvalidate();
    }

    /**
     * 必须在初始化后才可以起作用
     *
     * @param value
     */
    public void setValue(float value) {
        this.pointF.x = getMeasuredWidth() * getRange().getPosition(value);
        this.pointF.y = getMeasuredHeight() / 5;
        curVal = value;
        postInvalidate();
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
            path.moveTo(20, getMeasuredHeight() * 2 / 5);
            path.lineTo(getMeasuredWidth() - 20, getMeasuredHeight() * 2 / 5);
            path.quadTo(getMeasuredWidth(), getMeasuredHeight() * 2 / 5, getMeasuredWidth(), getMeasuredHeight() * 2 / 5 + 20);
            path.lineTo(getMeasuredWidth(), getMeasuredHeight() * 3 / 5 - 20);
            path.quadTo(getMeasuredWidth(), getMeasuredHeight() * 3 / 5, getMeasuredWidth() - 20, getMeasuredHeight() * 3 / 5);
            path.lineTo(20, getMeasuredHeight() * 3 / 5);
            path.quadTo(0, getMeasuredHeight() * 3 / 5, 0, getMeasuredHeight() * 3 / 5 - 20);
            path.lineTo(0, getMeasuredHeight() * 2 / 5 + 20);
            path.quadTo(0, getMeasuredHeight() * 2 / 5, 20, getMeasuredHeight() * 2 / 5);
            canvas.drawPath(path, paint);
        }
        drawTriangle:
        {
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(pointF.x, pointF.y);
            path.lineTo(pointF.x + 40, pointF.y - 45);
            path.lineTo(pointF.x - 40, pointF.y - 45);
            path.lineTo(pointF.x, pointF.y);

            triPaint.setColor(getIndicatorColor(curVal));
            canvas.drawPath(path, triPaint);
        }

        drawLimit:
        {
            for (int i = 1; i < getRange().position.length; i++) {
                drawDesc:
                {
                    if (i > getRange().desc.length)
                        continue;
                    float textLength = textPaint.measureText(getRange().desc[i - 1]);
                    float start = getMeasuredWidth() * (getRange().position[i] + getRange().position[i - 1]) / 2 - textLength / 2;
                    canvas.drawText(getRange().desc[i - 1], start, getMeasuredHeight() * 4 / 5 + 20, textPaint);
                }
                if (i == getRange().position.length - 1)
                    continue;
                paint.setShader(null);
                paint.setPathEffect(pathEffect);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                path.reset();
                path.moveTo(getMeasuredWidth() * getRange().position[i], getMeasuredHeight() / 5 + 20);
                path.lineTo(getMeasuredWidth() * getRange().position[i], getMeasuredHeight() / 2);
                path.lineTo(getMeasuredWidth() * getRange().position[i], getMeasuredHeight());
                canvas.drawText("" + getRange().values[i] + getRange().unit, getMeasuredWidth() * getRange().position[i] - textPaint.measureText(getRange().values[i] + getRange().unit) / 2, getMeasuredHeight() / 5, textPaint);
                canvas.drawPath(path, paint);


            }

        }
    }


    private int getIndicatorColor(float value) {
        if (getRange().values.length < 3)
            return Color.parseColor("#ea360a");
        if (value < getRange().values[1])
            return Color.parseColor("#00c1e3");
        if (value > getRange().values[getRange().values.length - 2])
            return Color.parseColor("#ea360a");
        return Color.parseColor("#7cc36c");
    }

    public static class Range {
        public float[] values;
        public float[] position;
        public String[] desc;
        public String unit;

        int getDivideNum() {
            if (position.length <= 2)
                return 1;
            return position.length - 2;
        }

        float getMin() {
            if (values == null || values.length <= 0)
                return 0;
            return values[0];
        }

        float getMax() {
            if (values == null || values.length <= 0)
                return 0;
            return values[values.length - 1];
        }

        float getPosition(float value) {
            if (values == null || values.length <= 1)
                return 0.5f;
            if (value < getMin()) {
                return 0.1f;
            } else if (value > getMax()) {
                return 0.9f;
            } else {
                for (int i = 0; i < values.length - 1; i++) {
                    if (value > values[i] && value < values[i + 1]) {
                        return (value - values[i]) / (values[i + 1] - values[i]) * (position[i + 1] - position[i]) + position[i];
                    }
                }
                return 0.5f;
            }

        }
    }
}
