package com.example.whatisbetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewPokemon;
    private TextView scoreTextView;
    private LinearLayout lettersLayout;
    private TextView congratulationsTextView;
    private Button newRoundButton;
    private List<String> pokemonNames;
    private int currentPokemonIndex;
    private int correctGuesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewPokemon = findViewById(R.id.imageViewPokemon);
        scoreTextView = findViewById(R.id.scoreTextView);
        lettersLayout = findViewById(R.id.lettersLayout);
        congratulationsTextView = findViewById(R.id.congratulationsTextView);
        newRoundButton = findViewById(R.id.newRoundButton);
        pokemonNames = Arrays.asList("Charizard", "Lucario", "Gengar", "Mewtwo", "Machamp", "Chandelure", "Aegislash", "Pikachu", "Empoleon", "Gardevoir", "Sceptile", "Decidueye", "Weavile", "Suicune", "Garchomp", "Scizor", "Darkrai", "Blastoise");

        generateRandomPokemon();

        newRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomPokemon();
            }
        });

        Button endGameButton = findViewById(R.id.newRoundButton2);
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();
            }
        });
    }

    private void endGame() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("correctGuesses", correctGuesses);
        startActivity(intent);
    }

    private void generateRandomPokemon() {
        Random random = new Random();
        currentPokemonIndex = random.nextInt(pokemonNames.size());
        String currentPokemonName = pokemonNames.get(currentPokemonIndex);

        int imageResource = getResources().getIdentifier(currentPokemonName.toLowerCase(), "drawable", getPackageName());
        imageViewPokemon.setImageResource(imageResource);

        lettersLayout.removeAllViews();

        for (int i = 0; i < currentPokemonName.length(); i++) {
            TextView textView = new TextView(this);
            textView.setText("_");
            textView.setTextSize(24);
            textView.setPadding(16, 0, 16, 0);
            textView.setOnDragListener(dropListener);
            lettersLayout.addView(textView);
        }

        List<Character> nameCharacters = new ArrayList<>();
        for (char c : currentPokemonName.toCharArray()) {
            nameCharacters.add(c);
        }
        Collections.shuffle(nameCharacters);

        for (char c : nameCharacters) {
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(c));
            textView.setTextSize(24);
            textView.setPadding(16, 0, 16, 0);
            textView.setOnTouchListener(touchListener);
            lettersLayout.addView(textView);
        }
    }


    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(null, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }
    };

    private final View.OnDragListener dropListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DROP:
                    TextView draggedTextView = (TextView) event.getLocalState();
                    TextView dropTargetTextView = (TextView) v;
                    CharSequence draggedCharSequence = draggedTextView.getText();
                    CharSequence dropTargetCharSequence = dropTargetTextView.getText();
                    if (dropTargetCharSequence.equals("_")) {
                        dropTargetTextView.setText(draggedCharSequence);
                        draggedTextView.setVisibility(View.INVISIBLE);
                        checkWord();
                    }
                    break;
            }
            return true;
        }
    };

    private void checkWord() {
        StringBuilder enteredWordBuilder = new StringBuilder();
        for (int i = 0; i < lettersLayout.getChildCount(); i++) {
            View view = lettersLayout.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                enteredWordBuilder.append(textView.getText().toString());
            }
        }
        String enteredWord = enteredWordBuilder.toString().replace(" ", "");
        Log.d("CHECK_WORD", "Wpisana nazwa: " + enteredWord + ", Oczekiwana nazwa: " + pokemonNames.get(currentPokemonIndex));
        if (enteredWord.equalsIgnoreCase(pokemonNames.get(currentPokemonIndex))) {
            congratulationsTextView.setVisibility(View.VISIBLE);
            newRoundButton.setVisibility(View.VISIBLE);
            correctGuesses++;
            updateScore(correctGuesses);
        }
    }
    private void updateScore(int score) {
        scoreTextView.setText("Punkty: " + score);
    }

    public void endGame(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("correctGuesses", correctGuesses);
        startActivity(intent);
    }
}
