package com.tttgames.xoxgame;

class Board {
    private char[][] board;

    public Board(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
    public void makeMoveInBoard(int row, int col, char value)
    {
        board[row][col] = value;
    }

    public void makeMoveInBoard()
    {

    }

    public char getValueInBoard(int row, int col)
    {
        return board[row][col];
    }

    public PlayerEnum evaluateBoard() {
        // Satır kontrol
        for (int row = 0; row < 3; ++row) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == 'X') return PlayerEnum.XPlayer;
                else if (board[row][0] == 'O') return PlayerEnum.OPlayer;
            }
        }

        // Sütun kontrol
        for (int col = 0; col < 3; ++col) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 'X') return PlayerEnum.XPlayer;
                else if (board[0][col] == 'O') return PlayerEnum.OPlayer;
            }
        }

        // Çapraz kontrol
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 'X') return PlayerEnum.XPlayer;
            else if (board[0][0] == 'O') return PlayerEnum.OPlayer;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 'X') return PlayerEnum.XPlayer;
            else if (board[0][2] == 'O') return PlayerEnum.OPlayer;
        }

        // Beraberlik kontrolü
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0')
                    return null; // Boş hücre varsa oyun bitmedi
            }
        }
        return PlayerEnum.TIE; // Eğer boş hücre yoksa ve kazanan yoksa, oyun berabere
    }
}