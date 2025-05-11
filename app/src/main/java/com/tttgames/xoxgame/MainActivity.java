package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent; // Başka Activity'lere geçiş için


public class MainActivity extends AppCompatActivity {

    private Button btnNewGame;
    private Button btnSettings;
    private Button btnScoreTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // XML dosyasındaki bileşenleri Java kodunda kullanmak için ID'lerini kullanarak referans alıyoruz.
        // tvTitle = findViewById(R.id.tvTitle); // Bu satırı silin veya yorum satırı yapın
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
                Intent intent = new Intent(MainActivity.this, Ayarlar.class);
                startActivity(intent);
            }
        });

        btnScoreTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkorTablosu.class);
                startActivity(intent);
            }
        });

        // İsteğe bağlı: Başlık metnini Java kodundan değiştirebilirsiniz
        // tvTitle.setText("Tic Tac Toe Oyunu"); // Bu satırı silin veya yorum satırı yapın
    }
}