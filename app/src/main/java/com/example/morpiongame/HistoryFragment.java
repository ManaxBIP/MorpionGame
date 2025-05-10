package com.example.morpiongame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.morpiongame.R;
import com.example.morpiongame.HistoryAdapter;
import com.example.morpiongame.database.DBHelper;
import com.example.morpiongame.database.GameHistory;

import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private HistoryAdapter adapter;
    private DBHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DBHelper(getContext());

        List<GameHistory> historyList = databaseHelper.getAllGames();
        if (historyList != null && !historyList.isEmpty()) {
            adapter = new HistoryAdapter(historyList);
            recyclerViewHistory.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "Aucune partie enregistrée", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
