package com.example.sagiproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsActivity extends AppCompatActivity {

    private Button btnStartFromInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        btnStartFromInstructions = findViewById(R.id.btnStartFromInstructions);

        btnStartFromInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InstructionsActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}

