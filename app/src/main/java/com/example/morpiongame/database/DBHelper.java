package com.example.morpiongame.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "morpion_game.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYER_SCORE = "player_score";
    public static final String COLUMN_AI_SCORE = "ai_score";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_DATE = "date";

    // Requête pour créer la table de l'historique
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_HISTORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PLAYER_SCORE + " INTEGER, " +
                    COLUMN_AI_SCORE + " INTEGER, " +
                    COLUMN_RESULT + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void deleteGame(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void insertGameHistory(SQLiteDatabase db, int playerScore, int aiScore, String result, String date) {
        String insertQuery = "INSERT INTO " + TABLE_HISTORY +
                " (" + COLUMN_PLAYER_SCORE + ", " + COLUMN_AI_SCORE + ", " + COLUMN_RESULT + ", " + COLUMN_DATE + ") " +
                "VALUES (" + playerScore + ", " + aiScore + ", '" + result + "', '" + date + "');";
        db.execSQL(insertQuery);
    }

    public void getAllHistory(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_HISTORY;
        db.rawQuery(query, null);
    }

    public List<GameHistory> getAllGames() {
        List<GameHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String result = cursor.getString(cursor.getColumnIndex("result"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                historyList.add(new GameHistory(id, result, date));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return historyList;
    }

}
