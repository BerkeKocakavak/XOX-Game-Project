package com.tttgames.xoxgame;

public class Board
{
    private char[][] board;

    public char[][] getBoard() {
        return board;
    }

    public Board(char[][] board)
    {
        this.board = board;
    }

    public boolean isMovesLeft()
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                if (this.board[i][j] == '\0')
                    return true;
            }
        }
        return false;
    }

    public PlayerEnum evaluateBoard()
    {
        //Satır kontrol
        for (int row = 0; row < 3; ++row)
        {
            if (this.board[row][0] == this.board[row][1] && this.board[row][1] == this.board[row][2])
            {
                if (board[row][0] == 'X') return PlayerEnum.XPlayer;
                else if (board[row][0] == 'O') return PlayerEnum.OPlayer;
            }
        }

        //Sütun kontrol
        for (int col = 0; col < 3; ++col)
        {
            if (this.board[0][col] == this.board[1][col] && this.board[1][col] == this.board[2][col])
            {
                if (this.board[0][col] == 'X') return PlayerEnum.XPlayer;
                else if (this.board[0][col] == 'O') return PlayerEnum.OPlayer;
            }
        }

        //Çapraz kontrol
        if (this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board [2][2])
        {
            if (this.board[0][0] == 'X') return PlayerEnum.XPlayer;
            else if (this.board[0][0] == 'O') return PlayerEnum.OPlayer;
        }

        return isMovesLeft() ? null : PlayerEnum.TIE;
    }
}
