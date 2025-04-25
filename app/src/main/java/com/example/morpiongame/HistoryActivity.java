package com.example.morpiongame;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.morpiongame.database.DBHelper;
import com.example.morpiongame.database.GameHistory;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistory;
    private HistoryAdapter adapter;
    private DBHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DBHelper(this);

        List<GameHistory> historyList = databaseHelper.getAllGames();
        if (historyList != null && !historyList.isEmpty()) {
            adapter = new HistoryAdapter(historyList);
            recyclerViewHistory.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Aucune partie enregistrée", Toast.LENGTH_SHORT).show();
        }
    }
}
