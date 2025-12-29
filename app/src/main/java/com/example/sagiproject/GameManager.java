

package com.example.sagiproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameManager {

    private final WordRepository repository;
    private final Random random = new Random();

    private int level;
    private Player[] players;
    private int currentPlayerIndex;

    private RoundData currentRound;

    private int selectedLeftIndex = -1;
    private boolean[] matchedLeft = new boolean[4];
    private boolean[] matchedRight = new boolean[4];
    private int matchedCount = 0;

    public GameManager(WordRepository repository) {
        this.repository = repository;
    }

    public void startNewGame(int level, String player1Name, String player2Name) {
        this.level = level;
        this.players = new Player[]{ new Player(player1Name), new Player(player2Name) };
        this.currentPlayerIndex = 0;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getSelectedLeftIndex() {
        return selectedLeftIndex;
    }

    public RoundData getCurrentRound() {
        return currentRound;
    }

    public RoundData startRound() {
        selectedLeftIndex = -1;
        matchedLeft = new boolean[4];
        matchedRight = new boolean[4];
        matchedCount = 0;

        List<WordPair> all = repository.getWordsByLevel(level);
        if (all.size() < 4) {
            throw new IllegalStateException("Not enough words in repository");
        }

        List<WordPair> copy = new ArrayList<>(all);
        Collections.shuffle(copy, random);
        List<WordPair> chosen = copy.subList(0, 4);

        String[] left = new String[4];
        List<String> rightList = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            left[i] = chosen.get(i).getEnglish();
            rightList.add(chosen.get(i).getHebrew());
        }

        Collections.shuffle(rightList, random);
        String[] right = rightList.toArray(new String[0]);

        Map<Integer, Integer> correctMap = new HashMap<>();
        for (int li = 0; li < 4; li++) {
            String correctHeb = chosen.get(li).getHebrew();
            int ri = indexOf(right, correctHeb);
            correctMap.put(li, ri);
        }

        currentRound = new RoundData(left, right, correctMap);
        return currentRound;
    }

    private int indexOf(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) return i;
        }
        return -1;
    }

    public void selectLeft(int leftIndex) {
        if (leftIndex < 0 || leftIndex > 3) return;
        if (currentRound == null) return;
        if (matchedLeft[leftIndex]) return;
        selectedLeftIndex = leftIndex;
    }

    public MatchResult selectRight(int rightIndex) {
        if (rightIndex < 0 || rightIndex > 3) {
            return new MatchResult(false, isRoundFinished(), currentPlayerIndex, null, null);
        }
        if (currentRound == null) {
            return new MatchResult(false, false, currentPlayerIndex, null, null);
        }
        if (matchedRight[rightIndex]) {
            return new MatchResult(false, isRoundFinished(), currentPlayerIndex, null, null);
        }
        if (selectedLeftIndex == -1) {
            return new MatchResult(false, isRoundFinished(), currentPlayerIndex, null, null);
        }

        int leftIndex = selectedLeftIndex;
        selectedLeftIndex = -1;

        Integer correctRight = currentRound.getCorrectMap().get(leftIndex);
        boolean correct = (correctRight != null && correctRight == rightIndex);

        if (correct) {
            matchedLeft[leftIndex] = true;
            matchedRight[rightIndex] = true;
            matchedCount++;
            players[currentPlayerIndex].addScore(10);
        } else {
            players[currentPlayerIndex].addScore(-5);
            switchTurn();
        }

        return new MatchResult(correct, isRoundFinished(), currentPlayerIndex, leftIndex, rightIndex);
    }

    public boolean isRoundFinished() {
        return matchedCount >= 4;
    }

    private void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex == 0) ? 1 : 0;
    }

    public RoundData startNextRoundIfFinished() {
        if (isRoundFinished()) {
            return startRound();
        }
        return currentRound;
    }
}

