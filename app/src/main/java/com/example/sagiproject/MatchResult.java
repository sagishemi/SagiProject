package com.example.sagiproject;

public class MatchResult {

    private final boolean correct;
    private final boolean roundFinished;
    private final int currentPlayerIndex; // 0 = שחקן 1, 1 = שחקן 2
    private final Integer leftIndex;
    private final Integer rightIndex;

    public MatchResult(boolean correct, boolean roundFinished, int currentPlayerIndex,
                       Integer leftIndex, Integer rightIndex) {
        this.correct = correct;
        this.roundFinished = roundFinished;
        this.currentPlayerIndex = currentPlayerIndex;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    public boolean isCorrect() {
        return correct;
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Integer getLeftIndex() {
        return leftIndex;
    }

    public Integer getRightIndex() {
        return rightIndex;
    }
}

