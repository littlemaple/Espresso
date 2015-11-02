package com.rainbow.blue.espresso.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by blue on 2015/10/30.
 */
public class GradientView extends View {
    private Paint paint = new Paint();

    public GradientView(Context context) {
        super(context);
        initPaintComponent();
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintComponent();
    }

    private void initPaintComponent() {
        paint = new Paint();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rate = getMeasuredWidth() / 200f;
        paint.setShader(new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.GREEN, Color.YELLOW, Color.RED}, new float[]{0, 0.24f, 0.33f, 0.66f, 0.75f, 1f}, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

    }


}
