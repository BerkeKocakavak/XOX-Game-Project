package com.tttgames.xoxgame;

public class AIPlayer extends Player {  //AI player extends Player

    private final MoveStrategy moveStrategy;

    public AIPlayer(String name, char symbol, MoveStrategy moveStrategy) { //Constructor
        super(name, symbol);
        this.moveStrategy = moveStrategy;
    }

    @Override
    public int[] makeMove(char[][] board) {
        return moveStrategy.makeMove(board);
    }
}
