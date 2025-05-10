package com.example.morpiongame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.morpiongame.database.GameHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<GameHistory> historyList;

    public HistoryAdapter(List<GameHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        GameHistory history = historyList.get(position);
        String FinalResult = history.getResult() + " " + history.getDate();
        holder.textView.setText(FinalResult);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public List<GameHistory> getHistoryList() {return historyList;}

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.historyText);
        }
    }

    public void removeItem(int position) {
        historyList.remove(position);
        notifyItemRemoved(position);
    }

}
