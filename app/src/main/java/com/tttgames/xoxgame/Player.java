package com.tttgames.xoxgame;

public abstract class Player implements PlayerBehavior {
    private String name;

    //X or O
    private char symbol;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public abstract int[] makeMove(char[][] board);

    public String getName() {
        return this.name;
    }
}