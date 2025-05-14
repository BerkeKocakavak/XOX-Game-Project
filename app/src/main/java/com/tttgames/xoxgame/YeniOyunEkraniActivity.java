package com.tttgames.xoxgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class YeniOyunEkraniActivity extends AppCompatActivity {
    private LinearLayout rootLayout;
    private Button btnEasy, btnMedium, btnHard, btnStartGame;
    private EditText etPlayer1Name, etPlayer2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yenioyunekrani);

        rootLayout = findViewById(R.id.rootLayout);
        applyThemeFromSettings();

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnStartGame = findViewById(R.id.btnStartGame);
        etPlayer1Name = findViewById(R.id.etPlayer1Name);
        etPlayer2Name = findViewById(R.id.etPlayer2Name);

        btnEasy.setOnClickListener(v -> startGame(1));
        btnMedium.setOnClickListener(v -> startGame(2));
        btnHard.setOnClickListener(v -> startGame(3));
        btnStartGame.setOnClickListener(v -> {
            String player1Name = etPlayer1Name.getText().toString().trim();
            String player2Name = etPlayer2Name.getText().toString().trim();
            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                Toast.makeText(this, "Oyuncu adlarını girin!", Toast.LENGTH_SHORT).show();
            } else {
                startGame(4);
            }
        });
    }

    private void startGame(int gameMode) {
        Intent intent = new Intent(this, OyunEkrani.class);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("PLAYER1_NAME", etPlayer1Name.getText().toString().trim());
        intent.putExtra("PLAYER2_NAME", etPlayer2Name.getText().toString().trim());
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
                    rootLayout.setBackgroundResource(R.drawable.tema_brainrot1); // Doğru görsel adını kullandık
                default:
                    rootLayout.setBackgroundResource(R.drawable.tema_varsayilan1);
                    break;
            }
        }
    }
}