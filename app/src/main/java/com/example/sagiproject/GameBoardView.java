package com.example.sagiproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameBoardView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<ConnectionLine> lines = new ArrayList<>();

    public GameBoardView(Context context) {
        super(context);
        init();
    }

    public GameBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
    }

    /** מוחק את כל הקווים (כשמתחיל סיבוב חדש) */
    public void resetLines() {
        lines.clear();
        invalidate();
    }

    /**
     * מוסיף קו עם אנימציה בין שני כפתורים.
     * החישוב נעשה ביחס ל-GameBoardView עצמו,
     * ולכן הקו יוצא בדיוק ממרכז הכפתורים.
     */
    public void addAnimatedLineBetweenButtons(View leftButton,
                                              View rightButton,
                                              boolean correct) {

        if (leftButton == null || rightButton == null) return;

        int[] p1 = new int[2];
        int[] p2 = new int[2];
        int[] overlayPos = new int[2];

        // מיקום הכפתורים בחלון
        leftButton.getLocationInWindow(p1);
        rightButton.getLocationInWindow(p2);

        // מיקום ה-View שמצייר את הקו בחלון
        this.getLocationInWindow(overlayPos);

        // אמצע כפתור שמאל
        float startX = p1[0] - overlayPos[0] + leftButton.getWidth() / 2f;
        float startY = p1[1] - overlayPos[1] + leftButton.getHeight() / 2f;

        // אמצע כפתור ימין
        float endX   = p2[0] - overlayPos[0] + rightButton.getWidth() / 2f;
        float endY   = p2[1] - overlayPos[1] + rightButton.getHeight() / 2f;

        ConnectionLine line = new ConnectionLine(startX, startY, endX, endY, correct);
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
                paint.setARGB(255, 0, 170, 0);   // ירוק
            } else {
                paint.setARGB(255, 200, 0, 0);   // אדום
            }

            canvas.drawLine(
                    line.startX,
                    line.startY,
                    line.getCurrentX(),
                    line.getCurrentY(),
                    paint
            );
        }
    }
}
