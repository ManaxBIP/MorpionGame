package com.example.morpiongame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVsEasyAI = findViewById(R.id.btnVsEasyAI);
        Button btnVsHardAI = findViewById(R.id.btnVsHardAI);
        Button btnTwoPlayers = findViewById(R.id.btnTwoPlayers);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        btnVsEasyAI.setOnClickListener(v -> startGame("AI_EASY"));
        btnVsHardAI.setOnClickListener(v -> startGame("AI_HARD"));
        btnTwoPlayers.setOnClickListener(v -> startGame("TWO_PLAYER"));

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void startGame(String mode) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}
