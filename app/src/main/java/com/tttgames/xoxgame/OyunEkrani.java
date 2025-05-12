package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

public class OyunEkrani extends AppCompatActivity {

    private static final String TAG = "OyunEkrani";
    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private TextView tvTurn;
    private Button[][] buttons = new Button[3][3];
    private Board board;
    private int currentPlayer = 1;
    private boolean gameOver = false;
    private int gameMode;
    private String player1Name;
    private String player2Name;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean gameResetPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oyunekrani);

        // UI öğelerini başlat
        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvTurn = findViewById(R.id.tvTurn);

        // Butonları başlat
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        // Buton tıklama olaylarını ayarla
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] != null) {
                    final int row = i;
                    final int col = j;
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // mainHandler'da çalıştır
                            mainHandler.post(() -> {
                                try {
                                    if (gameResetPending) {
                                        resetGame();
                                        gameResetPending = false;
                                    } else {
                                        onCellClicked(row, col);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error in onClick: ", e);
                                    showToast("Bir hata oluştu: " + e.getMessage());
                                    resetGame();
                                }
                            });
                        }
                    });
                } else {
                    Log.e(TAG, "Button at position [" + i + "][" + j + "] is null!");
                }
            }
        }

        // Oyun modunu ve oyuncu adlarını al
        gameMode = getIntent().getIntExtra("GAME_MODE", 0);
        player1Name = getIntent().getStringExtra("PLAYER1_NAME");
        player2Name = getIntent().getStringExtra("PLAYER2_NAME");

        // Varsayılan oyuncu adlarını ayarla
        if (player1Name == null) {
            player1Name = "Oyuncu 1";
            Log.w(TAG, "Oyuncu 1 adı null, varsayılan kullanılıyor: " + player1Name);
        }
        if (player2Name == null) {
            player2Name = (gameMode == 4) ? "Oyuncu 2" : "Yapay Zeka";
            Log.w(TAG, "Oyuncu 2 adı null, varsayılan kullanılıyor: " + player2Name);
        }

        // Ekranı ve tahtayı ayarla
        setupGameScreen();
        initializeBoard();
    }

    private void initializeBoard() {
        // Tahtayı başlat
        char[][] initialBoard = {
                {'\0', '\0', '\0'},
                {'\0', '\0', '\0'},
                {'\0', '\0', '\0'}
        };
        board = new Board(initialBoard);
        gameOver = false;
        currentPlayer = 1;
        updateTurnLabel();
        // Butonları sıfırla
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] != null) {
                    buttons[i][j].setText("");
                    buttons[i][j].setEnabled(true);
                }
            }
        }
        gameResetPending = false;
    }

    private void setupGameScreen() {
        // Oyuncu adlarını ayarla
        if (tvPlayer1Name != null) {
            tvPlayer1Name.setText("Oyuncu 1: " + player1Name);
        }
        if (tvPlayer2Name != null) {
            tvPlayer2Name.setText("Oyuncu 2: " + player2Name);
        }
        updateTurnLabel();
    }

    private void onCellClicked(int row, int col) {
        if (gameOver) {
            return; // Oyun zaten bittiyse hiçbir şey yapma
        }

        if (board == null) {
            Log.e(TAG, "Board is null in onCellClicked!");
            return;
        }
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            Log.e(TAG, "Invalid row or column in onCellClicked! row = " + row + ", col = " + col);
            return;
        }

        if (board.getBoard()[row][col] != '\0') {
            return; // Eğer hücre doluysa ve oyun bitmemişse, hiçbir şey yapma
        }

        // Oyuncu sembolünü yerleştir
        char playerSymbol = (currentPlayer == 1) ? 'X' : 'O';
        board.getBoard()[row][col] = playerSymbol;
        Button clickedButton = buttons[row][col];
        if (clickedButton != null) {
            clickedButton.setText(String.valueOf(playerSymbol));
            clickedButton.setEnabled(false);
        } else {
            Log.e(TAG, "Button is null in onCellClicked! row = " + row + ", col = " + col);
        }

        // Oyunun bitip bitmediğini kontrol et
        PlayerEnum result = board.evaluateBoard();
        if (result != null) {
            gameOver = true;
            handleGameOver(result);
            return;
        }

        // Sıradaki oyuncuya geç
        switchPlayer();
        if (gameMode != 4) {
            computerMove();
        }
    }

    private void computerMove() {
        // Oyunun bitip bitmediğini kontrol et
        if (gameOver) return;
        if (board == null) {
            Log.e(TAG, "Board is null in computerMove!");
            return;
        }

        // Rastgele bir hareket yap
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (board.getBoard()[row][col] != '\0');

        board.getBoard()[row][col] = 'O';
        Button computerButton = buttons[row][col];
        if (computerButton != null) {
            computerButton.setText("O");
            computerButton.setEnabled(false);
        } else {
            Log.e(TAG, "Button is null in computerMove! row = " + row + ", col = " + col);
        }

        // Oyunun bitip bitmediğini kontrol et
        PlayerEnum result = board.evaluateBoard();
        if (result != null) {
            gameOver = true;
            handleGameOver(result);
            return;
        }
        switchPlayer();
    }

    private void handleGameOver(PlayerEnum result) {
        // Aktivitenin bitip bitmediğini kontrol et
        if (isFinishing()) return;

        // Sonucu al
        String message = "";
        switch (result) {
            case XPlayer:
                message = player1Name + " Kazandı!";
                break;
            case OPlayer:
                message = (gameMode == 4) ? player2Name + " Kazandı!" : "Yapay Zeka Kazandı!";
                break;
            case TIE:
                message = "Berabere!";
                break;
        }

        // Alert dialog oluştur
        final String finalMessage = message;
        mainHandler.post(() -> {
            new AlertDialog.Builder(OyunEkrani.this)
                    .setTitle("Oyun Bitti")
                    .setMessage(finalMessage)
                    .setCancelable(false)
                    .setPositiveButton("Yeni Oyun", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetGame();
                        }
                    })
                    .show();
        });
        gameOver = true;
        gameResetPending = true;
    }

    private void switchPlayer() {
        // Oyuncuyu değiştir ve etiketi güncelle
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        // Sıra etiketini güncelle
        if (tvTurn != null) {
            if (gameMode == 4) {
                tvTurn.setText("Sıra: " + (currentPlayer == 1 ? player1Name : player2Name));
            } else {
                tvTurn.setText("Sıra: " + player1Name);
            }
        }
    }

    private void resetGame() {
        // Aktivitenin bitip bitmediğini kontrol et
        if (isFinishing()) return;
        try {
            // Tahtayı ve arayüzü sıfırla
            initializeBoard();
        } catch (Exception e) {
            Log.e(TAG, "Error resetting the game", e);
            showToast("Oyun sıfırlanırken bir hata oluştu: " + e.getMessage());
        }
    }

    private void showToast(final String message) {
        // Aktivitenin bitip bitmediğini kontrol et
        if (isFinishing()) return;
        // Toast mesajını göster
        mainHandler.post(() -> Toast.makeText(OyunEkrani.this, message, Toast.LENGTH_LONG).show());
    }
}
