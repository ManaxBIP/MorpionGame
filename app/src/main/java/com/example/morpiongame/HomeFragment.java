package com.example.morpiongame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.morpiongame.database.DBHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button playButton = view.findViewById(R.id.playButton);
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GameActivity.class);
            intent.putExtra("mode", "AI_EASY");
            startActivity(intent);
        });

        return view;
    }
}

