package com.example.morpiongame.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_history")
public class GameHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public long timestamp;// pour trier par date
    private String result;
    private String date;
    public GameHistory(int id, String result, String date) {
        this.id = id;
        this.result = result;
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public String getDate(){
        return date;
    }

    public int getId() {return id;}

    public void setResult(String result) {
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
