package com.tttgames.xoxgame;

public interface PlayerBehavior
{
    int[] makeMove(char[][] board);
    char getSymbol();
    String getName();
}
