package com.example.morpiongame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer SharedPreferences
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // Appliquer le mode sombre/lumineux depuis SharedPreferences (au lancement)
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Initialiser le switch en fonction de l'état du mode sombre
        switchDarkMode.setChecked(isDarkMode);


        // Listener pour changer le mode sombre
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Sauvegarder l'état du mode sombre dans SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            // Appliquer le mode uniquement si nécessaire
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Exemple des boutons de jeu
        Button btnVsEasyAI = findViewById(R.id.btnVsEasyAI);
        Button btnVsHardAI = findViewById(R.id.btnVsHardAI);
        Button btnTwoPlayers = findViewById(R.id.btnTwoPlayers);

        btnVsEasyAI.setOnClickListener(v -> startGame("AI_EASY"));
        btnVsHardAI.setOnClickListener(v -> startGame("AI_HARD"));
        btnTwoPlayers.setOnClickListener(v -> startGame("TWO_PLAYER"));

        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

    }
    private void startGame(String mode) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quitter l'application")
                .setMessage("Es-tu sûr(e) de vouloir quitter le jeu ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    finishAffinity();  // Ferme toutes les activités et quitte l'app
                })
                .setNegativeButton("Non", null)
                .show();
    }

}




