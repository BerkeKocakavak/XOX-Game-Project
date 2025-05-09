package com.tttgames.xoxgame; // Bu satırı kendi paket adınızla değiştirin

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class YeniOyunEkraniActivity extends AppCompatActivity {

    // XML dosyasındaki bileşenlere erişmek için değişkenler
    private Button btnEasy;
    private Button btnMedium;
    private Button btnHard;
    private EditText etPlayer1Name;
    private EditText etPlayer2Name;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hangi layout dosyasının kullanılacağını belirtiyoruz
        setContentView(R.layout.yenioyunekrani); //  activity_game_mode_selection.xml yerine yenioyunekrani.xml kullanıyoruz

        // Layout dosyasındaki bileşenleri Java kodunda kullanmak için ID'lerini buluyoruz
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        etPlayer1Name = findViewById(R.id.etPlayer1Name);
        etPlayer2Name = findViewById(R.id.etPlayer2Name);
        btnStartGame = findViewById(R.id.btnStartGame);

        // Butonlara tıklama olaylarını tanımlıyoruz
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Kolay" butonuna tıklandığında yapılacaklar
                Toast.makeText(YeniOyunEkraniActivity.this, "Kolay mod seçildi!", Toast.LENGTH_SHORT).show();
                // Burada oyun başlatma işlemini yapabilirsiniz, örneğin:
                // Intent intent = new Intent(GameModeSelectionActivity.this, OyunEkraniActivity.class);
                // intent.putExtra("mod", "kolay"); // Seçilen modu diğer Activity'ye gönderebilirsiniz
                // startActivity(intent);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Orta" butonuna tıklandığında yapılacaklar
                Toast.makeText(YeniOyunEkraniActivity.this, "Orta mod seçildi!", Toast.LENGTH_SHORT).show();
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Zor" butonuna tıklandığında yapılacaklar
                Toast.makeText(YeniOyunEkraniActivity.this, "Zor mod seçildi!", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Oyunu Başlat" butonuna tıklandığında yapılacaklar
                String player1Name = etPlayer1Name.getText().toString().trim();
                String player2Name = etPlayer2Name.getText().toString().trim();

                if (player1Name.isEmpty() || player2Name.isEmpty()) {
                    Toast.makeText(YeniOyunEkraniActivity.this, "Oyuncu adlarını girin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Oyuncu adları girilmişse oyunu başlat
                    Toast.makeText(YeniOyunEkraniActivity.this, "Oyun başlıyor: " + player1Name + " vs. " + player2Name, Toast.LENGTH_SHORT).show();
                    // Burada oyun başlatma işlemini yapabilirsiniz, örneğin:
                    // Intent intent = new Intent(GameModeSelectionActivity.this, OyunEkraniActivity.class);
                    // intent.putExtra("mod", "coklu");
                    // intent.putExtra("oyuncu1", player1Name);
                    // intent.putExtra("oyuncu2", player2Name);
                    // startActivity(intent);
                }
            }
        });
    }
}
