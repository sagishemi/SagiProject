package com.example.sagiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnStartGame;
    private Button btnInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("GEMINI_TEST", "MainActivity onCreate started");   // לוג בדיקה בסיסי

        // ---- בדיקה שחיבור ל-Gemini עובד ----
        GeminiWordsService service = new GeminiWordsService();

        new Thread(() -> {
            Log.d("GEMINI_TEST", "Thread for Gemini started");

            List<WordPair> words = service.getWordsFromGemini(Level.EASY);

            Log.d("GEMINI_TEST", "Gemini returned " + words.size() + " words");

            for (WordPair w : words) {
                Log.d("GEMINI", "WORD: " + w.getEnglish() + " = " + w.getHebrew());
            }
        }).start();
        // ---- סוף בדיקה ----

        btnStartGame = findViewById(R.id.btnStartGame);
        btnInstructions = findViewById(R.id.btnInstructions);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        btnInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InstructionsActivity.class);
                startActivity(intent);
            }
        });
    }
}
