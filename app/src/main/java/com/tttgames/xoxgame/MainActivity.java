package com.tttgames.xoxgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnNewGame, btnSettings, btnScoreTable;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);

        btnNewGame = findViewById(R.id.btnNewGame);
        btnSettings = findViewById(R.id.btnSettings);
        btnScoreTable = findViewById(R.id.btnScoreTable);

        btnNewGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, YeniOyunEkraniActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Ayarlar.class);
            startActivity(intent);
        });

        btnScoreTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SkorVeIstatistikActivity.class);
            startActivity(intent);
        });

        // Temayı onCreate'te de uygula (ilk açılış için)
        applyThemeFromSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aktivite ön plana çıktığında temayı tekrar uygula
        applyThemeFromSettings();
    }

    private void applyThemeFromSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("game_settings", Context.MODE_PRIVATE);
        String selectedTheme = sharedPreferences.getString("other_screens_theme", "Varsayilan");

        if (rootLayout != null) {
            switch (selectedTheme) {
                case "SiyahBeyaz":
                    rootLayout.setBackgroundResource(R.drawable.tema_siyahbeyaz);
                    break;
                case "KirmiziTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_kirmizipembe);
                    break;
                case "BrainRotTema":
                    rootLayout.setBackgroundResource(R.drawable.tema_brainrot);
                    break;
                default:
                    rootLayout.setBackgroundResource(R.drawable.tema_varsayilan);
                    break;
            }
        }
    }
}