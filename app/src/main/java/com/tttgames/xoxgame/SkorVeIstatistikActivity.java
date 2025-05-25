package com.tttgames.xoxgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// This class serves only to print the stats table
public class SkorVeIstatistikActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listViewMaclar;
    private ListView listViewIstatistik;
    private Button btnTemizle;
    private LinearLayout rootLayout;

    // Prints the table
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skor_ve_istatistik);
        rootLayout = findViewById(R.id.rootLayout);
        applyThemeFromSettings();
        databaseHelper = new DatabaseHelper(this);

        listViewMaclar = findViewById(R.id.listViewMaclar);
        listViewIstatistik = findViewById(R.id.listViewIstatistik);
        btnTemizle = findViewById(R.id.btnTemizle);


        yukleMacGecmisi();
        yukleOyuncuIstatistikleri();

        btnTemizle.setOnClickListener(v -> {
            databaseHelper.clearAllData();
            Toast.makeText(this, "Tüm maçlar ve istatistikler silindi.", Toast.LENGTH_SHORT).show();
            yukleMacGecmisi();
            yukleOyuncuIstatistikleri();
        });
    }

    // Info of the matches played (players, result, date)
    private void yukleMacGecmisi() {
        ArrayList<String> matchResults = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllMatchResults();

        if (cursor.moveToFirst()) {
            do {
                String player1 = cursor.getString(cursor.getColumnIndexOrThrow("player1"));
                String player2 = cursor.getString(cursor.getColumnIndexOrThrow("player2"));
                String winner = cursor.getString(cursor.getColumnIndexOrThrow("winner"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                String resultText;
                if ("Draw".equals(winner)) {
                    resultText = date + ": " + player1 + " vs " + player2 + " → Berabere";
                } else {
                    resultText = date + ": " + winner + " kazandı (" + player1 + " vs " + player2 + ")";
                }

                matchResults.add(resultText);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchResults);
        listViewMaclar.setAdapter(adapter);
    }

    // Stats for PvP matches
    private void yukleOyuncuIstatistikleri() {
        ArrayList<String> statsList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllPlayerStats();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
                int losses = cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
                int draws = cursor.getInt(cursor.getColumnIndexOrThrow("draws"));

                statsList.add(name + " - Galibiyet: " + wins + ", Mağlubiyet: " + losses + ", Beraberlik: " + draws);
            } while (cursor.moveToNext());
        }
        cursor.close();

        StatListAdapter adapter = new StatListAdapter(this, statsList);
        listViewIstatistik.setAdapter(adapter);
    }
    private void applyThemeFromSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("game_settings", Context.MODE_PRIVATE);
        String selectedTheme = sharedPreferences.getString("other_screens_theme", "Varsayilan");

        if (rootLayout != null) {
            switch (selectedTheme) {
                case "SiyahBeyaz":
                    rootLayout.setBackgroundResource(R.drawable.tema_siyahbeyaz1);
                    break;
                case "KirmiziTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_kirmizipembe1);
                    break;
                case "BrainRotTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_brainrot1);
                    break;
                default:
                    rootLayout.setBackgroundResource(R.drawable.tema_varsayilan1);
                    break;
            }
        }
    }

}
