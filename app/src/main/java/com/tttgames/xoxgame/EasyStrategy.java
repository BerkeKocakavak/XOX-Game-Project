package com.tttgames.xoxgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasyStrategy implements MoveStrategy {

    private final Random random = new Random();

    @Override
    public int[] makeMove(char[][] board) {
        List<int[]> availableMoves = new ArrayList<>();

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                if (board[i][j] == '\0')
                    availableMoves.add(new int[]{i, j});

        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}
