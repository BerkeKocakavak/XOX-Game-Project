package com.tttgames.xoxgame;

import java.util.Random;

public class MediumStrategy implements MoveStrategy { //MediumMove Strategy For AIplayer

    private final EasyStrategy easyStrategy = new EasyStrategy();
    private final HardStrategy hardStrategy = new HardStrategy();
    private final Random random = new Random();

    @Override
    public int[] makeMove(char[][] board) {
        if (random.nextBoolean()) {
            return easyStrategy.makeMove(board);
        } else {
            return hardStrategy.makeMove(board);
        }
    }
}
