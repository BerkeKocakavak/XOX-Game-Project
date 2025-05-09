package com.tttgames.xoxgame;

import java.util.*;
public class AIPlayer extends Player
{

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    private final Difficulty difficulty;
    private Random random;

    public AIPlayer(String name, char symbol, Difficulty difficulty)
    {
        super("test", 't');
        this.difficulty = difficulty;
        this.random = new Random();
    }
    @Override
    public void makeMove(char[][] board) 
    {

    }
}
