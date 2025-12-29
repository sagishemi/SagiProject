package com.example.sagiproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private Button[] leftBtns = new Button[4];
    private Button[] rightBtns = new Button[4];

    private TextView txtPlayer1;
    private TextView txtPlayer2;
    private TextView txtTurn;

    private GameBoardView boardView;

    private GameManager gameManager;
    private int level = Level.EASY; // כרגע רמה קלה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);
        txtTurn   = findViewById(R.id.txtTurn);

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

        // אחרי שהכל נמדד על המסך – נחשב מרכזי כפתורים
        boardView.post(new Runnable() {
            @Override
            public void run() {
                updateAnchorsFromButtons();
            }
        });

        for (int i = 0; i < 4; i++) {
            final int index = i;
            leftBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftClicked(index);
                }
            });
        }

        for (int i = 0; i < 4; i++) {
            final int index = i;
            rightBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightClicked(index);
                }
            });
        }
    }

    private void applyRoundToButtons(RoundData round) {
        String[] left = round.getLeft();
        String[] right = round.getRight();

        for (int i = 0; i < 4; i++) {
            leftBtns[i].setText(left[i]);
            leftBtns[i].setEnabled(true);
            leftBtns[i].setAlpha(1f);

            rightBtns[i].setText(right[i]);
            rightBtns[i].setEnabled(true);
            rightBtns[i].setAlpha(1f);
        }

        boardView.resetLines();
        // נעדכן שוב עוגנים אחרי שהטקסטים התעדכנו
        boardView.post(new Runnable() {
            @Override
            public void run() {
                updateAnchorsFromButtons();
            }
        });
    }

    private void updateAnchorsFromButtons() {
        float[] lx = new float[4], ly = new float[4];
        float[] rx = new float[4], ry = new float[4];

        int[] loc = new int[2];
        int[] rootLoc = new int[2];

        findViewById(R.id.root).getLocationOnScreen(rootLoc);

        for (int i = 0; i < 4; i++) {
            leftBtns[i].getLocationOnScreen(loc);
            lx[i] = (loc[0] - rootLoc[0]) + leftBtns[i].getWidth() / 2f;
            ly[i] = (loc[1] - rootLoc[1]) + leftBtns[i].getHeight() / 2f;

            rightBtns[i].getLocationOnScreen(loc);
            rx[i] = (loc[0] - rootLoc[0]) + rightBtns[i].getWidth() / 2f;
            ry[i] = (loc[1] - rootLoc[1]) + rightBtns[i].getHeight() / 2f;
        }

        boardView.setAnchors(lx, ly, rx, ry);
    }

    private void onLeftClicked(int index) {
        gameManager.selectLeft(index);
        for (int i = 0; i < 4; i++) {
            leftBtns[i].setAlpha(1f);
        }
        leftBtns[index].setAlpha(0.6f);
    }

    private void onRightClicked(int index) {
        MatchResult result = gameManager.selectRight(index);

        for (int i = 0; i < 4; i++) {
            leftBtns[i].setAlpha(1f);
        }

        if (result.getLeftIndex() != null && result.getRightIndex() != null) {
            Button leftBtn  = leftBtns[result.getLeftIndex()];
            Button rightBtn = rightBtns[result.getRightIndex()];

            // קו עם אנימציה
            boardView.addAnimatedLine(result.getLeftIndex(),
                    result.getRightIndex(),
                    result.isCorrect());

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
}
