package com.example.sagiproject;

import java.util.HashMap;
import java.util.Map;

public class RoundData {

    private final String[] left;   // 4 אנגלית
    private final String[] right;  // 4 עברית (מעורבב)
    private final Map<Integer, Integer> correctMap; // leftIndex -> rightIndex

    public RoundData(String[] left, String[] right, Map<Integer, Integer> correctMap) {
        if (left == null || right == null || correctMap == null) {
            throw new IllegalArgumentException("Null data");
        }
        if (left.length != 4 || right.length != 4) {
            throw new IllegalArgumentException("Round must have 4 items on each side");
        }
        this.left = left;
        this.right = right;
        this.correctMap = new HashMap<>(correctMap);
    }

    public String[] getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public Map<Integer, Integer> getCorrectMap() {
        return new HashMap<>(correctMap);
    }
}
