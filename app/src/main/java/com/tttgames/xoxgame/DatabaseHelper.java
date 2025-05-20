package com.tttgames.xoxgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.List;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xox_game.db";
    private static final int DATABASE_VERSION = 1;

    // Table: players
    private static final String TABLE_PLAYERS = "players";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WINS = "wins";
    private static final String COLUMN_LOSSES = "losses";
    private static final String COLUMN_DRAWS = "draws";

    // Table: matches
    private static final String TABLE_MATCHES = "matches";
    private static final String COLUMN_PLAYER1 = "player1";
    private static final String COLUMN_PLAYER2 = "player2";
    private static final String COLUMN_WINNER = "winner";
    private static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPlayersTable = "CREATE TABLE " + TABLE_PLAYERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT UNIQUE, " +
                COLUMN_WINS + " INTEGER DEFAULT 0, " +
                COLUMN_LOSSES + " INTEGER DEFAULT 0, " +
                COLUMN_DRAWS + " INTEGER DEFAULT 0);";

        String createMatchesTable = "CREATE TABLE " + TABLE_MATCHES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER1 + " TEXT, " +
                COLUMN_PLAYER2 + " TEXT, " +
                COLUMN_WINNER + " TEXT, " +
                COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

        db.execSQL(createPlayersTable);
        db.execSQL(createMatchesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        onCreate(db);
    }

    // Oyuncu ekle veya var ise dokunma
    public void addPlayerIfNotExists(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PLAYERS, null, COLUMN_NAME + "=?",
                new String[]{name}, null, null, null);
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            db.insert(TABLE_PLAYERS, null, values);
        }
        cursor.close();
    }

    // Maç sonucu kaydet
    public void addMatchResult(String player1, String player2, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Ekle maç
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER1, player1);
        values.put(COLUMN_PLAYER2, player2);
        values.put(COLUMN_WINNER, winner);
        db.insert(TABLE_MATCHES, null, values);

        // İstatistikleri güncelle
        updateStats(player1, player2, winner);
    }

    private void updateStats(String player1, String player2, String winner) {
        if (winner.equals("Draw")) {
            incrementDraw(player1);
            incrementDraw(player2);
        } else {
            incrementWin(winner);
            if (winner.equals(player1)) {
                incrementLoss(player2);
            } else {
                incrementLoss(player1);
            }
        }
    }

    private void incrementWin(String name) {
        updateStat(name, COLUMN_WINS);
    }

    private void incrementLoss(String name) {
        updateStat(name, COLUMN_LOSSES);
    }

    private void incrementDraw(String name) {
        updateStat(name, COLUMN_DRAWS);
    }

    private void updateStat(String name, String column) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PLAYERS + " SET " + column + " = " + column + " + 1 WHERE " + COLUMN_NAME + " = ?",
                new String[]{name});
    }

    // Oyuncu istatistiklerini al
    public Cursor getAllPlayerStats() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PLAYERS, null, null, null, null, null, COLUMN_WINS + " DESC");
    }

    // Tüm maç sonuçlarını al
    public Cursor getAllMatchResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MATCHES, null, null, null, null, null, COLUMN_DATE + " DESC");
    }

    // Tüm verileri temizle

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MATCHES, null, null);
        db.delete(TABLE_PLAYERS, null, null);
    }

    public List<String> getAllPlayerNames() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT name FROM players", null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return names;
    }

}
