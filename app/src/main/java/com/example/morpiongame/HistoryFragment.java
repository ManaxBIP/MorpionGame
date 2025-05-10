package com.example.morpiongame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                GameHistory history = adapter.getHistoryList().get(position);

                // Supprime de la base de données
                DBHelper dbHelper = new DBHelper(requireContext());
                dbHelper.deleteGame(history.getId());

                // Supprime de la liste
                adapter.removeItem(position);

                Toast.makeText(getContext(), "Partie supprimée", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewHistory);


        return view;
    }
}
