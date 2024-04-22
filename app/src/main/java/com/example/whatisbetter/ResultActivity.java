package com.example.whatisbetter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Odbierz wynik z MainActivity
        int correctGuesses = getIntent().getIntExtra("correctGuesses", 0);

        // Wyświetl wynik w TextView
        TextView resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setText("Liczba poprawnych odpowiedzi: " + correctGuesses);
    }
}