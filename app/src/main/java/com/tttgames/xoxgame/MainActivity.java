package com.tttgames.xoxgame; // Burası sizin paket adınız olmalı

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent; // Başka Activity'lere geçiş için

// Sınıf adının GameMenuActivity olduğuna dikkat edin
public class MainActivity extends AppCompatActivity {

    private Button btnNewGame;
    private Button btnSettings;
    private Button btnScoreTable;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bu satır, GameMenuActivity'nin activity_main.xml layout dosyasını kullanacağını söyler.
        setContentView(R.layout.activity_main);

        // XML dosyasındaki bileşenleri Java kodunda kullanmak için ID'lerini kullanarak referans alıyoruz.
        tvTitle = findViewById(R.id.tvTitle);
        btnNewGame = findViewById(R.id.btnNewGame);
        btnSettings = findViewById(R.id.btnSettings);
        btnScoreTable = findViewById(R.id.btnScoreTable);

        // Şimdi butonlara tıklama olayları ekleyebiliriz.
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YeniOyunEkraniActivity.class);
                startActivity(intent);

            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ayarlar Tıklandı!", Toast.LENGTH_SHORT).show();
                // Buraya Ayarlar Activity'sine geçiş kodunu ekleyeceğiz.
            }
        });

        btnScoreTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Skor Tablosu Tıklandı!", Toast.LENGTH_SHORT).show();
                // Buraya Skor Tablosu Activity'sine geçiş kodunu ekleyeceğiz.
            }
        });

        // İsteğe bağlı: Başlık metnini Java kodundan değiştirebilirsiniz
        // tvTitle.setText("Tic Tac Toe Oyunu");
    }
}