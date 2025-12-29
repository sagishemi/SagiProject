package com.example.sagiproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<ConnectionLine> lines = new ArrayList<>();

    // נקודות מרכז של הכפתורים
    private float[] leftX = new float[4], leftY = new float[4];
    private float[] rightX = new float[4], rightY = new float[4];

    public GameBoardView(Context context) {
        super(context);
        init();
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
    }

    /** Activity מעבירה את מרכזי הכפתורים אחרי layout */
    public void setAnchors(float[] lx, float[] ly, float[] rx, float[] ry) {
        System.arraycopy(lx, 0, leftX, 0, 4);
        System.arraycopy(ly, 0, leftY, 0, 4);
        System.arraycopy(rx, 0, rightX, 0, 4);
        System.arraycopy(ry, 0, rightY, 0, 4);
        invalidate();
    }

    /** מתחיל סיבוב חדש – מוחק כל הקווים */
    public void resetLines() {
        lines.clear();
        invalidate();
    }

    /** מוסיף קו עם אנימציה בין שמאל לימין */
    public void addAnimatedLine(int leftIndex, int rightIndex, boolean correct) {
        float sx = leftX[leftIndex];
        float sy = leftY[leftIndex];
        float ex = rightX[rightIndex];
        float ey = rightY[rightIndex];

        ConnectionLine line = new ConnectionLine(sx, sy, ex, ey, correct);
        lines.add(line);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(250);
        animator.addUpdateListener(animation -> {
            line.progress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();

        // אם החיבור לא נכון – נמחק את הקו אחרי קצת זמן
        if (!correct) {
            postDelayed(() -> {
                lines.remove(line);
                invalidate();
            }, 450);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (ConnectionLine line : lines) {
            if (line.correct) {
                paint.setARGB(255, 0, 170, 0); // ירוק
            } else {
                paint.setARGB(255, 200, 0, 0); // אדום
            }
            canvas.drawLine(line.startX, line.startY,
                    line.getCurrentX(), line.getCurrentY(), paint);
        }
    }
}
