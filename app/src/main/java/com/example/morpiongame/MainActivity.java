package com.example.morpiongame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private boolean PlayerTurn = true;  //X joue en premier
    private int RoundCount = 0;
    private int PlayerScore = 0;
    private int IAScore = 0;
    private TextView scoreText;
    private Button replayButton;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupButton();
        scoreText = findViewById(R.id.scoreText);
        replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(v -> ResetGame());
    }

    private void SetupButton(){
        for (int i = 0 ; i < 3 ; i++){
            for (int j = 0; j < 3 ; j++){
                String ButtonId = "button" + i + j;
                int ResId = getResources().getIdentifier(ButtonId, "id", getPackageName());
                buttons[i][j] = findViewById(ResId);

                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(v -> {
                    if (!((Button) v).getText().toString().equals("")) return;
                    if (!PlayerTurn) return;
                    if (gameOver) return;

                    ((Button) v).setText("X");
                    RoundCount++;

                    if(CheckWin("X")) {
                        ShowResult("Tu as gagné !");
                        return;
                    }

                    if(RoundCount == 9) {
                        ShowResult("Egalité !");
                        return;
                    }

                    PlayerTurn = false;
                    aiMove();
                });
            }
        }
    }

    private void aiMove(){
        Random rand = new Random();
        int i, j;

        do {
            i = rand.nextInt(3);
            j = rand.nextInt(3);
        } while (!buttons[i][j].getText().toString().equals(""));

        buttons[i][j].setText("O");
        RoundCount++;

        if(CheckWin("O")){
            ShowResult("L'IA a gagné !");
            return;
        }

        if(RoundCount == 9) {
            ShowResult("Egalité !");
            return;
        }

        PlayerTurn = true;
    }

    private boolean CheckWin(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) &&
                    buttons[i][1].getText().equals(player) &&
                    buttons[i][2].getText().equals(player)) return true;

            if (buttons[0][i].getText().equals(player) &&
                    buttons[1][i].getText().equals(player) &&
                    buttons[2][i].getText().equals(player)) return true;
        }

        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) return true;

        if (buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player)) return true;

        return false;
    }

    private void ShowResult(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        gameOver = true;

        if(message.contains("gagné") && message.contains("Tu")) {
            PlayerScore++;
        } else if (message.contains("IA")){
            IAScore++;
        }

        UpdateScore();
        replayButton.setVisibility(View.VISIBLE);
    }

    private void ResetGame(){
        RoundCount = 0;
        PlayerTurn = true;
        gameOver = false;
        replayButton.setVisibility(View.GONE);
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j].setText("");
            }
        }
    }

    private void UpdateScore(){
        String scoreStr = "Score - Toi: " + PlayerScore + " | IA: " + IAScore;
        scoreText.setText(scoreStr);
    }
}