package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log; // Log sınıfını ekledik

public class OyunEkrani extends AppCompatActivity {

    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private TextView tvTurn;

    private int gameMode;
    private String player1Name;
    private String player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oyunekrani);

        // Layout elemanlarını bul
        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvTurn = findViewById(R.id.tvTurn);

        // Gelen verileri al ve logla
        gameMode = getIntent().getIntExtra("GAME_MODE", 0);
        player1Name = getIntent().getStringExtra("PLAYER1_NAME");
        player2Name = getIntent().getStringExtra("PLAYER2_NAME");

        Log.d("GameActivity", "onCreate: gameMode = " + gameMode);
        Log.d("GameActivity", "onCreate: player1Name = " + player1Name);
        Log.d("GameActivity", "onCreate: player2Name = " + player2Name);

        // Gelen verilere göre UI'ı ayarla
        setupGameScreen();
    }

    private void setupGameScreen() {
        // Oyun moduna göre arayüzü ayarla
        switch (gameMode) {
            case 1: // Kolay
                tvPlayer1Name.setText(player1Name + " (X)");
                tvPlayer2Name.setText("Bilgisayar (O)");
                tvTurn.setText("Sıra: " + player1Name);
                break;
            case 2: // Orta
                tvPlayer1Name.setText(player1Name + " (X)");
                tvPlayer2Name.setText("Bilgisayar (O)");
                tvTurn.setText("Sıra: " + player1Name);
                break;
            case 3: // Zor
                tvPlayer1Name.setText(player1Name + " (X)");
                tvPlayer2Name.setText("Bilgisayar (O)");
                tvTurn.setText("Sıra: " + player1Name);
                break;
            case 4: // Çok Oyunculu
                tvPlayer1Name.setText(player1Name + " (X)");
                tvPlayer2Name.setText(player2Name + " (O)");
                tvTurn.setText("Sıra: " + player1Name);
                break;
            default:
                tvTurn.setText("Geçersiz oyun modu!");
                break;
        }
    }
}
