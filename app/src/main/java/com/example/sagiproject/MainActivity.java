package com.example.sagiproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private WordRepository wordRepository;

    private Button btnStartGame;
    private Button btnInstructions;
    private Button btnLogOut;
    public ArrayList<WordPair> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartGame = findViewById(R.id.btnStartGame);
        btnInstructions = findViewById(R.id.btnInstructions);
        btnLogOut = findViewById(R.id.btnLogOut);

        // מעבר למסך המשחק
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(this);

        // מעבר למסך ההוראות
        btnInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, InstructionsActivity.class);
                    startActivity(intent);
                }


        });


        /*public void updateWords (ArrayList<WordPair>arrayList) {
            wordRepository.updateWords(arrayList);

        }


*/
        FBsingleton.getInstance(this);
    }

    @Override
    public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            finish();
    }
}
