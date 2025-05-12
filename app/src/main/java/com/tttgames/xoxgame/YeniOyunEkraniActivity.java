package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

public class YeniOyunEkraniActivity extends AppCompatActivity {

    private Button btnEasy;
    private Button btnMedium;
    private Button btnHard;
    private EditText etPlayer1Name;
    private EditText etPlayer2Name;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yenioyunekrani);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        etPlayer1Name = findViewById(R.id.etPlayer1Name);
        etPlayer2Name = findViewById(R.id.etPlayer2Name);
        btnStartGame = findViewById(R.id.btnStartGame);

        // Butonlara tıklama olaylarını ayarlıyoruz. startGame metoduna parametre olarak oyun modunu gönderiyoruz.
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(1); // 1: Kolay
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(2); // 2: Orta
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(3); // 3: Zor
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String player1Name = etPlayer1Name.getText().toString().trim();
                String player2Name = etPlayer2Name.getText().toString().trim();
                if (player1Name.isEmpty() || player2Name.isEmpty()) {
                    Toast.makeText(YeniOyunEkraniActivity.this, "Oyuncu adlarını girin!", Toast.LENGTH_SHORT).show();
                } else {
                    startGame(4); // 4: Çok Oyunculu
                }
            }
        });
    }

    private void startGame(int gameMode) {
        Intent intent = new Intent(YeniOyunEkraniActivity.this, OyunEkrani.class);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("PLAYER1_NAME", etPlayer1Name.getText().toString().trim());
        intent.putExtra("PLAYER2_NAME", etPlayer2Name.getText().toString().trim());
        startActivity(intent);
    }
}
