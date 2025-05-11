package com.example.morpiongame;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.example.morpiongame.database.DBHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private DBHelper dbHelper;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        checkLocationPermission();
        scoreText = findViewById(R.id.scoreText);
        replayButton = findViewById(R.id.replayButton);
        replayButton.setOnClickListener(v -> resetGame());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> backMenu());
        setupButtons();

        // Initialisation de DBHelper
        dbHelper = DBHelper.getInstance(this);

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

    private void writeScoreToFile(String content) {
        try {
            FileOutputStream fos = openFileOutput("score_log.txt", MODE_APPEND);
            fos.write((content + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // Enregistrer le résultat dans la base de données
        String result = message.contains("Tu") ? "win" : (message.contains("IA") ? "lose" : "draw");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertGameHistory(db, playerScore, aiScore, result, date);

        // Enregistrer le résultat dans un fichier
        String fileLog = "Résultat: " + result + " | Score: Toi " + playerScore + " - IA " + aiScore;
        writeScoreToFile(fileLog);

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
        startActivity(intent);
        finish();
    }

    private void getUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String country = addresses.get(0).getCountryName();
                                TextView locationText = findViewById(R.id.locationText);
                                locationText.setText("Vous jouez depuis : " + country);
                                locationText.setVisibility(View.VISIBLE);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            }
        }
    }
}
