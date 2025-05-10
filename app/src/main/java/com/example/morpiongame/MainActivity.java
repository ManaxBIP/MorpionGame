package com.example.morpiongame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_nav);
        // Configurer la navigation de la BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home){
                selectedFragment = new HomeFragment();
            }
            if (item.getItemId() == R.id.nav_history){
                selectedFragment = new HistoryFragment();
            }
            if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            // Remplacer le fragment actuel par le nouveau
            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
            }
            return true;
        });

        // Charger par défaut le fragment "Home"
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);  // Sélectionner "Home" au lancement
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Quitter l'application")
                .setMessage("Es-tu sûr de vouloir quitter ?")
                .setPositiveButton("Oui", (dialog, which) -> finish())
                .setNegativeButton("Non", null)
                .show();
    }

}
