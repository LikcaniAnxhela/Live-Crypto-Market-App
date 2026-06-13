package com.unyt.cryptopulse.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniChartView extends View {

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<Float> values = new ArrayList<>();

    public MiniChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setColor(Color.rgb(0, 229, 255));
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);

        fillPaint.setColor(Color.argb(45, 0, 229, 255));
        fillPaint.setStyle(Paint.Style.FILL);

        gridPaint.setColor(Color.argb(70, 160, 175, 205));
        gridPaint.setStrokeWidth(1.5f);

        dotPaint.setColor(Color.rgb(39, 232, 167));
        dotPaint.setStyle(Paint.Style.FILL);

        setDemoValues(10);
    }

    public void setDemoValues(double seed) {
        values.clear();

        Random random = new Random((long) (seed * 1000));
        float base = 50;

        for (int i = 0; i < 14; i++) {
            base += random.nextFloat() * 22f - 10f;

            if (base < 8) {
                base = 8;
            }

            if (base > 95) {
                base = 95;
            }

            values.add(base);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0 || values.size() < 2) {
            return;
        }

        for (int i = 1; i < 4; i++) {
            float y = height * i / 4f;
            canvas.drawLine(0, y, width, y, gridPaint);
        }

        Path linePath = new Path();
        Path fillPath = new Path();

        for (int i = 0; i < values.size(); i++) {
            float x = i * (width / (float) (values.size() - 1));
            float y = height - (values.get(i) / 100f * height);

            if (i == 0) {
                linePath.moveTo(x, y);

                fillPath.moveTo(x, height);
                fillPath.lineTo(x, y);
            } else {
                linePath.lineTo(x, y);
                fillPath.lineTo(x, y);
            }
        }

        fillPath.lineTo(width, height);
        fillPath.close();

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(linePath, linePaint);

        float lastX = width;
        float lastY = height - (values.get(values.size() - 1) / 100f * height);

        canvas.drawCircle(lastX, lastY, 8f, dotPaint);
    }
}