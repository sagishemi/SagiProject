package com.example.sagiproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sagiproject.gemini.GeminiCallback;
import com.example.sagiproject.gemini.GeminiManager;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Button[] leftBtns  = new Button[4];
    private Button[] rightBtns = new Button[4];

    private TextView txtPlayer1;
    private TextView txtPlayer2;
    private TextView txtTurn;

    private GameBoardView boardView;
    WordRepository wordRepository = new WordRepository();

    private GameManager gameManager;
    private int level = Level.EASY;   // כרגע רמה קלה (אפשר לשנות/לקבל מ-Intent)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);
        txtTurn    = findViewById(R.id.txtTurn);

        leftBtns[0] = findViewById(R.id.btnL0);
        leftBtns[1] = findViewById(R.id.btnL1);
        leftBtns[2] = findViewById(R.id.btnL2);
        leftBtns[3] = findViewById(R.id.btnL3);

        rightBtns[0] = findViewById(R.id.btnR0);
        rightBtns[1] = findViewById(R.id.btnR1);
        rightBtns[2] = findViewById(R.id.btnR2);
        rightBtns[3] = findViewById(R.id.btnR3);

        boardView = findViewById(R.id.boardView);

        gameManager = new GameManager(new WordRepository());
        gameManager.startNewGame(level, "Player 1", "Player 2");

        RoundData round = gameManager.startRound();
        applyRoundToButtons(round);
        updateScoreAndTurnUI();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            leftBtns[i].setOnClickListener(v -> onLeftClicked(index));
        }

        for (int i = 0; i < 4; i++) {
            final int index = i;
            rightBtns[i].setOnClickListener(v -> onRightClicked(index));
        }
        
        FBsingleton.getInstance(this);
        String prompt =  "תשובה עד 30 מילים";

        GeminiManager.getInstance().sendMessage(prompt, new GeminiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(() ->
                                {

                                }
                        );
                    }

                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }

    /** מילוי טקסטים בכפתורים בתחילת סיבוב */
    private void applyRoundToButtons(RoundData round) {
        String[] left  = round.getLeft();
        String[] right = round.getRight();

        for (int i = 0; i < 4; i++) {
            leftBtns[i].setText(left[i]);
            leftBtns[i].setEnabled(true);
            leftBtns[i].setAlpha(1f);

            rightBtns[i].setText(right[i]);
            rightBtns[i].setEnabled(true);
            rightBtns[i].setAlpha(1f);
        }

        // התחלת סיבוב – מוחקים קווים קודמים
        boardView.resetLines();
    }

    private void onLeftClicked(int index) {
        gameManager.selectLeft(index);
        for (int i = 0; i < 4; i++) {
            leftBtns[i].setAlpha(1f);
        }
        leftBtns[index].setAlpha(0.6f);   // סימון כפתור שנבחר
    }

    private void onRightClicked(int index) {
        MatchResult result = gameManager.selectRight(index);

        // מחזירים שקיפות רגילה לכל הכפתורים
        for (int i = 0; i < 4; i++) {
            leftBtns[i].setAlpha(1f);
        }

        if (result.getLeftIndex() != null && result.getRightIndex() != null) {

            int li = result.getLeftIndex();
            int ri = result.getRightIndex();

            Button leftBtn  = leftBtns[li];
            Button rightBtn = rightBtns[ri];

            // כאן הקסם – קו יוצא בדיוק מהאמצע של שני הכפתורים:
            boardView.addAnimatedLineBetweenButtons(leftBtn, rightBtn, result.isCorrect());

            if (result.isCorrect()) {
                leftBtn.setEnabled(false);
                rightBtn.setEnabled(false);
                leftBtn.setAlpha(0.5f);
                rightBtn.setAlpha(0.5f);
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        }

        updateScoreAndTurnUI();

        if (result.isRoundFinished()) {
            Toast.makeText(this, "Round finished - new round", Toast.LENGTH_SHORT).show();
            RoundData next = gameManager.startRound();
            applyRoundToButtons(next);
        }
    }

    private void updateScoreAndTurnUI() {
        txtPlayer1.setText("Player 1: " + gameManager.getPlayer(0).getScore());
        txtPlayer2.setText("Player 2: " + gameManager.getPlayer(1).getScore());
        int current = gameManager.getCurrentPlayerIndex();
        txtTurn.setText("Turn: " + (current == 0 ? "P1" : "P2"));
    }

    public void updateWords(ArrayList<WordPair> arrayList) {
        wordRepository.updateWords(arrayList);
    }
}
