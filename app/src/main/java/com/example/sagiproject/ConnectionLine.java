package com.example.sagiproject;

public class ConnectionLine {

    public final float startX;
    public final float startY;

    public final float endX;
    public final float endY;

    public final boolean correct;

    // 0..1 - כמה מהקו צויר (לאנימציה)
    public float progress = 0f;

    public ConnectionLine(float startX, float startY,
                          float endX, float endY,
                          boolean correct) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.correct = correct;
    }

    public float getCurrentX() {
        return startX + (endX - startX) * progress;
    }

    public float getCurrentY() {
        return startY + (endY - startY) * progress;
    }
}
