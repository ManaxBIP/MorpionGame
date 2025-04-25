package com.example.morpiongame.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameHistoryDao {

    @Insert
    void insert(GameHistory game);

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    List<GameHistory> getAllGames();
}
