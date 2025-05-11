package com.tttgames.xoxgame;

import java.util.*;
public class AIPlayer extends Player {

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    private final Difficulty difficulty;
    private Random random;
    private char opponentSymbol;

    public AIPlayer(String name, char symbol, Difficulty difficulty) {
        super("test", 'X');
        this.difficulty = difficulty;
        this.random = new Random();
        this.opponentSymbol = (symbol == 'X') ? 'O' : 'X';
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void makeMove(char[][] board) {
        switch (getDifficulty()) {
            case EASY:
                makeRandomMove(board);
                break;
            case HARD:
                makeMinimaxMove(board);
                break;
            case MEDIUM:
            default:
                if (Math.random() < 0.5) {
                    makeRandomMove(board);
                } else {
                    makeMinimaxMove(board);
                }
                break;
        }
    }

    private List<int[]> availableMoves(char[][] board) {
        List<int[]> list = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == '\0')
                    list.add(new int[]{i, j});
            }
        }
        return list;
    }

    private boolean isMovesLeft(char[][] board) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == '\0')
                    return true;
            }
        }
        return false;
    }

    private void makeRandomMove(char[][] board) {
        while (true) {
            int availableMoveCount = availableMoves(board).size();
            int rand = random.nextInt() % availableMoveCount;

            if (board[(int) (rand / 3)][rand % 3] != '\0') {
                board[(int) (rand / 3)][rand % 3] = 'O';
                return;
            }
        }
    }

    private void makeMinimaxMove(char[][] board)
    {
        int[] bestMove = findBestMove(board);
        if (bestMove[0] != -1) {
            board[bestMove[0]][bestMove[1]] = getSymbol();
           // System.out.println(getName() + " (" + getSymbol() + ") Minimax ile şuraya oynadı: [" + bestMove[0] + "," + bestMove[1] + "]");
        } else {
            makeRandomMove(board);
        }
    }

    private int[] findBestMove(char[][] board)
    {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0' || board[i][j] == ' ') {
                    board[i][j] = getSymbol();
                    int moveVal = minimax(board, 0, false);
                    board[i][j] = '\0';

                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    private int evaluateBoard (char[][] board)
    {
        //Sstır kontrol
        for (int row = 0; row < 3; ++row) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == getSymbol()) return 10;
                else if (board[row][0] == opponentSymbol) return -10;
            }
        }

        //Sütun kontrol
        for (int col = 0; col < 3; ++col) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == getSymbol()) return 10;
                else if (board[0][col] == opponentSymbol) return -10;
            }
        }

        //Çapraz kontrol
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == getSymbol()) return 10;
            else if (board[0][0] == opponentSymbol) return -10;
        }

        return 0;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizingPlayer) {
        int score = evaluateBoard(board);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!isMovesLeft(board)) return 0;

        if (isMaximizingPlayer)
        {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == '\0' || board[i][j] == ' ')
                    {
                        board[i][j] = getSymbol();
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = '\0'; // Geri al
                    }
                }
            }
            return best;
        }
        else
        {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (board[i][j] == '\0' || board[i][j] == ' ')
                    {
                        board[i][j] = opponentSymbol;
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = '\0'; // Geri al
                    }
                }
            }
            return best;
        }
    }
}