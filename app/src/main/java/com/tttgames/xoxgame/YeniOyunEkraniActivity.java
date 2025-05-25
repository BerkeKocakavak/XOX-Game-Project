package com.tttgames.xoxgame;

import com.tttgames.xoxgame.DatabaseHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

public class YeniOyunEkraniActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private AutoCompleteTextView etPlayer1Name, etPlayer2Name;
    private LinearLayout rootLayout;
    private Button btnEasy, btnMedium, btnHard, btnStartGame;

    private void setupAutoCompleteNames() {
        List<String> playerNames = databaseHelper.getAllPlayerNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, playerNames);
        etPlayer1Name.setAdapter(adapter);
        etPlayer2Name.setAdapter(adapter);
        etPlayer1Name.setThreshold(1);
        etPlayer2Name.setThreshold(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yenioyunekrani);
        databaseHelper = new DatabaseHelper(this);

        rootLayout = findViewById(R.id.rootLayout);
        applyThemeFromSettings();

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnStartGame = findViewById(R.id.btnStartGame);
        etPlayer1Name = findViewById(R.id.etPlayer1Name); // Bu AutoCompleteTextView olmalı
        etPlayer2Name = findViewById(R.id.etPlayer2Name); // Bu AutoCompleteTextView olmalı

        btnEasy.setOnClickListener(v -> startGame(1));
        btnMedium.setOnClickListener(v -> startGame(2));
        btnHard.setOnClickListener(v -> startGame(3));
        btnStartGame.setOnClickListener(v -> {
            String player1Name = etPlayer1Name.getText().toString().trim();
            String player2Name = etPlayer2Name.getText().toString().trim();
            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                Toast.makeText(this, "Oyuncu adlarını girin!", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("player_names", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PLAYER1_NAME", player1Name);
                editor.putString("PLAYER2_NAME", player2Name);
                editor.apply();

                databaseHelper.addPlayerIfNotExists(player1Name);
                databaseHelper.addPlayerIfNotExists(player2Name);
                startGame(4);
            }
        });

        etPlayer1Name = findViewById(R.id.etPlayer1Name);
        etPlayer2Name = findViewById(R.id.etPlayer2Name);

        setupAutoCompleteNames();
    }


    private void startGame(int gameMode) {
        String player1Name = etPlayer1Name.getText().toString().trim();
        String player2Name = etPlayer2Name.getText().toString().trim();

        if (player1Name.isEmpty()) {
            player1Name = "Oyuncu 1";
        }
        if (gameMode == 4 && player2Name.isEmpty()) {
            Toast.makeText(this, "İki kişilik modda iki oyuncu adı da girilmelidir!", Toast.LENGTH_SHORT).show();
            return;
        } else if (gameMode != 4) {
            player2Name = "Yapay Zeka";
        }

        //only add players if it's a pvp game

        if (gameMode == 4) {
            databaseHelper.addPlayerIfNotExists(player1Name);
            databaseHelper.addPlayerIfNotExists(player2Name);
        }

        Intent intent = new Intent(this, OyunEkrani.class);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("PLAYER1_NAME", player1Name);
        intent.putExtra("PLAYER2_NAME", player2Name);
        startActivity(intent);
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