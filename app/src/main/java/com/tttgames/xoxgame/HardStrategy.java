package com.tttgames.xoxgame;

public class HardStrategy implements MoveStrategy {

    private final char aiSymbol = 'O';
    private final char opponentSymbol = 'X';

    @Override
    public int[] makeMove(char[][] board) {
        return findBestMove(board);
    }

    private int[] findBestMove(char[][] board) {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = aiSymbol;
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

    private int minimax(char[][] board, int depth, boolean isMax) {
        int score = evaluate(board);

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!isMovesLeft(board)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j] == '\0') {
                        board[i][j] = aiSymbol;
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = '\0';
                    }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j] == '\0') {
                        board[i][j] = opponentSymbol;
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = '\0';
                    }
            return best;
        }
    }

    private boolean isMovesLeft(char[][] board) {
        for (char[] row : board)
            for (char cell : row)
                if (cell == '\0')
                    return true;
        return false;
    }

    private int evaluate(char[][] board) {
        for (int row = 0; row < 3; ++row) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == aiSymbol) return 10;
                else if (board[row][0] == opponentSymbol) return -10;
            }
        }

        for (int col = 0; col < 3; ++col) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == aiSymbol) return 10;
                else if (board[0][col] == opponentSymbol) return -10;
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == aiSymbol) return 10;
            else if (board[0][0] == opponentSymbol) return -10;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == aiSymbol) return 10;
            else if (board[0][2] == opponentSymbol) return -10;
        }

        return 0;
    }
}
