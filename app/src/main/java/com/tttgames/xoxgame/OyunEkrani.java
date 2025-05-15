package com.tttgames.xoxgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;

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
    private Button btnReset;
    private LinearLayout rootLayout;
    private int xDrawableResId = R.drawable.x_image;
    private int oDrawableResId = R.drawable.o_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oyunekrani);

        rootLayout = findViewById(R.id.rootLayout);
        applyThemeFromSettings(); // Tema ve ikonları uygula
        // UI öğelerini başlat
        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvTurn = findViewById(R.id.tvTurn);
        btnReset = findViewById(R.id.btnReset);

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
                            mainHandler.post(() -> {
                                try {
                                    if (!gameOver) { // Oyun bitmemişse hücreye tıklanabilir
                                        onCellClicked(row, col);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error in onClick: ", e);
                                    showToast("Bir hata oluştu: " + e.getMessage());
                                }
                            });
                        }
                    });
                } else {
                    Log.e(TAG, "Button at position [" + i + "][" + j + "] is null!");
                }
            }
        }

        // Reset butonu tıklama olayını ayarla
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHandler.post(() -> {
                    resetGame();
                    gameResetPending = false;
                });
            }
        });

        // Oyun modunu ve oyuncu adlarını al
        gameMode = getIntent().getIntExtra("GAME_MODE", 0);
        player1Name = getIntent().getStringExtra("PLAYER1_NAME");
        player2Name = getIntent().getStringExtra("PLAYER2_NAME");

        // Varsayılan adlar
        if (player1Name == null) player1Name = "Oyuncu 1";
        if (player2Name == null) player2Name = (gameMode == 4) ? "Oyuncu 2" : "Yapay Zeka";

        setupGameScreen();
        initializeBoard();
    }

    private void initializeBoard() {
        char[][] initialBoard = {
                {'\0', '\0', '\0'},
                {'\0', '\0', '\0'},
                {'\0', '\0', '\0'}
        };
        board = new Board(initialBoard);
        gameOver = false;
        currentPlayer = 1;
        updateTurnLabel(); // Başlangıçta oyuncu adını göster
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] != null) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackgroundResource(0);
                    buttons[i][j].setEnabled(true);
                }
            }
        }
        gameResetPending = false;
        btnReset.setVisibility(View.GONE);
    }
    private void applyThemeFromSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("game_settings", Context.MODE_PRIVATE);
        String selectedTheme = sharedPreferences.getString("current_theme", "Varsayilan");
        String xImagePref = sharedPreferences.getString("x_image", "x_image");
        String oImagePref = sharedPreferences.getString("o_image", "o_image");

        // Arka plan temasını uygula
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

        // X ikonu kaynağını belirle... (Aynı kalabilir)
        switch (xImagePref) {
            case "x_pembe":
                xDrawableResId = R.drawable.x_pembe;
                break;
            case "x_br":
                xDrawableResId = R.drawable.x_br;
                break;
            case "x_gri":
                xDrawableResId = R.drawable.x_gri;
                break;
            default:
                xDrawableResId = R.drawable.x_image;
                break;
        }

        // O ikonu kaynağını belirle... (Aynı kalabilir)
        switch (oImagePref) {
            case "o_pembe":
                oDrawableResId = R.drawable.o_pembe;
                break;
            case "o_br":
                oDrawableResId = R.drawable.o_br;
                break;
            case "o_gri":
                oDrawableResId = R.drawable.o_gri;
                break;
            default:
                oDrawableResId = R.drawable.o_image;
                break;
        }
    }


        private void setupGameScreen() {
        if (gameMode >= 1 && gameMode <= 3) {
            tvPlayer1Name.setText("Bilgisayara Karşı");
            tvPlayer2Name.setText("");
            tvTurn.setVisibility(View.GONE); // Bilgisayar oyununda sırayı gizle
        } else {
            tvPlayer1Name.setText("Oyuncu: " + player1Name);
            tvPlayer2Name.setText("Oyuncu: " + player2Name);
            tvTurn.setVisibility(View.VISIBLE); // Çok oyunculu modda sırayı göster
        }
        updateTurnLabel();
    }

    private void onCellClicked(int row, int col) {
        if (gameOver || board == null || row < 0 || row > 2 || col < 0 || col > 2) return;
        if (board.getBoard()[row][col] != '\0') return;

        char playerSymbol = (currentPlayer == 1) ? 'X' : 'O';
        board.getBoard()[row][col] = playerSymbol;

        Button clickedButton = buttons[row][col];
        if (clickedButton != null) {
            if (playerSymbol == 'X') {
                clickedButton.setBackgroundResource(xDrawableResId);
            } else {
                clickedButton.setBackgroundResource(oDrawableResId);
            }
            clickedButton.setEnabled(false);
        } else {
            Log.e(TAG, "Button is null in onCellClicked! row = " + row + ", col = " + col);
        }

        PlayerEnum result = board.evaluateBoard();
        if (result != null) {
            gameOver = true;
            handleGameOver(result);
            return;
        }

        switchPlayer();

        if (gameMode != 4) {
            computerMove();
        }
    }

    private void computerMove() {
        if (gameOver) return;



        AIPlayer.Difficulty difficulty = null;
        switch (gameMode) {
            case 1:
                difficulty = AIPlayer.Difficulty.EASY;
                break;
            case 2:
                difficulty = AIPlayer.Difficulty.MEDIUM;
                break;
            case 3:
                difficulty = AIPlayer.Difficulty.HARD;
                break;
        }

        AIPlayer ai = new AIPlayer("AI", 'O', difficulty, board);
        int[] move = ai.makeMove(board.getBoard());

        board.getBoard()[move[0]][move[1]] = 'O';
        buttons[move[0]][move[1]].setBackgroundResource(oDrawableResId);
        buttons[move[0]][move[1]].setEnabled(false);

        PlayerEnum result = board.evaluateBoard();
        if (result != null) {
            gameOver = true;
            handleGameOver(result);
        } else {
            switchPlayer();
        }
    }
    private void handleGameOver(PlayerEnum result) {
        if (isFinishing()) return;

        String message;
        switch (result) {
            case XPlayer:
                message = player1Name + " Kazandı!";
                if (gameMode != 4) {
                    message = "Kazandınız!";
                }
                break;
            case OPlayer:
                message = (gameMode == 4) ? player2Name + " Kazandı!" : "Yapay Zeka Kazandı!";
                break;
            case TIE:
                message = "Berabere!";
                break;
            default:
                message = "Bilinmeyen sonuç!";
                break;
        }

        showToast(message);
        gameResetPending = true;
        showResetButton();
        if (gameMode == 4) {
            tvTurn.setVisibility(View.GONE); // Oyun bittiğinde çok oyunculu modda sırayı gizle
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        if (tvTurn != null) {
            if (gameMode >= 1 && gameMode <= 3) {
                tvTurn.setText("");
            } else {
                tvTurn.setText("Sıra: " + ((currentPlayer == 1) ? player1Name : player2Name));
            }
        }
    }

    private void resetGame() {
        if (isFinishing()) return;
        try {
            initializeBoard();
            if (gameMode == 4) {
                tvTurn.setVisibility(View.VISIBLE); // Oyunu yeniden başlattığında çok oyunculu modda sırayı göster
            }
        } catch (Exception e) {
            Log.e(TAG, "Error resetting the game", e);
            showToast("Oyun sıfırlanırken bir hata oluştu: " + e.getMessage());
        }
    }

    private void showToast(final String message) {
        if (isFinishing()) return;
        mainHandler.post(() -> Toast.makeText(OyunEkrani.this, message, Toast.LENGTH_LONG).show());
    }

    private void showResetButton() {
        if (btnReset != null) {
            btnReset.setVisibility(View.VISIBLE);
        }
    }
}
