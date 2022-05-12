package Logic.Objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
    private System[][] board = new System[12][12];
    private Random randomCalls = new Random();

    public void initBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                board[i][j] = new System(randomCalls.nextInt(104) + 154 * i + 39, randomCalls.nextInt(34) + 84 * j + 39, randomCalls.nextInt(4));
            }
        }
    }

    public System getBoardTile(int x, int y) {
        if (x < board.length && y < board[x].length) {
            return board[x][y];
        }
        return null;
    }
}
