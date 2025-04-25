package com.example.morpiongame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private TextView scoreText;
    private Button replayButton;
    private boolean playerTurn = true;
    private int roundCount = 0;
    private int playerScore = 0;
    private int aiScore = 0;
    private String gameMode = "";
    private boolean gameOver = false;
    private Button backButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreText = findViewById(R.id.scoreText);
        replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(v -> resetGame());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> backMenu());
        setupButtons();

        // Récupérer le mode de jeu
        gameMode = getIntent().getStringExtra("mode");
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        // Appliquer le mode sombre/lumineux lors de la création de l'activité
        updateNightMode();
    }

    private void setupButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(v -> {
                    if (!((Button) v).getText().toString().equals("") || gameOver) return;
                    ((Button) v).setText("X");
                    roundCount++;

                    if (checkWin("X")) {
                        showResult("Tu as gagné !");
                        return;
                    }

                    if (roundCount == 9) {
                        showResult("Égalité !");
                        return;
                    }

                    playerTurn = false;
                    if (gameMode.equals("AI_EASY")) {
                        aiMoveEasy();
                    } else if (gameMode.equals("AI_HARD")) {
                        aiMoveHard();
                    } else {
                        playerTurn = true;
                    }
                });
            }
        }
    }

    private void aiMoveEasy() {
        Random rand = new Random();
        int i, j;

        do {
            i = rand.nextInt(3);
            j = rand.nextInt(3);
        } while (!buttons[i][j].getText().toString().equals(""));

        buttons[i][j].setText("O");
        roundCount++;

        if (checkWin("O")) {
            showResult("L'IA a gagné !");
            return;
        }

        if (roundCount == 9) {
            showResult("Égalité !");
            return;
        }

        playerTurn = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Appliquer le mode sombre/lumineux à chaque fois que l'activité revient
        updateNightMode();
    }

    private void updateNightMode() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void aiMoveHard() {
        // Logique IA Difficile (Minimax à implémenter ici)
        aiMoveEasy();  // En attendant l'implémentation du Minimax, on garde Easy
    }

    private boolean checkWin(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) && buttons[i][1].getText().equals(player) && buttons[i][2].getText().equals(player)) return true;
            if (buttons[0][i].getText().equals(player) && buttons[1][i].getText().equals(player) && buttons[2][i].getText().equals(player)) return true;
        }
        if (buttons[0][0].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][2].getText().equals(player)) return true;
        if (buttons[0][2].getText().equals(player) && buttons[1][1].getText().equals(player) && buttons[2][0].getText().equals(player)) return true;
        return false;
    }

    private void showResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        gameOver = true;
        if (message.contains("Tu")) {
            playerScore++;
        } else if (message.contains("IA")) {
            aiScore++;
        }
        updateScore();
        replayButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    private void updateScore() {
        String scoreStr = "Score - Toi: " + playerScore + " | IA: " + aiScore;
        scoreText.setText(scoreStr);
    }

    private void resetGame() {
        roundCount = 0;
        playerTurn = true;
        gameOver = false;
        replayButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void backMenu() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Ferme toutes les autres activités avant de revenir au menu principal.
        startActivity(intent);

        // Fermer l'activité en cours (GameActivity).
        finish();
    }
}
