package com.tttgames.xoxgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OyunEkrani extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private static final String TAG = "OyunEkrani";
    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private TextView tvTurn;
    private ImageButton[][] buttons = new ImageButton[3][3];
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
        databaseHelper = new DatabaseHelper(this);

        rootLayout = findViewById(R.id.rootLayout);
        applyThemeFromSettings();

        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvTurn = findViewById(R.id.tvTurn);
        btnReset = findViewById(R.id.btnReset);

        // ImageButton Definings
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> {
                    if (!gameOver) {
                        onCellClicked(row, col);
                    }
                });
            }
        }

        btnReset.setOnClickListener(v -> {
            resetGame();
            gameResetPending = false;
        });

        gameMode = getIntent().getIntExtra("GAME_MODE", 0);
        player1Name = getIntent().getStringExtra("PLAYER1_NAME");
        player2Name = getIntent().getStringExtra("PLAYER2_NAME");

        if (player1Name == null) player1Name = "Oyuncu 1";
        if (player2Name == null) player2Name = (gameMode == 4) ? "Oyuncu 2" : "Yapay Zeka";

        setupGameScreen();
        initializeBoard();
    }

    private void initializeBoard() {  // Initializing Board
        board = new Board(new char[3][3]);
        gameOver = false;
        currentPlayer = 1;
        updateTurnLabel();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setImageResource(0);
                buttons[i][j].setEnabled(true);
            }
        }
        gameResetPending = false;
        btnReset.setVisibility(View.GONE);
    }

    private void applyThemeFromSettings() {
        SharedPreferences prefs = getSharedPreferences("game_settings", Context.MODE_PRIVATE);
        String selectedTheme = prefs.getString("current_theme", "Varsayilan");
        String xImagePref = prefs.getString("x_image", "x_image");
        String oImagePref = prefs.getString("o_image", "o_image");

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
            }
        }

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
        }

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
        }
    }

    private void setupGameScreen() {
        if (gameMode >= 1 && gameMode <= 3) {
            tvPlayer1Name.setText("Bilgisayara Karşı");
            tvPlayer2Name.setText("");
            tvTurn.setVisibility(View.GONE);
        } else {
            tvPlayer1Name.setText("Oyuncu: " + player1Name);
            tvPlayer2Name.setText("Oyuncu: " + player2Name);
            tvTurn.setVisibility(View.VISIBLE);
        }
        updateTurnLabel();
    }

    private void onCellClicked(int row, int col) {
        if (board.getBoard()[row][col] != '\0') return;

        char symbol = currentPlayer == 1 ? 'X' : 'O';
        board.getBoard()[row][col] = symbol;

        buttons[row][col].setImageResource(symbol == 'X' ? xDrawableResId : oDrawableResId);
        buttons[row][col].setEnabled(false);

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

    private void computerMove() {  //AI move Selection
        if (gameOver) return;

        MoveStrategy strategy;
        switch (gameMode) {
            case 1:
                strategy = new EasyStrategy();
                break;
            case 2:
                strategy = new MediumStrategy();
                break;
            case 3:
                strategy = new HardStrategy();
                break;
            default:
                strategy = null;
        }

        if (strategy == null) return;

        AIPlayer ai = new AIPlayer("AI", 'O', strategy);
        int[] move = ai.makeMove(board.getBoard());

        board.makeMoveInBoard(move[0], move[1], 'O');
        buttons[move[0]][move[1]].setImageResource(oDrawableResId);
        buttons[move[0]][move[1]].setEnabled(false);

        PlayerEnum result = board.evaluateBoard();
        if (result != null) {
            gameOver = true;
            handleGameOver(result);
        } else {
            switchPlayer();
        }
    }

    private void handleGameOver(PlayerEnum result) { //According to the game situations
        String message;
        switch (result) {
            case XPlayer:
                message = (gameMode != 4) ? "Kazandınız!" : player1Name + " Kazandı!";
                break;
            case OPlayer:
                message = (gameMode != 4) ? "Yapay Zeka Kazandı!" : player2Name + " Kazandı!";
                break;
            case TIE:
                message = "Berabere!";
                break;
            default:
                message = "Bilinmeyen sonuç!";
        }

        if (gameMode == 4) {
            String winner = (result == PlayerEnum.XPlayer) ? player1Name :
                    (result == PlayerEnum.OPlayer) ? player2Name : "Draw";
            databaseHelper.addMatchResult(player1Name, player2Name, winner);
        }

        showToast(message);
        gameResetPending = true;
        showResetButton();
        tvTurn.setVisibility(View.GONE);
    }

    private void showResetButton() {
        btnReset.setVisibility(View.VISIBLE);
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
        updateTurnLabel();
    }

    private void updateTurnLabel() {
        if (gameMode == 4) {
            tvTurn.setText("Sıra: " + (currentPlayer == 1 ? player1Name : player2Name));
        }
    }

    private void resetGame() {
        initializeBoard();
        if (gameMode == 4) {
            tvTurn.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(final String message) {
        mainHandler.post(() -> Toast.makeText(OyunEkrani.this, message, Toast.LENGTH_LONG).show());
    }
}
